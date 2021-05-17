package system;

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
    }
    private void insertAllUsers() throws SQLException, ClassNotFoundException {
        for (User user : users)
            UserQuery.insertNewUser( user );
    }
}
