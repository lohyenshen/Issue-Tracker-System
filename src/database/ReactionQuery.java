package database;

import classes.Reactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReactionQuery extends Query{
    /**
     * return a array representing all reactions on a single comment
     */
    public static Reactions getReactions( int commentID) throws SQLException, ClassNotFoundException {
        String query =
                "SELECT \n" +
                        "    COALESCE(SUM(type = 'like'), 0) AS likeCount,\n" +
                        "    COALESCE(SUM(type = 'love'), 0) AS loveCount,\n" +
                        "    COALESCE(SUM(type = 'haha'), 0) AS hahaCount,\n" +
                        "    COALESCE(SUM(type = 'wow'), 0) AS wowCount,\n" +
                        "    COALESCE(SUM(type = 'sad'), 0) AS sadCount,\n" +
                        "    COALESCE(SUM(type = 'angry'), 0) AS angryCount \n" +
                "FROM reaction\n" +
                "WHERE commentID = " + commentID + " ; ";

        ResultSet rs = constructResultSet( query );
        rs.next();
        return new Reactions(
                                rs.getInt("likeCount"),
                                rs.getInt("loveCount"),
                                rs.getInt("hahaCount"),
                                rs.getInt("wowCount"),
                                rs.getInt("sadCount"),
                                rs.getInt("angryCount")
                            );
    }



    public static void updateReaction( int userID, int commentID, String reactionEntered) throws SQLException, ClassNotFoundException {
        if (reactedBefore( userID, commentID))

            // if user reacted before
            // if user choose the SAME reaction again -> we undo the reaction             ( like -> unlike)
            // if user choose DIFFERENT reaction      -> we change to the latest reaction ( like -> haha)
            if (sameTypeOfReaction( userID, commentID, reactionEntered))
                ReactionQuery.deleteReaction( userID, commentID);

            else
                ReactionQuery.changeReaction( userID, commentID, reactionEntered);

        // if user did not react before, insert new reaction
        else
            ReactionQuery.insertNewReaction( userID, commentID, reactionEntered);
    }




    /**
     * checks whether or not has (currentUser) reacted on (currentComment)
     */
    private static boolean reactedBefore(int userID, int commentID) throws SQLException, ClassNotFoundException {
        String query =
                "SELECT COUNT(*) AS freq\n" +
                "FROM reaction\n" +
                "WHERE userID = "+userID+" AND commentID = "+commentID+";";
        ResultSet rs = constructResultSet( query );
        rs.next();
        return rs.getInt("freq") >= 1;
    }

    /**
     * insert a new record of reaction into database
     */
    private static void insertNewReaction( int userID, int commentID, String reactionEntered) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO reaction VALUES(?,?,?)";

        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, userID);
        pst.setInt(2, commentID);
        pst.setString(3, reactionEntered);
        pst.executeUpdate();

        pst.close();
        con.close();
    }

    /**
     * checks whether or not "reactionEntered" == "previousReaction" stored in database
     */
    private static boolean sameTypeOfReaction(int userID, int commentID, String reactionEntered) throws SQLException, ClassNotFoundException {
        String query =
                "SELECT type\n" +
                "FROM reaction\n" +
                "WHERE userID = "+userID+" AND commentID = "+commentID+";";
        ResultSet rs = constructResultSet( query );
        rs.next();
        String previousReaction =  rs.getString("type");
        return reactionEntered.equals( previousReaction );
    }

    /**
     * deletes the reaction by (currentUser) on (currentComment)
     */
    private static void deleteReaction(int userID, int commentID) throws SQLException, ClassNotFoundException {
        String query =
                "DELETE FROM reaction\n" +
                "WHERE userID = "+userID+" AND commentID = "+commentID+";";

        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.executeUpdate();

        pst.close();
        con.close();
    }

    /**
     * update the reaction table in database with the latest reaction made by user
     */
    private static void changeReaction(int userID, int commentID, String reactionEntered) throws SQLException, ClassNotFoundException {
        String query =
                "UPDATE reaction\n" +
                "SET type = \'"+reactionEntered+"\'\n" +
                "WHERE userID = "+userID+" AND commentID = "+commentID+";";

        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(query);
        pst.executeUpdate();

        pst.close();
        con.close();
    }
}
