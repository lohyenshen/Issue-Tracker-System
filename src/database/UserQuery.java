package database;

import system.User;

import java.sql.*;

public class UserQuery extends Query{

    /**
     * return array of all users in the database
     */
    public static User[] getUsers() throws SQLException, ClassNotFoundException{
        String query = "SELECT * FROM user;";
        Connection con = getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery( query );

        int L = size( rs );
        User[] users = new User[L];

        for (int i = 0; i < users.length; i++) {
            rs.next();

            int userID = rs.getInt("userID");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String password = rs.getString("password");

            users[i] = new User(userID, name, email, password);
        }

        st.close();
        con.close();
        return users;
    }

    /**
     * insert a record of user into database
     */
    public static void insertNew(User u) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO user VALUES(?,?,?,?)";
        Connection con = getConnection();
        PreparedStatement pst  = con.prepareStatement(query);

        // userID, name, email, password
        pst.setInt(1, u.getUserID());
        pst.setString(2, u.getName());
        pst.setString(3, u.getEmail());
        pst.setString(4, u.getPassword());
        pst.executeUpdate();

        pst.close();
        con.close();
    }

    /**
     * return a user based on userIDToSearch
     */
    public static User getUser(int userIDToSearch) throws SQLException, ClassNotFoundException {
        String query =
                "SELECT *\n" +
                "FROM user\n" +
                "WHERE userID = " + userIDToSearch + " ;";
        Connection con = getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery( query );

        rs.next();
        int userID = rs.getInt("userID");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");

        st.close();
        con.close();
        return new User(userID, name, email, password);
    }
}
