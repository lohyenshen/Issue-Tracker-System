package database;

import classes.*;

import java.sql.*;

public class UserQuery extends Query{
    /**
     * return array of users based on result set
     */
    private static User[] constructUsers( ResultSet rs) throws SQLException, ClassNotFoundException{
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

        return users;
    }

    /**
     * return array of all users in the database
     */
    public static User[] getUsers() throws SQLException, ClassNotFoundException{
        String query = "SELECT * FROM user;";
        return constructUsers( constructResultSet( query ) );
    }

    /**
     * return a user based on "userID"
     */
    public static User getUser( int userIDToSearch) throws SQLException, ClassNotFoundException {
        String query =
                "SELECT *\n" +
                "FROM user\n" +
                "WHERE userID = " + userIDToSearch + " ;";
        return constructUsers( constructResultSet( query ) )[0]; // 0 index bcuz it will only return 1 specific user (userID is PRIMARY KEY)
    }
    /**
     * return a user based on "user name"
     */
    public static User getUser( String userNameToSearch) throws SQLException, ClassNotFoundException {
        String query =
                "SELECT *\n" +
                "FROM user\n" +
                "WHERE name = \"" + userNameToSearch + "\" ;";
        return constructUsers( constructResultSet( query ) )[0]; // 0 index bcuz it will only return 1 specific user (userID is PRIMARY KEY)
    }

    /**
     * insert a record of user into database
     */
    public static void insertNewUser(User u) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO user VALUES(?,?,?,?)";
        Connection con = getConnection();
        PreparedStatement pst  = con.prepareStatement(query);

        // userID, name, email, password
        pst.setInt(1, 0);// PRIMARY KEY (auto_increment)
        pst.setString(2, u.getName());
        pst.setString(3, u.getEmail());
        pst.setString(4, u.getPassword());
        pst.executeUpdate();

        pst.close();
        con.close();
    }
}
