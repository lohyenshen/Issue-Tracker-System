# tutorial - https://gist.github.com/aravindhebbali/f2cc73794e9f9bfaa673

# establish connection with database
rm(list = ls())
# install.packages('RMySQL')
# install.packages('dplyr')
# install.packages('ggplot2')
# install.packages('lubridate')
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

pdf( paste0(getwd(),'/Report Generation/1) Number of Issues for each STATUS.pdf') )
# ggplot(issue_by_status, aes(x=status, y=frequency, fill=status)) +
#   geom_bar(stat="identity") +
#   geom_text(aes(label=frequency), vjust=1.6, color="black", size=5) +
#   ggtitle('Number of Issues for each STATUS') +
#   theme_minimal()
ggplot(issue_by_status, aes(x="", y=frequency, fill=status)) +
  geom_bar(stat="identity", width=1) +
  coord_polar("y", start=0) +
  geom_text(aes(label = frequency), position = position_stack(vjust=0.5), color="black", size=5) +
  ggtitle('Number of Issues for each STATUS') +
  labs(x = NULL, y = NULL, fill = NULL)
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

pdf( paste0(getwd(),'/Report Generation/2) Number of Issues for each TAG.pdf') )
ggplot(issue_by_label, aes(x=tag, y=frequency, fill=tag)) +
  geom_bar(stat="identity") +
  geom_text(aes(label=frequency), vjust=1.6, color="black", size=5) +
  ggtitle(most_frequent_label_title) +
  theme_classic()
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


pdf( paste0(getwd(),'/Report Generation/3) Number of Issues Solved per ASSIGNEE.pdf') )
ggplot(assignee_issuesResolved, aes(x=assignee_name, y=issues_resolved, fill=assignee_name)) +
  geom_bar(stat="identity") +
  geom_text(aes(label=issues_resolved), vjust=1.6, color="black", size=5) +
  ggtitle(top_performing_assignee_title) +
  theme_classic()
dev.off()



########################################################################################################################
# line graph - issue frequency VS time (day/week/month)

# day
issue <-
  issue_original %>%
    filter( time > (now()-hours(24))  )

time      <- c()
frequency <- c()
for (i in 23:0){
  current <- now()-hours(i)

  time      <- append( time, current)
  frequency <- append( frequency, sum( issue$time < current) )
}
df <- data.frame( time, frequency)



pdf( paste0(getwd(),'/Report Generation/4) Issue Frequency 1 DAY from now.pdf') )
ggplot(df, aes(x=time, y=frequency)) +
  geom_line() +
  ggtitle(paste0('Issue Frequency 1 DAY from now')) +
  theme_classic()
dev.off()


# week
issue <-
  issue_original %>%
    filter( time > (now()-days(7))  )

time  <- c()
frequency <- c()
for (i in 6:0){
  current <- now()-days(i)

  time      <- append( time, current)
  frequency <- append( frequency, sum( issue$time < current) )
}
df <- data.frame( time, frequency)



pdf( paste0(getwd(),'/Report Generation/5) Issue Frequency 1 WEEK from now.pdf') )
ggplot(df, aes(x=time, y=frequency)) +
  geom_line() +
  ggtitle(paste0('Issue Frequency 1 WEEK from now')) +
  theme_classic()
dev.off()


# month
issue <-
  issue_original %>%
    filter( time > (now()-days(30))  )

time      <- c()
frequency <- c()
for (i in 29:0){
  current   <- now()-days(i)

  time      <- append( time, current)
  frequency <- append( frequency, sum( issue$time < current) )
}
df <- data.frame( time, frequency)



pdf( paste0(getwd(),'/Report Generation/6) Issue Frequency 1 MONTH from now.pdf') )
ggplot(df, aes(x=time, y=frequency)) +
  geom_line() +
  ggtitle(paste0('Issue Frequency 1 MONTH from now')) +
  theme_classic()
dev.off()


# year
issue <-
  issue_original %>%
    filter( time > (now()-months(12))  )

time      <- c()
frequency <- c()
for (i in 11:0){
  current   <- now()-months(i)

  time      <- append( time, current)
  frequency <- append( frequency, sum( issue$time < current) )
}
df <- data.frame( time, frequency)



pdf( paste0(getwd(),'/Report Generation/7) Issue Frequency 1 YEAR from now.pdf') )
ggplot(df, aes(x=time, y=frequency)) +
  geom_line() +
  ggtitle(paste0('Issue Frequency 1 YEAR from now')) +
  theme_classic()
dev.off()


dbDisconnect(con)


