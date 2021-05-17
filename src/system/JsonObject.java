package system;

public class JsonObject {
    private Project[] projects;
    private User[] users;
    public JsonObject( Project[] projects, User[] users){
        this.projects = projects;
        this.users = users;
    }
}
