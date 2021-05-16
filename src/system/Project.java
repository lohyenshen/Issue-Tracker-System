package system;

import java.util.ArrayList;

public class Project {
    private int projectID;
    private String name;
    private Issue[] issues;

    public Project(int projectID, String name, Issue[] issues) {
        this.projectID = projectID;
        this.name = name;
        this.issues = issues;
    }

    public int getProjectID() {
        return projectID;
    }

    public String getName() {
        return name;
    }

    public Issue[] getIssues() {
        return issues;
    }
}
