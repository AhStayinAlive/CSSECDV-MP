package View;

import Controller.SQLite;
import Model.User;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.mindrot.jbcrypt.BCrypt;

public class MgmtUser extends javax.swing.JPanel {

    public SQLite sqlite;
    public DefaultTableModel tableModel;
    public Frame frame;
    private static final DateTimeFormatter TS_FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MgmtUser(SQLite sqlite, Frame frame) {
        initComponents();
        this.sqlite = sqlite;
        this.frame  = frame;
        tableModel  = (DefaultTableModel) table.getModel();
        table.getTableHeader().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
    }

    public void init() {
        if (frame == null || !frame.canAccessRoleHome(Frame.ROLE_ADMIN)) {
            for (int nCtr = tableModel.getRowCount(); nCtr > 0; nCtr--) {
                tableModel.removeRow(0);
            }
            editRoleBtn.setVisible(false);
            deleteBtn.setVisible(false);
            lockBtn.setVisible(false);
            chgpassBtn.setVisible(false);
            return;
        }
        editRoleBtn.setVisible(true);
        deleteBtn.setVisible(true);
        lockBtn.setVisible(true);
        chgpassBtn.setVisible(true);
        for (int nCtr = tableModel.getRowCount(); nCtr > 0; nCtr--) {
            tableModel.removeRow(0);
        }
        ArrayList<User> users = sqlite.getUsers();
        for (int nCtr = 0; nCtr < users.size(); nCtr++) {
            tableModel.addRow(new Object[]{
                users.get(nCtr).getUsername(),
                users.get(nCtr).getPassword(),
                users.get(nCtr).getRole(),
                users.get(nCtr).getLocked()
            });
        }
    }

