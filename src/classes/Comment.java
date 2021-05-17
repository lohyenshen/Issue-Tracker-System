package classes;

import java.sql.Timestamp;

public class Comment {
    private int commentID;
    private int issueID;///
    private User commentUser;///
    private Timestamp time;
    private String description;
    private Reactions reactions;


    public Comment(int commentID, int issueID, User commentUser, Timestamp time, String description, Reactions reactions) {
        this.commentID = commentID;
        this.issueID = issueID;
        this.commentUser = commentUser;
        this.time = time;
        this.description = description;
        this.reactions = reactions;
    }



    public int getCommentID() {
        return commentID;
    }

    public int getIssueID() {
        return issueID;
    }

    public User getCommentUser() {
        return commentUser;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public Reactions getReactions() {
        return reactions;
    }

    public void setIssueID(int issueID) {
        this.issueID = issueID;
    }

    public void setCommentUser(User commentUser) {
        this.commentUser = commentUser;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Created on: " + time.toString() + "   By: " + commentUser.getName() + "\n");
        sb.append( description );
        sb.append("$$" + reactions.toString());
        sb.append("\n");
        return sb.toString();
    }
}
