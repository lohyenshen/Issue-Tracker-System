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
    static Project[] projects;
    static User currentUser;
    public ProjectDashboard(User currentUser) throws SQLException, ClassNotFoundException {
        initComponents();
        this.setLocationRelativeTo(null);
        this.currentUser=currentUser;
        display_Project_Dashboard(1);
    }
    
    private void display_Project_Dashboard(int selection) throws SQLException, ClassNotFoundException{
       
       try{
           if(selection==1){
            projects = ProjectQuery.getProjects_Unsorted();  
             }
             else if(selection==2){
              projects = ProjectQuery.getProjects_SortedBy_Alphanumeric( true );
             }
             else if(selection==3){
              projects = ProjectQuery.getProjects_SortedBy_Alphanumeric( false );
             }
             else if(selection==4){
               projects = ProjectQuery.getProjects_SortedBy_projectID( true );
             }
             else if(selection==5){
                projects = ProjectQuery.getProjects_SortedBy_projectID( false );
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
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        projectTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        id = new javax.swing.JButton();
        projectName = new javax.swing.JButton();
        json = new javax.swing.JButton();
        report = new javax.swing.JButton();
        logout = new javax.swing.JButton();
        refresh = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Project Dashboard");

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

        report.setText("Report");

        logout.setText("Log Out");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });

        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(report, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60, 60, 60)
                                .addComponent(json, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(175, 175, 175))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(projectName)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(73, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(217, 217, 217)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(id)
                    .addComponent(projectName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(refresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(json)
                    .addComponent(report))
                .addGap(14, 14, 14)
                .addComponent(logout)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        // TODO add your handling code here:
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
        // TODO add your handling code here:
        Login login=new Login();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_logoutActionPerformed

    private void jsonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jsonActionPerformed
        // TODO add your handling code here:
        String[] options={"Import","Export"};
        try{
            int reaction=JOptionPane.showOptionDialog(null, "Do you want to IMPORT or EXPORT JSON file?","",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
            if(reaction==0)
                importJson();
            else if(reaction==1)
                exportJson();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        
    }//GEN-LAST:event_jsonActionPerformed

    private void projectTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_projectTableMouseClicked
        // TODO add your handling code here:
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

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        try {
            DefaultTableModel model = (DefaultTableModel)projectTable.getModel();
            model.setRowCount(0);
            display_Project_Dashboard(1);
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_refreshActionPerformed

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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton json;
    private javax.swing.JButton logout;
    private javax.swing.JButton projectName;
    private javax.swing.JTable projectTable;
    private javax.swing.JButton refresh;
    private javax.swing.JButton report;
    // End of variables declaration//GEN-END:variables


}
