# tutorial - https://gist.github.com/aravindhebbali/f2cc73794e9f9bfaa673

# establish connection with database
rm(list = ls())
library('RMySQL')
library('dplyr')
library('ggplot2')
library('lubridate')
con <- dbConnect( RMySQL::MySQL(),
                  dbname = "issue_tracker_system",
                  user = "root",
                  password = "wix1002"
)


# read the table 'issue'
issue_original      <- data.frame( dbReadTable(con, 'issue') )
user_original       <- data.frame( dbReadTable(con, 'user') )

# convert 'time' column from char to POSIXct
issue_original$time <- as.POSIXct( issue_original$time, format="%Y-%m-%d %H:%M:%S")

# filter rows 7 days from now
# issue <-
#   issue_original %>%
#     filter( time > (now()-days(7))  )
issue <- issue_original


########################################################################################################################
# issue by status


issue_by_status <-
  issue %>%
    group_by(status) %>%
    summarise( frequency = n() )

pdf( paste0(getwd(),'/Report Generation/Number of Issues for each STATUS.pdf') )
ggplot(issue_by_status, aes(x=status, y=frequency, fill=status)) +
  geom_bar(stat="identity") +
  geom_text(aes(label=frequency), vjust=1.6, color="black", size=5) +
  ggtitle('Number of Issues for each STATUS')
  theme_minimal()
dev.off()


########################################################################################################################
# issue by tag

issue_by_label <-
  issue %>%
    group_by(tag) %>%
    summarise( frequency = n() ) %>%
    arrange( desc(frequency) )

most_frequent_label <-
  issue_by_label %>%
    filter( frequency == max(frequency) )
most_frequent_label_title <- paste( "Most Frequent Tag(s) = ", paste(most_frequent_label$tag, collapse=", ") )

pdf( paste0(getwd(),'/Report Generation/Number of Issues for each TAG.pdf') )
ggplot(issue_by_label, aes(x=tag, y=frequency, fill=tag)) +
  geom_bar(stat="identity") +
  geom_text(aes(label=frequency), vjust=1.6, color="black", size=5) +
  ggtitle(most_frequent_label_title)
  theme_minimal()
dev.off()


########################################################################################################################
# TOP PERFORMER in team

# PART 1 - obtain names of assignees that resolved issues
query <-  "
          SELECT u.name
          FROM user u
          WHERE u.userID = ANY(
                              SELECT
                                  i.assigneeID
                              FROM  issue i
                              WHERE i.status = 'Resolved'
                              GROUP BY (i.assigneeID)
                              ORDER BY COUNT(*) DESC
                              )

          "
assigneeNames <- dbGetQuery(con, query)$name



# PART 2 - obtain no of issues resolved by assignees
query <- "
          SELECT
              COUNT(*) AS issues_resolved
          FROM  issue i
          WHERE i.status = 'Resolved'
          GROUP BY (i.assigneeID)
          ORDER BY issues_resolved DESC
         "
assigneeIssuesResolved <- as.integer( dbGetQuery(con, query)$issues_resolved )

# PART 3 - combine them into a dataframe
assignee_issuesResolved <- data.frame( assignee_name=assigneeNames, issues_resolved=assigneeIssuesResolved )



top_performing_assignee <-
  assignee_issuesResolved %>%
    filter( issues_resolved==max(issues_resolved) )
top_performing_assignee_title <- paste( "Top Perfomer(s) = ", paste(top_performing_assignee$assignee_name, collapse=", ") )


pdf( paste0(getwd(),'/Report Generation/Number of Issues Solved per ASSIGNEE.pdf') )
ggplot(assignee_issuesResolved, aes(x=assignee_name, y=issues_resolved, fill=assignee_name)) +
  geom_bar(stat="identity") +
  geom_text(aes(label=issues_resolved), vjust=1.6, color="black", size=5) +
  ggtitle(top_performing_assignee_title)
  theme_minimal()
dev.off()



########################################################################################################################

dbDisconnect(con)


