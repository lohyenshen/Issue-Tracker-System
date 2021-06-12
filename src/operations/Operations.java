package operations;

import com.google.gson.*;
import database.*;
import classes.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class Operations {
    protected static Scanner sc = new Scanner(System.in);
    private static String path_To_R_Script_Exe;

    protected static String opr;

    protected static User currentUser;

    protected static Project[] projects;
    protected static int selected_Project_ID;
    protected static Project currentProject;

    protected static Issue[]   issues;
    protected static int selected_Issue_ID;
    protected static Issue currentIssue;

    /**
     * verifies a user's login by checking (credentials entered) with (credentials extracted from database)
     */
    protected static boolean loginSuccessfully() throws SQLException, ClassNotFoundException {
        boolean userExist = false;
        currentUser = null;       // reset currentUser to null whenever a new login is called

        System.out.print("Enter email : ");
        String emailEntered = sc.nextLine();
        System.out.print("Enter password: ");
        String passwordEntered = sc.nextLine();

        User[] users = UserQuery.getUsers();
        for (User user : users) {
            if (emailEntered.equals(user.getEmail())) {
                if (passwordEntered.equals(user.getPassword())) {
                    currentUser = new User( user.getUserID(), user.getName(), user.getEmail(), user.getPassword());
                    userExist = true;
                    break;
                }
            }
        }

        if (userExist) {
            System.out.println("-----You are now signed in ------");
            System.out.printf ("-----Welcome back %s !      -----\n", currentUser.getName());
        }
        else
            System.out.println("Wrong credentials entered, please retry to Login");

        return userExist;
    }

    /**
     * this method registers a new user
     *
     * - insert a new user into database if user registered successfully
     */
    protected static void register() throws SQLException, ClassNotFoundException {
        // obtain required details of a new user
        System.out.println("\nCreating new user account..... Please enter your credentials as below\n");
        User uniqueUser = createUniqueUser();

        if (uniqueUser == null){
            System.out.println("Account creation is unsuccessful, Please retry");
        }
        else{
            System.out.println("Account created successfully, Please proceed to login");
            UserQuery.insertNewUser(uniqueUser);
        }
    }

    /**
     * this method ensures all new users are unique (email) is not duplicated
     * by comparing (entered details) with (details extracted from database).
     *
     * user will need to enter (email, password, name)
     */
    private static User createUniqueUser() throws SQLException, ClassNotFoundException {
        // extract all registered user from database
        User[] users = UserQuery.getUsers();

        // email
        String emailEntered;
        boolean isUniqueEmail;
        do {
            isUniqueEmail = true;
            System.out.print("Enter email: ");
            emailEntered = sc.nextLine();

            // check if email entered is in valid email format
            if (!emailEntered.matches("^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@" +"(?:[a-zA-Z0-9-]+\\.)+[a-z" +"A-Z]{2,7}$")){
                System.out.println("INVALID EMAIL ! ");
                isUniqueEmail = false;
                continue;
            }
            // check whether has the entered email been registered before (ensure no duplicated email)
            for (User user : users) {
                if (emailEntered.equals(user.getEmail())) {
                    System.out.println("THIS EMAIL IS REGISTERED BEFORE! ");
                    isUniqueEmail = false;
                    break;
                }
            }
        } while (!isUniqueEmail) ;


        // password
        String password;
        do{
            System.out.print("Enter password: ");
            password = sc.nextLine();

            if (password.isEmpty() || password.isBlank())
                System.out.println("password cannot be EMPTY OR BLANK !");
            else
                break;
        } while (true);

        String confirmPassword;
        do {
            System.out.print("Re-enter password to confirm: ");
            confirmPassword = sc.nextLine();

            if (!confirmPassword.equals(password))
                System.out.println("PASSWORDS DO NOT MATCH !");
            else
                break;
        } while (true);


        // name
        String name;
        do{
            System.out.print("Enter name: ");
            name = sc.nextLine();

            if (name.isEmpty() || name.isBlank())
                System.out.println("name cannot be EMPTY OR BLANK !");
            else
                break;
        } while (true);


        // userID's ACTUAL VALUE will be handled by MySQL auto increment feature
        return new User(0, name, emailEntered, password);
    }

    /**
     * allow user to choose whether to sort by ASC or DESC
     *
     *      true  - ASC
     *      false - DESC
     */
    private static boolean ASC_or_DESC(){
        do{
            System.out.print("Enter\n'A' to sort in ASCENDING order\n'D' to sort in DESCENDING order: ");
            char order = sc.nextLine().charAt(0);

            if (order == 'A')
                return true;
            else if (order == 'D')
                return false;
            else
                System.out.println("Invalid order entered");
        } while(true);
    }



    /**
     * initialize 'projects' from database without any sorting
     */
    protected static void initialize_Projects_Unsorted() throws SQLException, ClassNotFoundException {
        projects = ProjectQuery.getProjects_Unsorted();
    }

    /**
     * allow user to sort projects either by "Alphanumeric" or "ProjectID"
     */
    protected static void initialize_Projects_SortedBy_Something() throws SQLException, ClassNotFoundException {
        do{
            System.out.print("Sort By?\nAlphanumeric(a) OR ProjectID(i): ");
            char sortBy = sc.nextLine().charAt(0);

            if (sortBy == 'a'){
                initialize_Projects_SortedBy_Alphanumeric();
                return;
            }
            else if (sortBy == 'i'){
                initialize_Projects_SortedBy_projectID();
                return;
            }
            else
                System.out.println("Invalid sorting variable entered");
        } while(true);
    }

    /**
     * initialize 'projects' from database sorted by "ALPHANUMERIC"
     */
    private static void initialize_Projects_SortedBy_Alphanumeric() throws SQLException, ClassNotFoundException {
        projects   = ProjectQuery.getProjects_SortedBy_Alphanumeric( ASC_or_DESC() );
    }

    /**
     * initialize 'projects' from database sorted by "projectID"
     */
    private static void initialize_Projects_SortedBy_projectID() throws SQLException, ClassNotFoundException {
        projects   = ProjectQuery.getProjects_SortedBy_projectID( ASC_or_DESC() );
    }
    /**
     * initialize 'projects' from database searched by "project name"
     */
    protected static void initialize_Projects_SearchBy_ProjectName() throws SQLException, ClassNotFoundException{
        projects   = ProjectQuery.getProjects_SearchBy_ProjectName( projectNameToSearch() );
    }

    /**
     * return a String of project name to search
     */
    private static String projectNameToSearch() {
        System.out.print("Enter name of project: ");
        do {
            String projectName = sc.nextLine();
            if (projectName.isBlank() || projectName.isEmpty())
                System.out.println("Project Name cannot be BLANK or EMPTY");
            else
                return projectName;
        } while (true);
    }


    /**
     * PRECONDITION - must call ANY 'initialize_Projects' method before displaying
     * display all projects
     */
    protected static void display_Project_Dashboard(){
        String title = "Project Dashboard";
        int[] lengths = {3,113,10};
        StringBuilder sb = new StringBuilder("\n\n\n");

        sb.append("-".repeat(title.length())).append("\n");
        sb.append(title).append("\n");
        sb.append("-".repeat(title.length())).append("\n");

        if (projects.length == 0){
            sb.append("No Project\n");
        }
        else{
            line(sb, lengths);
            sb.append( String.format("|%3s|%113s|%10s|\n", "ID", "Project Name", "Issues") );
            line(sb, lengths);

            for (Project p : projects){
                sb.append( String.format("|%3d|%113s|%10d|\n", p.getProjectID(), p.getName(), p.getIssues().length) );
                line(sb, lengths);
            }
        }
        System.out.println(sb.toString());
    }
    /**
     * verify whether 'selectedProjectID' is valid
     */
    protected static boolean validProjectID(int projectIDToVerify) {
        for (Project p : projects)
            if (p.getProjectID() == projectIDToVerify)
                return true;
        return false;
    }











    /**
     * retrieves all issues related to a project (based on projectIndex)
     */
    protected static void initialise_Issues_Unsorted() throws SQLException, ClassNotFoundException {
        currentProject  = ProjectQuery.getProject( selected_Project_ID );
        issues          = IssueQuery.getIssues( selected_Project_ID );
    }

    //////////////////////////SORT//////////////////////////////////////////////////////////////////////////////////////
    /**
     * allow user to choose to (SORT) issues either by (priority) / (time)
     */
    protected static void initialise_Issues_SortedBy_Something() throws SQLException, ClassNotFoundException {
        do{
            System.out.print("Sort By?\npriority(p) OR time(t): ");
            char sortBy = sc.nextLine().charAt(0);

            if (sortBy == 'p'){
                initialise_Issues_SortedBy_Priority();
                return;
            }
            else if (sortBy == 't'){
                initialise_Issues_SortedBy_Time();
                return;
            }
            else
                System.out.println("Invalid sorting variable entered");
        } while(true);
    }

    /**
     * initialize 'issues' from database sorted by "priority"
     */
    private static void initialise_Issues_SortedBy_Priority() throws SQLException, ClassNotFoundException {
        issues     = IssueQuery.getIssues_SortedBy_Priority( selected_Project_ID , ASC_or_DESC() );
    }

    /**
     * initialize 'issues' from database sorted by "time"
     */
    private static void initialise_Issues_SortedBy_Time() throws SQLException, ClassNotFoundException{
        issues     = IssueQuery.getIssues_SortedBy_Time( selected_Project_ID , ASC_or_DESC() );
    }



    //////////////////////////FILTER//////////////////////////////////////////////////////////////////////////////////////
    /**
     * allow user to choose to (FILTER) issues either by (status) / (tag)
     */
    protected static void  initialise_Issues_FilterBy_Something() throws SQLException, ClassNotFoundException{
        do{
            System.out.print("Filter By?\nstatus(s) OR tag(t): ");
            char sortBy = sc.nextLine().charAt(0);

            if (sortBy == 's'){
                initialise_Issues_FilterBy_Status();
                return;
            }
            else if (sortBy == 't'){
                initialise_Issues_FilterBy_Tag();
                return;
            }
            else
                System.out.println("Invalid filter variable entered");
        } while(true);
    }

    /**
     * allow user to enter a status to filter by
     */
    private static String statusToFilter() {
        System.out.print("Enter status you want to filter by: ");
        do {
            String status = sc.nextLine();
            if (status.isBlank() || status.isEmpty())
                System.out.println("status cannot be BLANK or EMPTY");
            else
                return status;
        } while (true);
    }
    /**
     * allow user to enter a tag to filter by
     */
    private static String tagToFilter() {
        System.out.print("Enter tag you want to filter by: ");
        do {
            String tag = sc.nextLine();
            if (tag.isBlank() || tag.isEmpty())
                System.out.println("Tag cannot be BLANK or EMPTY");
            else
                return tag;
        } while (true);
    }
    /**
     * initialize 'issues' from database filtered by "status"
     */
    private static void initialise_Issues_FilterBy_Status() throws SQLException, ClassNotFoundException{
        issues     = IssueQuery.getIssues_FilterBy_Status( selected_Project_ID , statusToFilter() );
    }
    /**
     * initialize 'issues' from database filtered by "tag"
     */
    private static void initialise_Issues_FilterBy_Tag() throws SQLException, ClassNotFoundException{
        issues     = IssueQuery.getIssues_FilterBy_Tag( selected_Project_ID , tagToFilter() );
    }

    /////////////////////////////////SEARCH/////////////////////////////////////////////////////////////////////////////

    /**
     * initialse 'issues' from database where (title / description / comments) matches keyword entered by user
     */
    protected static void initialize_Issues_SearchBy_Key_Word() throws SQLException, ClassNotFoundException {
        issues = IssueQuery.getIssues_SearchBy_Key_Word( selected_Project_ID, keyWord());
    }
    private static String keyWord(){
        do{
            System.out.println("Enter any word to search");
            String keyWord = sc.nextLine();
            if (keyWord.isEmpty() || keyWord.isBlank())
                System.out.println("Word to Search for cannot be BLANK or EMPTY");
            else
                return keyWord;
        } while (true);
    }

    ///////////////////////CREATE/////////////////////////////////////////

    /**
     * allow currentUser to create new issue by specifying the details listed as below
     *
     * - title
     * - description
     * - tag
     * - priority
     * - assignee
     *
     * MySQL
     * issueID, projectID, creatorID, assigneeID, title, description, time, tag, priority, status
     */
    protected static void create_New_Issue() throws SQLException, ClassNotFoundException {
        System.out.println("\nProceeding to create new issue, please enter details of this issue as listed below");
        int issueID   = 0; // actual issueID will be handled by MySQL auto increment feature
        int projectID = selected_Project_ID;
        User creator  = currentUser;
        User assignee = UserQuery.getUser( inputAssigneeID() );
        String title = inputTitle();
        String description = inputIssueDescription();
        Timestamp time = new Timestamp(new Date(System.currentTimeMillis()).getTime());
        String tag    = inputTag();
        int priority = inputPriority();
        String status = "Open";

        Issue newIssue = new Issue( issueID, projectID, creator, assignee, title, description, time, tag, priority, status, null);
        IssueQuery.insertNewIssue( newIssue );

        initialise_Issues_Unsorted();// need to update "issues" once we have inserted a new issue
    }



    /**
     * prompt user to enter a name of assignee,
     * return the id of assignee
     */
    private static int inputAssigneeID() throws SQLException, ClassNotFoundException {
        do{
            System.out.print("Enter name of assignee: ");
            String nameEntered = sc.nextLine();

            User[] users = UserQuery.getUsers();
            for (User user : users)
                if (user.getName().equals(nameEntered))
                    return user.getUserID();

            System.out.println("Assignee does not exists!");
        } while (true);
    }

    /**
     * prompt user to enter a "title"
     */
    private static String inputTitle() {
        do{
            System.out.print("Enter title: ");
            String title = sc.nextLine();

            if (title.isEmpty() || title.isBlank())
                System.out.println("Title cannot be BLANK or EMPTY");
            else
                return title;
        } while (true);
    }
    /**
     * prompt user to enter a description
     */
    private static String inputIssueDescription() {
        StringBuilder sb = new StringBuilder();
        System.out.println("Enter description\nEnter 'e' to stop: ");
        do{
            String s = sc.nextLine();
            if (s.equals("e"))
                return sb.toString();
            sb.append(s).append("\n");
        } while (true);
    }

    /**
     * prompt user to enter a tag
     *
     * tag can be null
     */
    private static String inputTag() {
        System.out.print("Enter tag if available: ");
        return sc.nextLine();
    }
    /**
     * prompt user to enter a tag
     */
    private static int inputPriority() {
        do{
            System.out.print("Enter priority: ");
            String s = sc.nextLine();

            if (isNumber(s)){
                int n = Integer.parseInt(s);
                if (n>=1 && n<=9)
                    return n;
                else
                    System.out.println("Priority should be within [1,9]");
            }
            else
                System.out.println("Please enter an integer!");
        } while (true);
    }


    /**
     * PRECONDITION - must call ANY 'initialize_Issues' method before displaying
     * display all issues related to a project
     */
    protected static void display_Issue_Dashboard() {
        String title = "Issue Dashboard";
        int[] lengths = {3, 38, 15, 15, 10, 25, 10, 10};
        StringBuilder sb = new StringBuilder("\n\n\n");

        String projectTitle =  "Project Title = " + currentProject.getName();
        sb.append("-".repeat(projectTitle.length())).append("\n");
        sb.append(projectTitle).append("\n");
        sb.append("-".repeat(projectTitle.length())).append("\n");

        sb.append("-".repeat(title.length())).append("\n");
        sb.append(title).append("\n");
        sb.append("-".repeat(title.length())).append("\n");

        if (issues.length == 0){
            sb.append("No Issues\n");
        }
        else{
            line(sb, lengths);
            sb.append( String.format("|%3s|%38s|%15s|%15s|%10s|%25s|%10s|%10s|\n", "ID", "Title", "Status", "Tag", "Priority", "Time", "Assignee", "Created By") );
            line(sb, lengths);

            for (Issue i : issues){
                sb.append( String.format("|%3d|%38s|%15s|%15s|%10d|%25s|%10s|%10s|\n", i.getIssueID(), i.getTitle(), i.getStatus(), i.getTag(), i.getPriority(), i.getTime().toString(), i.getAssignee().getName(), i.getCreator().getName()));
                line(sb, lengths);
            }
        }
        System.out.println(sb.toString());
    }

    /**
     * determine whether issueID entered is valid
     */
    protected static boolean validIssueID(int issueIDToVerify) {
        for (Issue i : issues)
            if (i.getIssueID() == issueIDToVerify)
                return true;
        return false;
    }

    /**
     * display the details of currentIssue
     */
    protected static void displayCurrentIssue() {
        StringBuilder sb = new StringBuilder("\n\n\n");
        String issueName = "Issue Title = " + currentIssue.getTitle();
        sb.append("-".repeat(issueName.length())).append("\n");
        sb.append(issueName).append("\n");
        sb.append("-".repeat(issueName.length())).append("\n");


        String issuePage         = "Issue Page";
        sb.append("-".repeat(issuePage.length())).append("\n");
        sb.append(issuePage).append("\n");
        sb.append("-".repeat(issuePage.length())).append("\n");

        sb.append(String.format("%-20s%-50s\n", "Title:"         ,currentIssue.getTitle()));
        sb.append(String.format("%-20s%-50s\n", "Issue ID:"      , currentIssue.getIssueID()));
        sb.append(String.format("%-20s%-50s\n", "Status:"        , currentIssue.getStatus()));
        sb.append(String.format("%-20s%-50s\n", "Tag:"           , currentIssue.getTag()));
        sb.append(String.format("%-20s%-50d\n","Priority:"       , currentIssue.getPriority()));
        sb.append(String.format("%-20s%-50s\n", "Created on:"    , currentIssue.getTime().toString()));
        sb.append(String.format("%-20s%-50s\n", "Created by:"    , currentIssue.getCreator().getName()));
        sb.append(String.format("%-20s%-50s\n", "Assigned to:"   , currentIssue.getAssignee().getName()));
        sb.append("\n");


        String issueDescription  = "Issue Description";
        sb.append("-".repeat(issueDescription.length())).append("\n");
        sb.append(issueDescription).append("\n");
        sb.append("-".repeat(issueDescription.length())).append("\n");
        sb.append( currentIssue.getDescription() );
        sb.append("\n\n");

        String issueComments     = "Comments";
        sb.append("-".repeat(issueComments.length())).append("\n");
        sb.append(issueComments).append("\n");
        sb.append("-".repeat(issueComments.length())).append("\n");
        Comment[] comments = currentIssue.getComments();
        for (int i = 0; i < comments.length; i++)
            sb.append( String.format("#%d      %s", (i+1), comments[i].toString()) );


        System.out.println( sb.toString() );
    }
    //////////COMMENT//////////////////////

    /**
     * insert a new comment into database
     */
    protected static void comment_On_Issue() throws SQLException, ClassNotFoundException {
        Comment newComment = new Comment( 
                0,  
                currentIssue.getIssueID(), 
                currentUser,  
                new Timestamp(new Date(System.currentTimeMillis()).getTime()), 
                inputCommentDescription(), 
                new Reactions(0,0,0,0,0,0), 
                hasPictureOrNot());
        CommentQuery.insertNewComment( newComment );

        update_Static_Variables();
    }

    private static boolean hasPictureOrNot() {
    }

    /**
     * allow user to enter description for a comment
     */
    private static String inputCommentDescription() {
        StringBuilder sb = new StringBuilder();
        System.out.println("Enter comment\nEnter 'e' to stop: ");
        do{
            String s = sc.nextLine();
            if (s.equals("e"))
                return sb.toString();
            sb.append(s).append("\n");
        } while (true);
    }

    ///////////////REACT//////////////////////////

    /**
     * allow user to
     *      - choose which comment to react on
     *      - which reaction to use
     *      - update the changes in database
     */
    protected static void react_On_Comment() throws SQLException, ClassNotFoundException {
        int commentIndex              = inputCommentIndex();
        Comment currentComment        = currentIssue.getComments()[commentIndex];
        ReactionQuery.updateReaction( currentUser.getUserID(), currentComment.getCommentID(), inputReaction());

        update_Static_Variables();
    }

    /**
     * allow user to choose which reaction to add to a existing comment
     */
    private static String inputReaction() {
        List<String> reactions = Arrays.asList("like",    "love",    "haha",    "wow",    "sad",    "angry");
        do{
            System.out.print("Enter (like, love, haha, wow, sad, angry): ");
            String reaction = sc.nextLine();
            if (reactions.contains(reaction))
                return reaction;
            else
                System.out.println("Please enter a valid reaction! ");
        } while (true);
    }

    /**
     * allow user to choose which comment to react on
     */
    private static int inputCommentIndex(){
        int commentIndex;
        do{
            System.out.print("Enter comment no: ");
            opr = sc.nextLine();
            if (isNumber(opr)){
                commentIndex = Integer.parseInt(opr)-1;
                if (commentIndex>=0 && commentIndex<currentIssue.getComments().length)
                    return commentIndex;
                else
                    System.out.println("Please enter a valid comment no.");
            }
            else
                System.out.println("Please enter a number");
        } while(true);
    }

    /**
     * for console output display only
     */
    private static void line( StringBuilder sb, int... lengths){
        sb.append("+");
        for (int length : lengths)
            sb.append("-".repeat(length)).append("+");
        sb.append("\n");
    }

    /**
     * allow user to either (import) or (export) JSON object
     */
    protected static void JSON_import_export() throws SQLException, ClassNotFoundException {
        System.out.println("JSON objects");
        do{
            System.out.print("Import(i) or Export(e): ");
            opr = sc.nextLine();

            switch (opr){
                case "i" -> {
                    importJson();
                    projects = ProjectQuery.getProjects_Unsorted(); // update projects once finished importing new data
                    return;
                }
                case "e" -> {
                    exportJson();
                    return;
                }
                default  -> System.out.println("Invalid operation");
            }
        } while (true);
    }

    /**
     *
     */
    protected static void generate_Report() throws IOException {
        deleteAllFiles();

        String R_Script_Exe = getPathTo_R_Script_Exe();
        String R_code       = System.getProperty("user.dir") + "\\Rscript\\report_generation_code.R";

        ProcessBuilder pb = new ProcessBuilder(R_Script_Exe, "", R_code);
        pb.start();

        System.out.println("REPORTS GENERATED SUCCESSFULLY");
        System.exit(0);
    }
    private static void deleteAllFiles() {
        File dir = new File( System.getProperty("user.dir") + "\\Report Generation");
        File[] files = dir.listFiles();

        if (files == null)
            return;
        for (File file : files)
            file.delete();
    }
    protected static String getPathTo_R_Script_Exe() throws IOException {
        if (path_To_R_Script_Exe != null)
            return path_To_R_Script_Exe;


        File dir = new File( "C:\\Program Files" );
        find_R_Script_Exe( dir.listFiles() );
        if (path_To_R_Script_Exe == null)
            throw new IOException("Could Not find path to Rscript.exe");
        return path_To_R_Script_Exe;
    }
    private static void find_R_Script_Exe( File[] listFiles) {
        if (listFiles == null)  return;

        for (File f : listFiles){
            if (f.isDirectory())
                find_R_Script_Exe( f.listFiles() );

            else {
                if (f.getAbsolutePath().endsWith("bin\\Rscript.exe")){
                    path_To_R_Script_Exe = f.getAbsolutePath();
                    return;
                }
            }
        }
    }

    /**
     * allow user to import json file & all data in json file will be inserted into database
     */
    private static void importJson() throws SQLException, ClassNotFoundException {
        // validate filePath
        File pathToJsonFile = pathToJsonFile();
        if (pathToJsonFile == null){
            System.out.println("Unable to import JSON file");
            return;
        }

        // validate jsonString
        String jsonString = jsonString( pathToJsonFile );
        if (jsonString == null){
            System.out.println("Unable to import JSON file");
            return;
        }

        // instantiate 'projects_and_users' from jsonString
        Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        Projects_And_Users projects_and_users = g.fromJson( jsonString, Projects_And_Users.class);

        // insert "projects_and_users" into database
        projects_and_users.insertIntoDatabase();

        System.out.println("Imported JSON file into database successfully");
    }

    /**
     * read the content of json file as String and return it
     */
    private static String jsonString( File pathToJsonFile) {
        Path p = Path.of(pathToJsonFile.getAbsolutePath());
        try{
            String jsonString = Files.readString(p);
            return jsonString;
        }
        catch (IOException e){
            System.out.println( e );
            return null;
        }
    }

    /**
     * return the path where the (json file to be imported) is located
     */
    private static File pathToJsonFile() {
        JFileChooser f = new JFileChooser();
        f.setCurrentDirectory( new File(System.getProperty("user.dir")) );
        FileFilter filter = new FileNameExtensionFilter("JSON", "JSON");
        f.setFileFilter(filter);
        f.setAcceptAllFileFilterUsed(false);
        f.setDialogTitle("Choose JSON file to IMPORT");
        f.setApproveButtonText("Import this");

        int result = f.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION)
            return f.getSelectedFile();
        else
            return null;
    }

    /**
     * allow user to export all projects as .json file
     */
    private static void exportJson() {
        File pathSelected = pathToExportJsonFile();

        if (pathSelected == null){
            System.out.println("Unable to export JSON file");
            return;
        }

        try (Writer writer = new FileWriter(pathSelected + "\\data.json")) {
            Projects_And_Users projects_and_users = new Projects_And_Users(  ProjectQuery.getProjects_SortedBy_projectID(true), UserQuery.getUsers());

            Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
            g.toJson(projects_and_users, writer);
            System.out.println("Exported JSON file successfully");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * return the (path to directory) where json file is exported
     */
    private static File pathToExportJsonFile() {
        JFileChooser f = new JFileChooser();
        f.setCurrentDirectory( new File(System.getProperty("user.dir")) );
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        f.setDialogTitle("Choose directory to EXPORT JSON file");
        f.setApproveButtonText("Export Here");

        int result = f.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION)
            return f.getSelectedFile();
        else
            return null;
    }

    /**
     * determine whether a String input is a number
     */
    protected static boolean isNumber(String input) {
        try{
            Integer.parseInt(input);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * - allow either "creator" or "assignee" to change the (status) of an issue
     * - reject operation when "currentUser" neither of them
     *
     * "creator"  -> OPEN,              RESOLVED, CLOSED
     * "assignee" -> OPEN, IN PROGRESS, RESOLVED, CLOSED
     */
    protected static void change_Status() throws SQLException, ClassNotFoundException {
        int currentUserID = currentUser.getUserID();
        int creatorID     = currentIssue.getCreator().getUserID();
        int assigneeID    = currentIssue.getAssignee().getUserID();


        if (currentUserID == creatorID)
            IssueQuery.updateStatus( currentIssue.getIssueID(), Open_Resolved_Closed());

        else if (currentUserID == assigneeID)
            IssueQuery.updateStatus( currentIssue.getIssueID(), Open_InProgress_Resolved_Closed());

        else
            System.out.println("You are not allowed to CHANGE the STATUS of this issue!");

        update_Static_Variables();
    }

    /**
     * allows only the (issue creator) to change the description of "currentIssue"
     */
    protected static void change_Issue_Description() throws SQLException, ClassNotFoundException {
        int currentUserID = currentUser.getUserID();
        int creatorID     = currentIssue.getCreator().getUserID();

        if (currentUserID == creatorID)                              // new issue description
            IssueQuery.updateDescription( currentIssue.getIssueID(), inputIssueDescription());
        else
            System.out.println("You are not allowed to CHANGE the DESCRIPTION of this issue!");

        update_Static_Variables();
    }

    /**
     * obtain & prints the change log of "currentIssue"'s description
     *
     * for fun, depends on GUI implementation
     */
    protected static void issue_Description_Change_Log() throws SQLException, ClassNotFoundException {
        Issue[] previousIssues = IssueQuery.getPreviousIssues( selected_Issue_ID );

        System.out.println("\n\n\nIssue Description Change Log");
        if (previousIssues.length == 0)
            System.out.println("No changes made yet");

        for (int i = 0; i < previousIssues.length; i++)
            System.out.printf( "%d) Changes made on (%s)\n%s\n\n", (i+1), previousIssues[i].getTime().toString(), previousIssues[i].getDescription()  );
    }

    /**
     * allows only the (commenter) to change the description of his/her comment
     */
    protected static void change_Comment() throws SQLException, ClassNotFoundException {
        int currentUserID       = currentUser.getUserID();
        int commentIndex        = inputCommentIndex();
        Comment selectedComment = currentIssue.getComments()[commentIndex];
        int commenterID         = selectedComment.getCommentUser().getUserID();


        if (currentUserID == commenterID)                                         // new comment description
            CommentQuery.updateCommentDescription( selectedComment.getCommentID(), inputCommentDescription());
        else
            System.out.println("You are not allowed to CHANGE this COMMENT!");

        update_Static_Variables();
    }

    /**
     * obtain & prints the change log of selected comment
     *
     * for fun, depends on GUI implementation
     */
    protected static void comment_Change_Log() throws SQLException, ClassNotFoundException {
        int ii = inputCommentIndex();
        Comment selectedComment = currentIssue.getComments()[ ii ];

        Comment[] previousComments = CommentQuery.getPreviousComments( selectedComment.getCommentID() );

        System.out.println("\n\n\nComment ("+(ii+1)+") Change Log");
        if (previousComments.length == 0)
            System.out.println("No changes made yet");

        for (int i = 0; i < previousComments.length; i++)
            System.out.printf( "%d) Changes made on (%s)\n%s\n\n", (i+1), previousComments[i].getTime().toString(), previousComments[i].getDescription()  );
    }

    //// maybe we can design a method to check the ownership????



    /**
     * return "Open" or "Resolved" or "Closed"
     */
    private static String Open_Resolved_Closed(){
        do{
            System.out.print("Enter new status of this issue\nOpen(o)   Resolved(r)   Closed(c): ");
            opr = sc.nextLine();

            switch (opr){
                case "o" -> {return "Open";}
                case "r" -> {return "Resolved";}
                case "c" -> {return "Closed";}
                default  -> System.out.println("Invalid Status");
            }
        } while (true);
    }

    /**
     * return "Open" or "In Progress" or "Resolved" or "Closed"
     */
    private static String Open_InProgress_Resolved_Closed(){
        do{
            System.out.print("Enter new status of this issue\nOpen(o)   In Progress(i)   Resolved(r)   Closed(c): ");
            opr = sc.nextLine();

            switch (opr){
                case "o" -> {return "Open";}
                case "i" -> {return "In Progress";}
                case "r" -> {return "Resolved";}
                case "c" -> {return "Closed";}
                default  -> System.out.println("Invalid Status");
            }
        } while (true);
    }
    protected static void update_Static_Variables() throws SQLException, ClassNotFoundException {
        projects = ProjectQuery.getProjects_Unsorted();
        currentProject = ProjectQuery.getProject( selected_Project_ID );
        issues = IssueQuery.getIssues( selected_Project_ID );
        currentIssue = IssueQuery.getIssue( selected_Issue_ID );
    }

    /**
     * allow user to upload a picture
     */
    protected static void uploadPicture() throws IOException, SQLException, ClassNotFoundException {
        String from = pathToPicture();
        String to   = System.getProperty("user.dir") + "\\Pictures\\" + CommentQuery.getLastID() + ".png";
        savePictureToDirectory( from, to );
    }


    /**
     * return the path where the (json file to be imported) is located
     */
    private static String pathToPicture() {
        JFileChooser f = new JFileChooser();
        f.setCurrentDirectory( new File(System.getProperty("user.dir")) );
        FileFilter filter = new FileNameExtensionFilter("png", "png");
        f.setFileFilter(filter);
        f.setAcceptAllFileFilterUsed(false);
        f.setDialogTitle("Choose picture to IMPORT");
        f.setApproveButtonText("Import this");

        int result = f.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION)
            return f.getSelectedFile().getAbsolutePath();
        else
            return null;
    }
    private static void savePictureToDirectory (String from, String to) throws IOException {
        generate_Picture_Folder_If_Not_Exits();


        final int BUFFERSIZE = 5 * 1024;
        FileInputStream fin   = new FileInputStream(new File(from));
        FileOutputStream fout = new FileOutputStream(new File(to));

        byte[] buffer = new byte[BUFFERSIZE];

        while (fin.available() != 0) {
            fin.read(buffer);
            fout.write(buffer);
        }
    }

    private static void generate_Picture_Folder_If_Not_Exits() {
        File report_directory = new File(System.getProperty("user.dir") + "\\Pictures");
        if (!report_directory.exists())
            report_directory.mkdirs();
    }
}
