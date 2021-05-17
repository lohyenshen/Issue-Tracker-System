package database;

import system.*;
import java.sql.*;

public class CommentQuery extends Query{

    /**
     * return an array of all comments that belong to a issue (based on issueIDToSearch)
     */
    public static Comment[] getComments(int issueIDToSearch) throws SQLException, ClassNotFoundException {
        String query =
                "SELECT *\n" +
                "FROM comment\n" +
                "WHERE issueID = " + issueIDToSearch +"\n" +
                "ORDER BY time ASC ; ";
        Connection con = getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery( query );

        int L = size( rs );
        Comment[] comments = new Comment[L];

        for (int i = 0; i < comments.length; i++) {
            rs.next();

            // commentID, issueID, userID, time, description, reaction
            int commentID       = rs.getInt("commentID");
            int issueID         = rs.getInt("issueID");
            User commentUser    = UserQuery.getUser( rs.getInt("userID") );
            Timestamp time      = rs.getTimestamp("time");
            String description  = rs.getString("description");
            Reactions reaction = new Reactions( rs.getString("reactions") );

            comments[i] = new Comment(commentID, issueID, commentUser, time, description, reaction);
        }

        st.close();
        con.close();
        return comments;
    }

    /**
     * insert a new record of comment into database
     */
    public static void insertNewComment(int commentID, int issueID, int userID, Timestamp time, String description) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO comment VALUES(?,?,?,?,?,?)";

        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, commentID);
        pst.setInt(2, issueID);
        pst.setInt(3, userID);
        pst.setTimestamp(4, time);
        pst.setString(5, description);
        pst.setString(6, "0 0 0 0 0 0");       // new comment has no reactions initially
        pst.executeUpdate();

        pst.close();
        con.close();
    }

    /**
     * update the the reactions in comment table (based on commentIDToSearch)
     */
    public static void updateComment(int commentIDToSearch, String newReactions) throws SQLException, ClassNotFoundException {
        String query =
                "UPDATE comment \n" +
                "SET reactions = \"" + newReactions + "\"\n" +
                "WHERE commentID = "+ commentIDToSearch + " ; ";

        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.executeUpdate();

        pst.close();
        con.close();
    }
}
