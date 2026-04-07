package View;

import Controller.SQLite;
import java.awt.CardLayout;
import java.awt.Color;

public class ManagerHome extends javax.swing.JPanel {

    public MgmtHistory mgmtHistory;
    public MgmtLogs    mgmtLogs;
    public MgmtProduct mgmtProduct;
    public MgmtUser    mgmtUser;

    private Home home;
    private Frame frame;
    private CardLayout contentView = new CardLayout();

    public ManagerHome() {
        initComponents();
    }

    public void init(SQLite sqlite, Frame frame) {
        this.frame = frame;
        mgmtHistory = new MgmtHistory(sqlite, frame);
        mgmtLogs    = new MgmtLogs(sqlite, frame);
        mgmtProduct = new MgmtProduct(sqlite, frame);
        mgmtUser    = new MgmtUser(sqlite, frame);

        home = new Home("WELCOME MANAGER!", new java.awt.Color(153, 102, 255));

        Content.setLayout(contentView);
        Content.add(home,             "home");
        Content.add(mgmtUser,         "mgmtUser");
        Content.add(mgmtHistory,      "mgmtHistory");
        Content.add(mgmtProduct,      "mgmtProduct");
        Content.add(mgmtLogs,         "mgmtLogs");
        configureRoleView();
    }

    public void updateLastLogin(String lastLoginTimestamp, String lastLoginStatus) {
        if (home != null) {
            home.setLastLoginInfo(lastLoginTimestamp, lastLoginStatus);
        }
    }

    public void showPnl(String panelName) {
        contentView.show(Content, panelName);
    }

    private void configureRoleView() {
        productsBtn.setVisible(true);
        historyBtn.setVisible(true);
        usersBtn.setVisible(false);
        logsBtn.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        usersBtn    = new javax.swing.JButton();
        productsBtn = new javax.swing.JButton();
        Content     = new javax.swing.JPanel();
        historyBtn  = new javax.swing.JButton();
        logsBtn     = new javax.swing.JButton();

        setBackground(new java.awt.Color(153, 102, 255));

        usersBtn.setBackground(new java.awt.Color(255, 255, 255));
        usersBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        usersBtn.setText("USERS");
        usersBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usersBtnActionPerformed(evt);
            }
        });

        productsBtn.setBackground(new java.awt.Color(255, 255, 255));
        productsBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        productsBtn.setText("PRODUCTS");
        productsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productsBtnActionPerformed(evt);
            }
        });

        Content.setBackground(new java.awt.Color(153, 102, 255));

        javax.swing.GroupLayout ContentLayout = new javax.swing.GroupLayout(Content);
        Content.setLayout(ContentLayout);
        ContentLayout.setHorizontalGroup(
            ContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        ContentLayout.setVerticalGroup(
            ContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 271, Short.MAX_VALUE)
        );

        historyBtn.setBackground(new java.awt.Color(255, 255, 255));
        historyBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        historyBtn.setText("HISTORY");
        historyBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historyBtnActionPerformed(evt);
            }
        });

        logsBtn.setBackground(new java.awt.Color(255, 255, 255));
        logsBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        logsBtn.setText("LOGS");
        logsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logsBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(usersBtn,    javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(historyBtn,  javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(logsBtn,     javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usersBtn,    javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(productsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(historyBtn,  javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logsBtn,     javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void usersBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usersBtnActionPerformed
        try {
            if (frame == null || !frame.canAccessRoleHome(Frame.ROLE_MANAGER)) return;
            mgmtUser.init();
            usersBtn.setForeground(Color.red); productsBtn.setForeground(Color.black);
            historyBtn.setForeground(Color.black); logsBtn.setForeground(Color.black);
            contentView.show(Content, "mgmtUser");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            if (frame != null) { frame.sessionUser = null; frame.loginNav(); }
        }
    }//GEN-LAST:event_usersBtnActionPerformed

    private void productsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productsBtnActionPerformed
        try {
            if (frame == null || !frame.canAccessRoleHome(Frame.ROLE_MANAGER)) return;
            mgmtProduct.init();
            usersBtn.setForeground(Color.black); productsBtn.setForeground(Color.red);
            historyBtn.setForeground(Color.black); logsBtn.setForeground(Color.black);
            contentView.show(Content, "mgmtProduct");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            if (frame != null) { frame.sessionUser = null; frame.loginNav(); }
        }
    }//GEN-LAST:event_productsBtnActionPerformed

    private void historyBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historyBtnActionPerformed
        try {
            if (frame == null || !frame.canAccessRoleHome(Frame.ROLE_MANAGER)) return;
            mgmtHistory.init();
            usersBtn.setForeground(Color.black); productsBtn.setForeground(Color.black);
            historyBtn.setForeground(Color.red); logsBtn.setForeground(Color.black);
            contentView.show(Content, "mgmtHistory");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            if (frame != null) { frame.sessionUser = null; frame.loginNav(); }
        }
    }//GEN-LAST:event_historyBtnActionPerformed

    private void logsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logsBtnActionPerformed
        try {
            if (frame == null || !frame.canAccessRoleHome(Frame.ROLE_MANAGER)) return;
            mgmtLogs.init();
            usersBtn.setForeground(Color.black); productsBtn.setForeground(Color.black);
            historyBtn.setForeground(Color.black); logsBtn.setForeground(Color.red);
            contentView.show(Content, "mgmtLogs");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            if (frame != null) { frame.sessionUser = null; frame.loginNav(); }
        }
    }//GEN-LAST:event_logsBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Content;
    private javax.swing.JButton historyBtn;
    private javax.swing.JButton logsBtn;
    private javax.swing.JButton productsBtn;
    private javax.swing.JButton usersBtn;
    // End of variables declaration//GEN-END:variables
}
