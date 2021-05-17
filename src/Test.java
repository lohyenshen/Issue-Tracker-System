import database.ProjectQuery;
import operations.Operations;
import system.Project;

import java.sql.SQLException;

public class Test extends Operations {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        projects = ProjectQuery.getProjects_SortedBy_projectID(true);

        for (Project p : projects)
            System.out.println( p  + "\n\n\n\n");
    }
}
