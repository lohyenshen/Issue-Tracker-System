package classes;

import database.CommentQuery;
import database.IssueQuery;
import database.ProjectQuery;
import database.UserQuery;

import java.sql.SQLException;

/**
 * 'Projects_And_Users' is a class which comprises of 2 instance variables (Used sorely for GSON class to parse Json object)
 *
 * - projects
 * - users
 */
public class Projects_And_Users {
    private Project[] projects;
    private User[] users;


    public Projects_And_Users(Project[] projects, User[] users){
        this.projects = projects;
        this.users = users;
    }

    /**
     * insert both NEW 'projects' and 'user' into database
     */
    public void insertIntoDatabase() throws SQLException, ClassNotFoundException {
        insertAllUsers();
        insertAllProjects();
    }
    private void insertAllProjects() throws SQLException, ClassNotFoundException {
        /*for (Project p : projects){
            ProjectQuery.insertNewProject( p );

            for (Issue i : p.getIssues()){
                IssueQuery.insertNewIssue( i );

                for (Comment c : i.getComments()){
                    CommentQuery.insertNewComment( c );
                }
            }
        }*/

        for (Project p : projects){
            ProjectQuery.insertNewProject( p );

            for (Issue i : p.getIssues()){
                i.setProjectID( ProjectQuery.getProject(p.getName()).getProjectID() );  // get actual projectID in database
                i.setCreator(   UserQuery.getUser(i.getCreator().getName()) );          // get actual creator (with actual userID in database)
                i.setAssignee(  UserQuery.getUser(i.getAssignee().getName()));          // get actual assignee (with actual userID in database)
                IssueQuery.insertNewIssue( i );

                for (Comment c : i.getComments()){
                    System.out.println(i.getTitle());
                    c.setIssueID( IssueQuery.getIssue(i.getTitle()).getIssueID() );     // get actual issueID in database
                    c.setCommentUser( UserQuery.getUser(c.getCommentUser().getName()) );// get actual commentUser (with actual userID in database)
                    c.setHasPicture(false);

                    CommentQuery.insertNewComment( c );
                }
            }
        }
    }
    private void insertAllUsers() throws SQLException, ClassNotFoundException {
        for (User user : users)
            UserQuery.insertNewUser( user );
    }
}
