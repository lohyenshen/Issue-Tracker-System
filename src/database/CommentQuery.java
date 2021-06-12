package database;

import classes.*;
import java.sql.*;

public class CommentQuery extends Query{
    /**
     * return an array of comments based on ResultSet
     */
    private static Comment[] constructComments( ResultSet rs) throws SQLException, ClassNotFoundException {
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
            Reactions reaction  = ReactionQuery.getReactions( commentID );
            boolean hasPicture  = rs.getBoolean("hasPicture");

            comments[i] = new Comment(commentID, issueID, commentUser, time, description, reaction, hasPicture);
        }
        return comments;
     }

    /**
     * return an array of all comments that belong to a issue (based on issueIDToSearch)
     */
    public static Comment[] getComments(int issueIDToSearch) throws SQLException, ClassNotFoundException {
        String query =
                "SELECT *\n" +
                "FROM comment\n" +
                "WHERE issueID = " + issueIDToSearch +"\n" +
                "ORDER BY time ASC ; ";
        return constructComments( constructResultSet(query) );
    }

    /**
     * insert a new record of comment into database
     */
    public static void insertNewComment( Comment c )throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO comment VALUES(?,?,?,?,?)";

        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(query);

        // commentID, issueID, userID, time, description, reactions
        pst.setInt(1, 0); // PRIMARY KEY (auto_increment)
        pst.setInt(2, c.getIssueID());
        pst.setInt(3, c.getCommentUser().getUserID());
        pst.setTimestamp(4, c.getTime());
        pst.setString(5, c.getDescription());
        pst.executeUpdate();

        pst.close();
        con.close();
    }

    /**
     * update the the reactions in comment table (based on commentIDToSearch)
     */
    public static void updateComment( int commentIDToSearch, String newReactions) throws SQLException, ClassNotFoundException {
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

    /**
     * update the "description", "time" of a comment in database
     *
     * "description" - the new description which user entered
     * "time"        - the time when a user make changes
     */
    public static void updateCommentDescription( int commentID, String newDescription) throws SQLException, ClassNotFoundException {
        // insert the old record into "comment_change_log" table
        insert_Into_Comment_Change_Log( commentID );

        // update the details of the comment
        String query =
                        "UPDATE comment " +
                        "SET description = ?, " +
                        "    time = ? " +
                        "WHERE commentID = ?;";
        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, newDescription);
        pst.setTimestamp(2, new Timestamp(new Date(System.currentTimeMillis()).getTime()));
        pst.setInt(3, commentID);
        pst.executeUpdate();

        pst.close();
        con.close();
    }



    /**
     * insert a new record of comment change log once user change the description
     */
    private static void insert_Into_Comment_Change_Log( int commentID) throws SQLException, ClassNotFoundException {
        String query =
                        "INSERT INTO comment_change_log " +
                        "SELECT c.* " +
                        "FROM comment c " +
                        "WHERE commentID = "+commentID+" ;";
        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.executeUpdate();
        pst.close();
        con.close();
    }

    /**
     * return an array of "previousComments" based on commentID
     *
     * previousComments
     * 1) once the creator changes the description of an comment
     * 2) LATEST  comment's information is stored in (comment table)
     * 3) CURRENT comment's information is stored in (comment_change_log table)
     */
    public static Comment[] getPreviousComments(int commentID) throws SQLException, ClassNotFoundException {
        String query =
                        "SELECT * " +
                        "FROM comment_change_log " +
                        "WHERE commentID = "+commentID+" " +
                        "ORDER BY time DESC ;";
        return constructComments( constructResultSet(query) );
    }

    public static int getLastID() throws SQLException, ClassNotFoundException {
        String query =
                "SELECT commentID \n" +
                        "FROM comment\n" +
                        "ORDER BY commentID DESC;";
        ResultSet rs = constructResultSet( query );
        rs.next();
        return rs.getInt("commentID");
    }
}
