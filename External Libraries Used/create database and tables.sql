# create the database required
DROP DATABASE IF EXISTS `issue_tracker_system`;
CREATE DATABASE IF NOT EXISTS `issue_tracker_system`;
USE `issue_tracker_system`;

# create the tables required

CREATE TABLE project
(
	projectID INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100),

    PRIMARY KEY(projectID)
);

CREATE TABLE user
(
	userID INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100),

    PRIMARY KEY(userID)
);

CREATE TABLE issue
(
	issueID INT NOT NULL AUTO_INCREMENT,
    projectID INT,
    creatorID INT,
    assigneeID INT,
    title VARCHAR(100),
    description TEXT,
    time TIMESTAMP,
    tag VARCHAR(100),
    priority INT,
    status VARCHAR(100),


    PRIMARY KEY(issueID),
    FOREIGN KEY (projectID) REFERENCES project(projectID),
    FOREIGN KEY (creatorID) REFERENCES user(userID),
    FOREIGN KEY (assigneeID) REFERENCES user(userID)
);

CREATE TABLE comment
(
	commentID INT NOT NULL AUTO_INCREMENT,
    issueID INT,
    userID INT,
    time TIMESTAMP,
    description TEXT,
    reactions TEXT,

    PRIMARY KEY(commentID),
    FOREIGN KEY (issueID) REFERENCES issue(issueID),
    FOREIGN KEY (userID) REFERENCES user(userID)
);



### some initial values
USE `issue_tracker_system`;

INSERT INTO project VALUES( 1, "TableView Widget");
INSERT INTO project VALUES( 2, "Project D");
INSERT INTO project VALUES( 3, "Project C");
INSERT INTO project VALUES( 4, "Project B");
INSERT INTO project VALUES( 5, "Project A");

INSERT INTO user VALUES(1, "loh", "loh@gmail.com", "LOH");
INSERT INTO user VALUES(2, "yen", "yen@gmail.com", "YEN");
INSERT INTO user VALUES(3, "shen", "shen@gmail.com", "SHEN");
INSERT INTO user VALUES(4, "shawn", "shawn@gmail.com", "SHAWN");
INSERT INTO user VALUES(5, "mentos", "mentos@gmail.com", "MENTOS");

#                         issueID, projectID, creatorID, assigneeID,  title,                          description,                      time,                   tag,         priority,             status
INSERT INTO issue VALUES(       1,         1,          1,         2, "Can't display the table",  "Hi\nI can't display the table\nhaha",         CURRENT_TIMESTAMP(),    "Frontend",         4,     "IN PROGRESS");
INSERT INTO issue VALUES(       2,         1,          1,         3, "Can't open file",          "Hi\nI can't open the file\nhaha",            CURRENT_TIMESTAMP(),    "Backend",          3,     "OPEN");
INSERT INTO issue VALUES(       3,         2,          2,         4, "Backend issue gua",        "Some issue 1\nNeed to fix" ,                 "2000-01-01 01:01:01.0", "Backend",         2,     "OPEN");

#                          commentID, issueID, userID, time,                  description, reactions
INSERT INTO comment VALUES(1        , 1      , 2     ,CURRENT_TIMESTAMP(),   "can't 1"   , "");
INSERT INTO comment VALUES(2        , 1      , 2     ,CURRENT_TIMESTAMP(),   "same issue& project but description doesnt match"   , "");
INSERT INTO comment VALUES(3        , 3      , 2     ,CURRENT_TIMESTAMP(),   "can't 2 (description matches but it belongs to other project"   , "");

#
#
#