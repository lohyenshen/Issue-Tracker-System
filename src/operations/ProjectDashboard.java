/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import classes.Project;
import classes.Projects_And_Users;
import classes.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.ProjectQuery;
import database.UserQuery;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author User 1
 */
public class ProjectDashboard extends javax.swing.JFrame {
    /**
     * Creates new form ProjectDashboard
     */
    private static Project[] projects;
    private static User currentUser;
    private static String path_To_R_Script_Exe;
    
    public ProjectDashboard(User currentUser) throws SQLException, ClassNotFoundException {
        initComponents();
        this.setLocationRelativeTo(null);
        this.currentUser=currentUser;
        this.setTitle("Bugs Everywhere SDN BHD");
        display_Project_Dashboard(1);
    }
    
    private void display_Project_Dashboard(int selection) throws SQLException, ClassNotFoundException{
       
       try{
           if(selection==1){
            projects = ProjectQuery.getProjects_Unsorted();  
             }
             else if(selection==2){
              projects = ProjectQuery.getProjects_SortedBy_Alphanumeric( true );    //sort by alphanumeric in asc
             }
             else if(selection==3){
              projects = ProjectQuery.getProjects_SortedBy_Alphanumeric( false );   //sort by alphanumeric in dsc
             }
             else if(selection==4){
               projects = ProjectQuery.getProjects_SortedBy_projectID( true );      //sort by id in asc
             }
             else if(selection==5){ 
                projects = ProjectQuery.getProjects_SortedBy_projectID( false );    //sort by id in dsc
             }
            DefaultTableModel dtm = (DefaultTableModel)projectTable.getModel();
            Object[] row=new Object[3];
            for(int i=0 ; i<projects.length; i++){
                row[0]=projects[i].getProjectID();
                row[1]=projects[i].getName();
                row[2]=projects[i].getIssues().length;
                dtm.addRow(row);
            }
           
       }catch(Exception e){
           JOptionPane.showMessageDialog(null, e);
       }
    }
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
    
    // Report generation
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

    private static void generate_Report_Folder_If_Not_Exits() {
        File report_directory = new File(System.getProperty("user.dir") + "\\Report Generation");
        if (!report_directory.exists())
            report_directory.mkdirs();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        projectTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        id = new javax.swing.JButton();
        projectName = new javax.swing.JButton();
        json = new javax.swing.JButton();
        report = new javax.swing.JButton();
        logout = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4), "Project Dashboard", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N

        projectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Project Name", "Issues"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        projectTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                projectTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(projectTable);

        jLabel3.setText("Sort by : ");

        id.setText("ID");
        id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idActionPerformed(evt);
            }
        });

        projectName.setText("Project name");
        projectName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                projectNameActionPerformed(evt);
            }
        });

        json.setText("JSON");
        json.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jsonActionPerformed(evt);
            }
        });

        report.setText("Generate Report");
        report.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(projectName)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(report)
                        .addGap(47, 47, 47)
                        .addComponent(json, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(id)
                    .addComponent(projectName))
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(json)
                    .addComponent(report))
                .addGap(40, 40, 40))
        );

        logout.setText("Log Out");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logout)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idActionPerformed
        String[] options={"Ascending","Descending"};
        
        try {
            int reaction=JOptionPane.showOptionDialog(null, "Do you want to sort in ascending or descending order?","Sorting..",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
            if(reaction==0){
                reaction=4;
            }
            else if(reaction==1)
                reaction=5;
            DefaultTableModel model = (DefaultTableModel)projectTable.getModel();
            model.setRowCount(0);   
            display_Project_Dashboard(reaction);
        
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_idActionPerformed

    private void projectNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_projectNameActionPerformed
        String[] options={"Ascending","Descending"};
        try {
            int reaction=JOptionPane.showOptionDialog(null, "Do you want to sort in ascending or descending order?","Sorting..",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
            if(reaction==0){
                reaction=2;
            }
            else if(reaction==1)
                reaction=3;
            DefaultTableModel model = (DefaultTableModel)projectTable.getModel();
            model.setRowCount(0);   
            display_Project_Dashboard(reaction);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_projectNameActionPerformed

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        Login login=new Login();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_logoutActionPerformed

    private void jsonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jsonActionPerformed
        String[] options={"Import","Export"};
        try{
            int reaction=JOptionPane.showOptionDialog(null, "Do you want to IMPORT or EXPORT JSON file?","",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
            if(reaction==0)
                importJson();
            else if(reaction==1)
                exportJson();
            
            //refresh the table
            DefaultTableModel model = (DefaultTableModel)projectTable.getModel();
            model.setRowCount(0);
            display_Project_Dashboard(1);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }//GEN-LAST:event_jsonActionPerformed

    private void projectTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_projectTableMouseClicked
       int column = 0;
       int row = projectTable.getSelectedRow();
       int projectID = (Integer) projectTable.getModel().getValueAt(row, column);

        try {
            IssueDashboard issueDashboard = new IssueDashboard(projectID,currentUser);
            issueDashboard.setVisible(true);
            this.dispose();
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, e);
        }
        
    }//GEN-LAST:event_projectTableMouseClicked

    private void reportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportActionPerformed
        generate_Report_Folder_If_Not_Exits();
        deleteAllFiles();

        String R_Script_Exe;
        try {
            R_Script_Exe        = getPathTo_R_Script_Exe();
            String R_code       = System.getProperty("user.dir") + "\\Rscript\\report_generation_code.R";

            ProcessBuilder pb = new ProcessBuilder(R_Script_Exe, "", R_code);
            pb.start();
            JOptionPane.showMessageDialog(null,"REPORTS GENERATED SUCCESSFULLY");
        } catch (IOException ex) {
            Logger.getLogger(ProjectDashboard.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null,"REPORTS GENERATION FAILED");
        }
           

        
    }//GEN-LAST:event_reportActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProjectDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProjectDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProjectDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProjectDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ProjectDashboard(currentUser).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(ProjectDashboard.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProjectDashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton id;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton json;
    private javax.swing.JButton logout;
    private javax.swing.JButton projectName;
    private javax.swing.JTable projectTable;
    private javax.swing.JButton report;
    // End of variables declaration//GEN-END:variables


}