    public void designer(JTextField component, String text) {
        component.setSize(70, 600);
        component.setFont(new java.awt.Font("Tahoma", 0, 18));
        component.setBackground(new java.awt.Color(240, 240, 240));
        component.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        component.setBorder(javax.swing.BorderFactory.createTitledBorder(
                new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true),
                text, javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 0, 12)));
    }

    /**
     * Shows a masked password dialog and verifies the entered value against the
     * session user's stored BCrypt hash.
     *
     * @return true if the logged-in user's password was entered correctly
     */
    private boolean reAuthenticate() {
        if (frame == null || frame.sessionUser == null) {
            JOptionPane.showMessageDialog(this,
                    "No active session found.",
                    "Authentication Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JPasswordField reAuthPass = new JPasswordField();
        designer(reAuthPass, "CURRENT PASSWORD");

        int result = JOptionPane.showConfirmDialog(this,
                new Object[]{"Enter your current password to continue:", reAuthPass},
                "Re-Authentication Required",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return false;

        String entered = new String(reAuthPass.getPassword());
        if (entered.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Password cannot be empty.",
                    "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!BCrypt.checkpw(entered, frame.sessionUser.getPassword())) {
            JOptionPane.showMessageDialog(this,
                    "Incorrect password. Operation cancelled.",
                    "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table        = new javax.swing.JTable();
        editRoleBtn  = new javax.swing.JButton();
        deleteBtn    = new javax.swing.JButton();
        lockBtn      = new javax.swing.JButton();
        chgpassBtn   = new javax.swing.JButton();

        table.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String[] { "Username", "Password", "Role", "Locked" }
        ) {
            boolean[] canEdit = new boolean[]{ false, false, false, false };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(160);
            table.getColumnModel().getColumn(1).setPreferredWidth(400);
            table.getColumnModel().getColumn(2).setPreferredWidth(100);
            table.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        editRoleBtn.setBackground(new java.awt.Color(255, 255, 255));
        editRoleBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        editRoleBtn.setText("EDIT ROLE");
        editRoleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editRoleBtnActionPerformed(evt);
            }
        });

        deleteBtn.setBackground(new java.awt.Color(255, 255, 255));
        deleteBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        deleteBtn.setText("DELETE");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        lockBtn.setBackground(new java.awt.Color(255, 255, 255));
        lockBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lockBtn.setText("LOCK/UNLOCK");
        lockBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lockBtnActionPerformed(evt);
            }
        });

        chgpassBtn.setBackground(new java.awt.Color(255, 255, 255));
        chgpassBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        chgpassBtn.setText("CHANGE PASS");
        chgpassBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chgpassBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(editRoleBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(deleteBtn,   javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(lockBtn,     javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(chgpassBtn,  javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chgpassBtn,  javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteBtn,   javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editRoleBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lockBtn,     javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void editRoleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editRoleBtnActionPerformed
        try {
            if (frame == null || !frame.canAccessRoleHome(Frame.ROLE_ADMIN)) return;
            if (table.getSelectedRow() < 0) return;

            if (!reAuthenticate()) return;

            String[] options = {"1-DISABLED", "2-CLIENT", "3-STAFF", "4-MANAGER", "5-ADMIN"};
            int currentRole  = (int) tableModel.getValueAt(table.getSelectedRow(), 2);
            String targetUser = (String) tableModel.getValueAt(table.getSelectedRow(), 0);

            String result = (String) JOptionPane.showInputDialog(null,
                    "USER: " + targetUser,
                    "EDIT USER ROLE",
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[currentRole - 1]);

            if (result != null) {
                int newRole = Character.getNumericValue(result.charAt(0));

                if (newRole < 1 || newRole > 5) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid role code. Role must be an integer between 1 and 5.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                sqlite.updateUserRole(targetUser, newRole);

                sqlite.addLogs(
                    "USER_ROLE_UPDATE",
                    frame.sessionUser.getUsername(),
                    "Updated role for user: " + targetUser + " to role " + newRole,
                    LocalDateTime.now().format(TS_FMT)
            );
                init();
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "Unexpected error", ex);
            JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            if (frame != null) { frame.sessionUser = null; frame.loginNav(); }
        }
    }//GEN-LAST:event_editRoleBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        try {
            if (frame == null || !frame.canAccessRoleHome(Frame.ROLE_ADMIN)) return;
            if (table.getSelectedRow() < 0) return;

            String targetUser = (String) tableModel.getValueAt(table.getSelectedRow(), 0);

            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete " + targetUser + "?",
                    "DELETE USER", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            if (!reAuthenticate()) return;

            sqlite.removeUser(targetUser);
            sqlite.addLogs(
                    "USER_DELETE",
                    frame.sessionUser.getUsername(),
                    "Deleted user: " + targetUser,
                    LocalDateTime.now().format(TS_FMT)
            );
            init();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "Unexpected error", ex);
            JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            if (frame != null) { frame.sessionUser = null; frame.loginNav(); }
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void lockBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lockBtnActionPerformed
        try {
            if (frame == null || !frame.canAccessRoleHome(Frame.ROLE_ADMIN)) return;
            if (table.getSelectedRow() < 0) return;

            String targetUser  = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
            Object lockedObj   = tableModel.getValueAt(table.getSelectedRow(), 3);
            int currentLocked  = (lockedObj instanceof Integer)
                    ? (Integer) lockedObj
                    : Integer.parseInt(lockedObj.toString());
            String action      = (currentLocked == 1) ? "unlock" : "lock";

            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to " + action + " " + targetUser + "?",
                    "LOCK/UNLOCK USER", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            if (!reAuthenticate()) return;

            sqlite.setUserLocked(targetUser, currentLocked == 1 ? 0 : 1);
            
            String actionLog = (currentLocked == 1) ? "UNLOCK_USER" : "LOCK_USER";

            sqlite.addLogs(
                    actionLog,
                    frame.sessionUser.getUsername(),
                    (currentLocked == 1 ? "Unlocked " : "Locked ") + "user: " + targetUser,
                    LocalDateTime.now().format(TS_FMT)
            );
            
            init();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "Unexpected error", ex);
            JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            if (frame != null) { frame.sessionUser = null; frame.loginNav(); }
        }
    }//GEN-LAST:event_lockBtnActionPerformed

    private void chgpassBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chgpassBtnActionPerformed
        try {
            if (frame == null || !frame.canAccessRoleHome(Frame.ROLE_ADMIN)) return;
            if (table.getSelectedRow() < 0) return;

            String targetUser = (String) tableModel.getValueAt(table.getSelectedRow(), 0);

            if (!reAuthenticate()) return;

            JPasswordField newPassFld  = new JPasswordField();
            JPasswordField confPassFld = new JPasswordField();
            designer(newPassFld,  "NEW PASSWORD");
            designer(confPassFld, "CONFIRM PASSWORD");

            int result = JOptionPane.showConfirmDialog(null,
                    new Object[]{"Enter new password for " + targetUser + ":", newPassFld, confPassFld},
                    "CHANGE PASSWORD",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) return;

            String newPass  = new String(newPassFld.getPassword());
            String confPass = new String(confPassFld.getPassword());

            if (!Register.isValidPassword(newPass)) {
                JOptionPane.showMessageDialog(this,
                        "Password must be 8\u201364 characters and contain uppercase, lowercase, digit, and special character.",
                        "Password Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPass.equals(confPass)) {
                JOptionPane.showMessageDialog(this,
                        "Passwords do not match.",
                        "Password Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String hashed = BCrypt.hashpw(newPass, BCrypt.gensalt(12));
            sqlite.updatePassword(targetUser, hashed);

            sqlite.addLogs(
                    "ADMIN_PASSWORD_CHANGE",
                    frame.sessionUser.getUsername(),
                    "Changed password for user: " + targetUser,
                    LocalDateTime.now().format(TS_FMT)
            );

            JOptionPane.showMessageDialog(this,
                    "Password updated successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            init();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "Unexpected error", ex);
            JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            if (frame != null) { frame.sessionUser = null; frame.loginNav(); }
        }
    }//GEN-LAST:event_chgpassBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chgpassBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editRoleBtn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton lockBtn;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
