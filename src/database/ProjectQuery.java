package database;

import classes.*;
import java.sql.*;

public class ProjectQuery extends Query{
    /**
     * return array of projects based on result set
     */
    private static Project[] constructProjects( ResultSet rs) throws SQLException, ClassNotFoundException{
        int L = size( rs );
        Project[] projects = new Project[L];

        for (int i = 0; i < projects.length; i++) {
            rs.next();

            // projectID, name
            int projectID = rs.getInt("projectID");
            String name = rs.getString("name");
            Issue[] c = IssueQuery.getIssues(projectID);

            projects[i] = new Project(projectID, name, c);
        }
        return projects;
    }

    /**
     * return a project based on "project ID"
     */
    public static Project getProject( int projectIDToSearch) throws SQLException, ClassNotFoundException{
        String query =
                "SELECT *\n" +
                "FROM project\n" +
                "WHERE projectID = "+ projectIDToSearch + " ;";

        return constructProjects( constructResultSet( query ) )[0]; // 0 index bcuz it will only return 1 specific project (projectID is PRIMARY KEY)
    }

    /**
     * return a project based on "project name"
     */
    public static Project getProject( String projectNameToSearch) throws SQLException, ClassNotFoundException{
        String query =
                "SELECT *\n" +
                "FROM project\n" +
                "WHERE name = \""+ projectNameToSearch + "\" ;";

        return constructProjects( constructResultSet( query ) )[0]; // 0 index bcuz it will only return 1 specific project (projectID is PRIMARY KEY)
    }

    /**
     * return an array of all projects in the database
     */
    public static Project[] getProjects_Unsorted() throws SQLException, ClassNotFoundException{
        String query = "SELECT * FROM project;";
        return constructProjects( constructResultSet( query ) );
    }

    /**
     * @param asc_or_desc - true(ASC)     false(DESC)
     * @return array of projects sorted by 'alphanumeric'
     */
    public static Project[] getProjects_SortedBy_Alphanumeric( boolean asc_or_desc) throws SQLException, ClassNotFoundException {
        String order = (asc_or_desc) ? "ASC" : "DESC";
        String query =
                "SELECT *\n" +
                "FROM project\n" +
                "ORDER BY name "+order+" ;";
                          ////
        return constructProjects( constructResultSet( query ) );
    }

    /**
     * @param asc_or_desc - true(ASC)     false(DESC)
     * @return array of projects sorted by 'projectID'
     */
    public static Project[] getProjects_SortedBy_projectID( boolean asc_or_desc) throws SQLException, ClassNotFoundException {
        String order = (asc_or_desc) ? "ASC" : "DESC";
        String query =
                "SELECT *\n" +
                "FROM project\n" +
                "ORDER BY projectID "+order+" ;";
        return constructProjects( constructResultSet( query ) );
    }

    /**
     * return an array projects searched by "project name"
     */
    public static Project[] getProjects_SearchBy_ProjectName( String projectName) throws SQLException, ClassNotFoundException {
        String query =
                "SELECT * \n" +
                "FROM project\n" +
                "WHERE name REGEXP \""+ projectName +"\";";
        return constructProjects( constructResultSet( query ) );
    }


    public static void insertNewProject(Project p) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO project VALUES(?,?)";
        Connection con = getConnection();
        PreparedStatement pst  = con.prepareStatement(query);

        pst.setInt(1,   0);// PRIMARY KEY (auto_increment)
        pst.setString(2, p.getName());
        pst.executeUpdate();

        pst.close();
        con.close();
    }
}
