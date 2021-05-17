package system;

/**
 * 'Projects_And_Users' is a class which comprises of 2 instance variables (Used sorely for GSON class to parse Json object)
 *
 * - projects
 * - users
 */
public class Projects_And_Users {
    private Project[] projects;
    private User[] users;


    public Projects_And_Users(Project[] projects, User[] users){
        this.projects = projects;
        this.users = users;
    }

    public void insertIntoDatabase() {

    }
}
