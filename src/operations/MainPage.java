package operations;
import database.*;

import java.io.IOException;
import java.sql.*;

public class MainPage extends Operations {

    /**
     * - allow user to register /login /exit program
     *
     * - after login
     *      - display project dashboard
     *
     * - after choosing project
     *      - display all issues related to project selected
     *
     *
     * - after choosing issue
     *      - display the details of issue
     *      - allow user to ADD comments
     *      - allow user to REACT to comments
     */

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        String fastLogin = "l\nloh@gmail.com\nLOH\n";
        register_and_login();
    }

    /**
     * allow user to either (register an account) / (login to registered account)
     */
    private static void register_and_login() throws SQLException, ClassNotFoundException, IOException {
        currentUser = null;
        do{
            System.out.println("\n" + "-".repeat(40) + "Welcome to Bugs Everywhere Sdn Bhd" + "-".repeat(40));
            System.out.print(
                            "Enter \n'l' to login" +
                            "\n'r' to register new account\n" +
                            "or 'e' to exit: ");
            opr = sc.nextLine();

            switch (opr) {
                case "l" -> {
                    if (loginSuccessfully())
                        project_dashboard();
                }
                case "r" -> register();
                case "e" -> System.exit(0);
                default  -> System.out.println("INVALID OPERATION");
            }
        } while (true);
    }

    /**
     * PRECONDITION - user must first login
     *
     * display all (projects) that are currently available in database
     * prompt user to select a project
     */
    private static void project_dashboard() throws SQLException, ClassNotFoundException, IOException {
        initialize_Projects_Unsorted();
        do{
            display_Project_Dashboard();
            System.out.print("\n" +
                                "Enter project ID to check project\n" +
                                "or 'a' to sort\n" +
                                "or 'b' to search\n" +
                                "or 'c' to JSON\n" +
                                "or 'r' to generate report\n" +
                                "or 'all' to display all projects\n" +
                                "or 'e' to exit to login page: ");
            opr = sc.nextLine();

            switch (opr){
                case "a"   -> initialize_Projects_SortedBy_Something();
                case "b"   -> initialize_Projects_SearchBy_ProjectName();
                case "c"   -> JSON_import_export();
                case "r"   -> generate_Report();
                case "all" -> initialize_Projects_Unsorted();
                case "e"   -> register_and_login();
                default -> {
                    if (isNumber(opr)){
                        int projectIDToVerify = Integer.parseInt(opr);
                        if ( validProjectID( projectIDToVerify ) ) {          // valid 'selectedProjectID' entered
                            selected_Project_ID = projectIDToVerify;
                            currentProject = ProjectQuery.getProject(selected_Project_ID);
                            issue_dashboard();
                        }
                        else
                            System.out.println("INVALID project ID!");
                    }
                    else
                        System.out.println("INVALID OPERATION");
                }
            }
        } while (true);
    }




    /**
     * PRECONDITION -  user must have chosen a project (by selecting projectID)
     *
     * display all (issues) related to a project
     * prompt user to select a issue to look further into
     */
    private static void issue_dashboard() throws SQLException, ClassNotFoundException, IOException {
        initialise_Issues_Unsorted();
        do{
            display_Issue_Dashboard();
            System.out.print("\n" +
                                "Enter issue ID to check issue\n" +
                                "or 'a' to sort\n" +
                                "or 'b' to filter\n" +
                                "or 'c' to search\n" +
                                "or 'd' to create issue\n" +
                                "or 'all' to display all issues\n" +
                                "or 'e' to exit to project dashboard: ");
            opr = sc.nextLine();

            switch (opr){
                case "a"   -> initialise_Issues_SortedBy_Something(); // sort     - (priority) / (time)
                case "b"   -> initialise_Issues_FilterBy_Something(); // filter   - (status) / (tag)
                case "c"   -> initialize_Issues_SearchBy_Key_Word();  // search() - title / description / comments
                case "d"   -> create_New_Issue();
                case "all" -> initialise_Issues_Unsorted();
                case "e"   -> project_dashboard();
                default    -> {
                    if (isNumber(opr)){
                        int issueIDToVerify = Integer.parseInt(opr);
                        if (validIssueID(issueIDToVerify)){
                            currentProject = ProjectQuery.getProject(selected_Project_ID);
                            selected_Issue_ID   = issueIDToVerify;
                            currentIssue        = IssueQuery.getIssue( selected_Issue_ID );
                            issue_page();
                        }
                        else
                            System.out.println("INVALID issue ID!");
                    }
                    else
                        System.out.println("INVALID OPERATION");
                }
            }
        } while (true);
    }


    /**
     * PRECONDITION - user must have chosen an issue (by selecting issueID)
     *
     * display all the details related to an issue
     */
    private static void issue_page() throws SQLException, ClassNotFoundException, IOException {
        do{
            displayCurrentIssue();
            System.out.print("Enter " +
                                "'c' to comment\n" +
                             "or 'r' to react\n" +
                             "or 'cs' to change status\n" +
                             "or 'cd' to change description\n" +
                             "or 'cc' to change comment\n" +
                             "or 'dcl' to view description (CHANGELOG)\n" +
                             "or 'ccl' to view comment (CHANGELOG)\n" +
                             "or 'e' to exit to issue dashboard: ");
            opr = sc.nextLine();

            switch (opr) {
                case "c"   -> comment_On_Issue();
                case "r"   -> react_On_Comment();

                case "cs"  -> change_Status();

                case "cd"  -> change_Issue_Description();
                case "cc"  -> change_Comment();

                case "dcl" -> issue_Description_Change_Log();
                case "ccl" -> comment_Change_Log();

                case "e"   -> issue_dashboard();
                default    -> System.out.println("INVALID OPERATION");
            }
        } while (true);
    }
}
