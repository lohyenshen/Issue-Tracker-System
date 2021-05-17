package system;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Issue {
    private int issueID;
    private int projectID;
    private User creator;
    private User assignee;
    private String title;
    private String description;
    private Timestamp time;
    private String tag;
    private int priority;
    private String status;
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

    @Override
    public String toString() {
        return "Issue{" +
                "issueID=" + issueID +
                ", projectID=" + projectID +
                ", creator=" + creator +
                ", assignee=" + assignee +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", time=" + time +
                ", tag='" + tag + '\'' +
                ", priority=" + priority +
                ", status='" + status + '\'' +
                ", comments=" + Arrays.toString(comments) +
                '}';
    }
}
