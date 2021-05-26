package classes;
import java.sql.Timestamp;

public class Issue {
    private int issueID;
    private int projectID;///
    private User creator; ///
    private User assignee;///
    private String title;
    private String description; // allowed to change
    private Timestamp time;
    private String tag;         // allowed to change
    private int priority;       // allowed to change
    private String status;      // allowed to change
    private Comment[] comments;

    public Issue(int issueID, int projectID, User creator, User assignee, String title, String description, Timestamp time, String tag, int priority, String status, Comment[] comments) {
        this.issueID = issueID;
        this.projectID = projectID;
        this.creator = creator;
        this.assignee = assignee;
        this.title = title;
        this.description = description;
        this.time = time;
        this.tag = tag;
        this.priority = priority;
        this.status = status;
        this.comments = comments;
    }

    public int getIssueID() {
        return issueID;
    }

    public int getProjectID() {
        return projectID;
    }

    public User getCreator() {
        return creator;
    }

    public User getAssignee() {
        return assignee;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getTag() {
        return tag;
    }

    public int getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public Comment[] getComments() {
        return comments;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }
}
