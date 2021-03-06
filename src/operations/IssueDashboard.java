/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import classes.Issue;
import classes.User;
import database.IssueQuery;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User 1
 */
public class IssueDashboard extends javax.swing.JFrame {
    /**
     * Creates new form IssueDashboard
     */
    static String ans;
    static User currentUser;
    static int projectID;
    
    public IssueDashboard(int projectID,User currentUser) throws SQLException, ClassNotFoundException{ 
        initComponents();
        this.projectID=projectID;
        this.currentUser=currentUser;
        display_Issue_Dashboard(projectID,1);
        this.setLocationRelativeTo(null);
        this.setTitle("Bugs Everywhere SDN BHD");
    }
        
    public void display_Issue_Dashboard(int projectID, int choice) throws SQLException, ClassNotFoundException{    
        Issue[] issues=null;
        try{
            if(choice==1){
                issues = IssueQuery.getIssues(projectID);  
             }
            else if(choice==2){
                issues = IssueQuery.getIssues_SortedBy_Priority(projectID, true);
             }
            else if(choice==3){
                issues = IssueQuery.getIssues_SortedBy_Priority(projectID, false);
             }
            else if(choice==4){
                issues = IssueQuery.getIssues_SortedBy_Time(projectID, true);
             }
            else if(choice==5){
                issues = IssueQuery.getIssues_SortedBy_Time(projectID, false);
             }
            else if(choice==6){
                issues = IssueQuery.getIssues_FilterBy_Status(projectID, ans);
             }
            else if(choice==7){
                issues = IssueQuery.getIssues_FilterBy_Tag(projectID, ans);
             }
            else if(choice==8){
                issues = IssueQuery.getIssues_SearchBy_Key_Word(projectID, ans);
             }


            DefaultTableModel dtm= (DefaultTableModel)issueTable.getModel();
            Object[] rows=new Object[8];
            for(int i=0 ; i<issues.length; i++){
                rows[0]=issues[i].getIssueID();
                rows[1]=issues[i].getTitle();
                rows[2]=issues[i].getStatus();
                rows[3]=issues[i].getTag();
                rows[4]=issues[i].getPriority();
                rows[5]=issues[i].getTime();
                rows[6]=issues[i].getAssignee().getName();
                rows[7]=issues[i].getCreator().getName();
                dtm.addRow(rows);
            }
        }
           
       catch(Exception e){
           JOptionPane.showMessageDialog(null, e);
       }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        issueTable = new javax.swing.JTable();
        priority = new javax.swing.JButton();
        time = new javax.swing.JButton();
        status = new javax.swing.JButton();
        tag = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        showAll = new javax.swing.JButton();
        searchTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        addIssue = new javax.swing.JButton();
        back = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jButton6.setText("jButton6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 4), "Issue Dashboard", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 24))); // NOI18N

        issueTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Title", "Status", "Tag", "Priority", "Time", "Assignee", "CreatedBy"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        issueTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                issueTableFocusGained(evt);
            }
        });
        issueTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                issueTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(issueTable);

        priority.setText("Priority");
        priority.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priorityActionPerformed(evt);
            }
        });

        time.setText("Time");
        time.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeActionPerformed(evt);
            }
        });

        status.setText("Status");
        status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusActionPerformed(evt);
            }
        });

        tag.setText("Tag");
        tag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagActionPerformed(evt);
            }
        });

        jLabel1.setText("Sort by: ");

        jLabel2.setText("Filter by: ");

        showAll.setText("Show All");
        showAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAllActionPerformed(evt);
            }
        });

        searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyPressed(evt);
            }
        });

        jLabel3.setText("Search: ");

        addIssue.setText("Add Issue");
        addIssue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addIssueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(priority, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(time, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(221, 221, 221)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(tag, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(showAll, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(111, 111, 111))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 898, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addIssue)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(priority)
                            .addComponent(time)
                            .addComponent(jLabel1)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(status)
                            .addComponent(tag)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(showAll, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(addIssue)
                        .addGap(179, 179, 179))))
        );

        back.setText("Back");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(back)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusActionPerformed
        try {
            int reaction = 6;
            ans = JOptionPane.showInputDialog("Enter status to be filtered(Open, Closed, In Progress)");
            while (ans.isEmpty() || ans.isBlank()){
                JOptionPane.showMessageDialog(null,"Status cannot be BLANK or EMPTY");
                ans = JOptionPane.showInputDialog("Enter status to be filtered(Open, Closed, In Progress)");}
            DefaultTableModel model = (DefaultTableModel)issueTable.getModel();
            model.setRowCount(0); 
            display_Issue_Dashboard(projectID,reaction);
            
        
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_statusActionPerformed

    private void issueTableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_issueTableFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_issueTableFocusGained

    private void issueTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_issueTableMouseClicked
       int column = 0;
       int row = issueTable.getSelectedRow();
       int selected_Issue_ID = (Integer) issueTable.getModel().getValueAt(row, column);
        try {
            IssuePageGUI issuePage = new IssuePageGUI(selected_Issue_ID, currentUser);
            issuePage.setVisible(true);
            this.dispose();
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_issueTableMouseClicked

    private void priorityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priorityActionPerformed
        String[] options={"Ascending","Descending"};
        
        try {
            int reaction=JOptionPane.showOptionDialog(null, "Do you want to sort in ascending or descending order?","Sorting..",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
            if(reaction==0){
                reaction=2;
            }
            else if(reaction==1)
                reaction=3;
            else
                return;
            DefaultTableModel model = (DefaultTableModel)issueTable.getModel();
            model.setRowCount(0);   
            display_Issue_Dashboard(projectID,reaction);
        
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }//GEN-LAST:event_priorityActionPerformed

    private void tagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagActionPerformed
        try {
            int reaction = 7;
            ans = JOptionPane.showInputDialog("Enter tag to be filtered(Frontend, Backend, cmty:bug-report)");
            while (ans.isEmpty() || ans.isBlank()){
                JOptionPane.showMessageDialog(null,"Tag cannot be BLANK or EMPTY");
                ans = JOptionPane.showInputDialog("Enter tag to be filtered(Frontend, Backend, cmty:bug-report)");}
            
            DefaultTableModel model = (DefaultTableModel)issueTable.getModel();
            model.setRowCount(0); 
            display_Issue_Dashboard(projectID,reaction);
            
            
        
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_tagActionPerformed

    private void searchTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyPressed
       try{
            int reaction = 8;
            if(evt.getKeyCode() == KeyEvent.VK_ENTER){
                ans = searchTextField.getText();
                if(ans.isEmpty() || ans.isBlank()){
                    JOptionPane.showMessageDialog(null,"Search field cannot be BLANK or EMPTY");
                }
                else if(!ans.isEmpty() || !ans.isBlank()){
                    DefaultTableModel model = (DefaultTableModel)issueTable.getModel();
                    model.setRowCount(0); 
                    display_Issue_Dashboard(projectID,reaction);
            }
            }
            
        }
        
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_searchTextFieldKeyPressed

    private void showAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAllActionPerformed
        try {
            DefaultTableModel model = (DefaultTableModel)issueTable.getModel();
            model.setRowCount(0);
            display_Issue_Dashboard(projectID,1);
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_showAllActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        try {
            ProjectDashboard pd=new ProjectDashboard(currentUser);
            pd.setVisible(true);
            this.dispose();
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_backActionPerformed

    private void addIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addIssueActionPerformed
        try{
            AddIssueFrame frame = new AddIssueFrame(projectID,currentUser);
            frame.setVisible(true);
            this.dispose();
        
        }
        catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Issue not added");
        }
    }//GEN-LAST:event_addIssueActionPerformed

    private void timeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeActionPerformed
        String[] options={"Ascending","Descending"};
        
        try {
            int reaction=JOptionPane.showOptionDialog(null, "Do you want to sort in ascending or descending order?","Sorting..",JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
            if(reaction==0){
                reaction=4;
            }
            else if(reaction==1)
                reaction=5;
            else
                return;
            DefaultTableModel model = (DefaultTableModel)issueTable.getModel();
            model.setRowCount(0);   
            display_Issue_Dashboard(projectID,reaction);
        
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_timeActionPerformed

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
            java.util.logging.Logger.getLogger(IssueDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IssueDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IssueDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IssueDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run(){
                try {
                    new IssueDashboard(projectID,currentUser).setVisible(true);
                } 
                catch (SQLException ex) {
                    Logger.getLogger(IssueDashboard.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (ClassNotFoundException ex) {
                    Logger.getLogger(IssueDashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addIssue;
    private javax.swing.JButton back;
    private javax.swing.JTable issueTable;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton priority;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JButton showAll;
    private javax.swing.JButton status;
    private javax.swing.JButton tag;
    private javax.swing.JButton time;
    // End of variables declaration//GEN-END:variables
}
