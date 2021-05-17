package database;

import javax.sql.rowset.*;
import java.sql.*;
public class Query {
    /*
    protected static Connection con;       // con = DriverManager.getConnection( url , uname , mysqlPassword);
    protected static Statement st;         // st = con.createStatement();
    protected static ResultSet rs;         // rs = st.executeQuery("query");
    protected static PreparedStatement pst;// pst = con.prepareStatement(query);
    */

    // st.close();
    // con.close();

    // executeQuery("query")   -> get result                   //
    // executeUpdate("query")  -> update to table in database  // DDL, DML, DQL

    /**QUERYING FROM DATABASE
     *
     * query = "select ~ from  ~;";
     *
     * Statement st = con.createStatement();
     * ResultSet rs = st.executeQuery(query);
     *
     * st.close();
     * con.close();
     */


    /**UPDATE TO DATABASE
     * query = "insert into student values (?,?)"
     *
     * PreparedStatement pst = con.prepareStatement(query);
     * pst.setInt(1, userID);
     * pst.setString(2, username);
     * pst.executeUpdate();
     *
     * pst.close();
     * con.close();
     */

    /**
     * establish connection to database (MySQL)
     */
    protected static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/issue_tracker_system?autoReconnect=true&useSSL=false";
        String uname = "root";
        String mysqlPassword = "wix1002";
        return DriverManager.getConnection( url , uname , mysqlPassword);
    }

    /**
     * return the size of ResultSet
     */
    protected static int size(ResultSet rs){
        int size = 0;
        try{
            if (rs != null){
                rs.last();          // moves cursor user_details table's last row
                size = rs.getRow(); // get last row id which is equal to the length
                rs.beforeFirst();
            }
        }
        catch(Exception ignored){}
        return size;
    }

    /**
     * return ResultSet based on query
     *
     * ------------
     * CachedRowSet - allow us to close database connection whilst preserving the data from database
     * ------------
     *
     * https://www.codejava.net/java-se/jdbc/how-to-use-cachedrowset-in-jdbc
     * https://www.youtube.com/watch?v=wilYFaDQPQs
     */
    protected static ResultSet constructResultSet( String query) throws SQLException, ClassNotFoundException{
        Connection con = getConnection();
        Statement st = con.createStatement();
        ResultSet rs =  st.executeQuery( query );

        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet rowset  = factory.createCachedRowSet();
        rowset.populate(rs);
        con.close();
        st.close();
        return rowset;
    }
}
