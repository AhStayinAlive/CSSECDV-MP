package View;

import Controller.Main;
import Model.User;
import java.awt.CardLayout;
import javax.swing.WindowConstants;

public class Frame extends javax.swing.JFrame {

    public static final int ROLE_DISABLED = 1;
    public static final int ROLE_CLIENT   = 2;
    public static final int ROLE_STAFF    = 3;
    public static final int ROLE_MANAGER  = 4;
    public static final int ROLE_ADMIN    = 5;

    /** The user currently logged in. Set by Login on success; cleared on logout. */
    public User sessionUser = null;

    public Frame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Container = new javax.swing.JPanel();
        HomePnl = new javax.swing.JPanel();
        Content = new javax.swing.JPanel();
        Navigation = new javax.swing.JPanel();
        adminBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        managerBtn = new javax.swing.JButton();
        staffBtn = new javax.swing.JButton();
        clientBtn = new javax.swing.JButton();
        logoutBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 153, 153));
        setMinimumSize(new java.awt.Dimension(800, 450));

        HomePnl.setBackground(new java.awt.Color(102, 102, 102));
        HomePnl.setPreferredSize(new java.awt.Dimension(801, 500));

        javax.swing.GroupLayout ContentLayout = new javax.swing.GroupLayout(Content);
        Content.setLayout(ContentLayout);
        ContentLayout.setHorizontalGroup(
            ContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 544, Short.MAX_VALUE)
        );
        ContentLayout.setVerticalGroup(
            ContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Navigation.setBackground(new java.awt.Color(204, 204, 204));

        adminBtn.setBackground(new java.awt.Color(250, 250, 250));
        adminBtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        adminBtn.setText("Admin Home");
        adminBtn.setFocusable(false);
        adminBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminBtnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SECURITY Svcs");
        jLabel1.setToolTipText("");

        managerBtn.setBackground(new java.awt.Color(250, 250, 250));
        managerBtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        managerBtn.setText("Manager Home");
        managerBtn.setFocusable(false);
        managerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                managerBtnActionPerformed(evt);
            }
        });

        staffBtn.setBackground(new java.awt.Color(250, 250, 250));
        staffBtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        staffBtn.setText("Staff Home");
        staffBtn.setFocusable(false);
        staffBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffBtnActionPerformed(evt);
            }
        });

        clientBtn.setBackground(new java.awt.Color(250, 250, 250));
        clientBtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        clientBtn.setText("Client Home");
        clientBtn.setFocusable(false);
        clientBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientBtnActionPerformed(evt);
            }
        });

        logoutBtn.setBackground(new java.awt.Color(250, 250, 250));
        logoutBtn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        logoutBtn.setText("LOGOUT");
        logoutBtn.setFocusable(false);
        logoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout NavigationLayout = new javax.swing.GroupLayout(Navigation);
        Navigation.setLayout(NavigationLayout);
        NavigationLayout.setHorizontalGroup(
            NavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(NavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(adminBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                    .addComponent(managerBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(staffBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clientBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logoutBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        NavigationLayout.setVerticalGroup(
            NavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(adminBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(managerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(staffBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, Short.MAX_VALUE)
                .addComponent(logoutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout HomePnlLayout = new javax.swing.GroupLayout(HomePnl);
        HomePnl.setLayout(HomePnlLayout);
        HomePnlLayout.setHorizontalGroup(
            HomePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HomePnlLayout.createSequentialGroup()
                .addComponent(Navigation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HomePnlLayout.setVerticalGroup(
            HomePnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Navigation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ContainerLayout = new javax.swing.GroupLayout(Container);
        Container.setLayout(ContainerLayout);
        ContainerLayout.setHorizontalGroup(
            ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 980, Short.MAX_VALUE)
            .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(HomePnl, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE))
        );
        ContainerLayout.setVerticalGroup(
            ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
            .addGroup(ContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(HomePnl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void adminBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminBtnActionPerformed
        try {
            if (!canAccessRoleHome(ROLE_ADMIN)) return;
            adminHomePnl.showPnl("home");
            contentView.show(Content, "adminHomePnl");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            sessionUser = null;
            loginNav();
        }
    }//GEN-LAST:event_adminBtnActionPerformed

    private void managerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_managerBtnActionPerformed
        try {
            if (!canAccessRoleHome(ROLE_MANAGER)) return;
            managerHomePnl.showPnl("home");
            contentView.show(Content, "managerHomePnl");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            sessionUser = null;
            loginNav();
        }
    }//GEN-LAST:event_managerBtnActionPerformed

    private void staffBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffBtnActionPerformed
        try {
            if (!canAccessRoleHome(ROLE_STAFF)) return;
            staffHomePnl.showPnl("home");
            contentView.show(Content, "staffHomePnl");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            sessionUser = null;
            loginNav();
        }
    }//GEN-LAST:event_staffBtnActionPerformed

    private void clientBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientBtnActionPerformed
        try {
            if (!canAccessRoleHome(ROLE_CLIENT)) return;
            clientHomePnl.showPnl("home");
            contentView.show(Content, "clientHomePnl");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "Unexpected error", ex);
            javax.swing.JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            sessionUser = null;
            loginNav();
        }
    }//GEN-LAST:event_clientBtnActionPerformed

    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutBtnActionPerformed
        try {
            sessionUser = null;
            configureNavigationForRole(0);
            frameView.show(Container, "loginPnl");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "Unexpected error", ex);
            sessionUser = null;
            configureNavigationForRole(0);
            frameView.show(Container, "loginPnl");
        }
    }//GEN-LAST:event_logoutBtnActionPerformed

    public Main main;
    public Login loginPnl   = new Login();
    public Register registerPnl = new Register();

    private AdminHome   adminHomePnl   = new AdminHome();
    private ManagerHome managerHomePnl = new ManagerHome();
    private StaffHome   staffHomePnl   = new StaffHome();
    private ClientHome  clientHomePnl  = new ClientHome();

    private CardLayout contentView = new CardLayout();
    private CardLayout frameView   = new CardLayout();

    public void init(Main controller) {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("CSSECDV - SECURITY Svcs");
        this.setLocationRelativeTo(null);

        this.main = controller;
        loginPnl.frame    = this;
        registerPnl.frame = this;

        // Pass 'this' so each home panel can thread Frame to MgmtUser for re-auth
        adminHomePnl.init(main.sqlite, this);
        clientHomePnl.init(main.sqlite, this);
        managerHomePnl.init(main.sqlite, this);
        staffHomePnl.init(main.sqlite, this);

        Container.setLayout(frameView);
        Container.add(loginPnl,    "loginPnl");
        Container.add(registerPnl, "registerPnl");
        Container.add(HomePnl,     "homePnl");
        frameView.show(Container, "loginPnl");

        Content.setLayout(contentView);
        Content.add(adminHomePnl,   "adminHomePnl");
        Content.add(managerHomePnl, "managerHomePnl");
        Content.add(staffHomePnl,   "staffHomePnl");
        Content.add(clientHomePnl,  "clientHomePnl");

        this.setVisible(true);
    }

    /**
     * Called after successful login.  Routes to the correct role panel, updates
     * the last-login display with the PREVIOUS session's values, then records
     * the current timestamp in the DB.
     *
     * @param user           the authenticated User object (role, username, etc.)
     * @param prevTimestamp  last_login_timestamp value that was in DB before this login
     * @param prevStatus     last_login_status value that was in DB before this login
     */
    public void mainNav(User user, String prevTimestamp, String prevStatus) {
        sessionUser = user;
        frameView.show(Container, "homePnl");
        configureNavigationForRole(user.getRole());

        int role = user.getRole();
        if (role == ROLE_ADMIN) {                // Administrator
            adminHomePnl.updateLastLogin(prevTimestamp, prevStatus);
            adminHomePnl.showPnl("home");
            contentView.show(Content, "adminHomePnl");
        } else if (role == ROLE_MANAGER) {       // Manager
            managerHomePnl.updateLastLogin(prevTimestamp, prevStatus);
            managerHomePnl.showPnl("home");
            contentView.show(Content, "managerHomePnl");
        } else if (role == ROLE_STAFF) {         // Staff
            staffHomePnl.updateLastLogin(prevTimestamp, prevStatus);
            staffHomePnl.showPnl("home");
            contentView.show(Content, "staffHomePnl");
        } else if (role == ROLE_CLIENT) {         // Client
            clientHomePnl.updateLastLogin(prevTimestamp, prevStatus);
            clientHomePnl.showPnl("home");
            contentView.show(Content, "clientHomePnl");
        } else {
            sessionUser = null;
            configureNavigationForRole(0);
            frameView.show(Container, "loginPnl");
        }
    }

    public void loginNav() {
        configureNavigationForRole(0);
        frameView.show(Container, "loginPnl");
    }

    public void registerNav() {
        configureNavigationForRole(0);
        frameView.show(Container, "registerPnl");
    }

    /**
     * Creates the account.  All validation (policy, uniqueness, confirm-match)
     * is done in Register.java before this is called.
     * Password is BCrypt-hashed inside SQLite.addUser().
     */
    public void registerAction(String username, String password) {
        main.sqlite.addUser(username, password);
    }

    public boolean canAccessRoleHome(int requiredRole) {
        return sessionUser != null && sessionUser.getRole() == requiredRole;
    }

    private void configureNavigationForRole(int role) {
        adminBtn.setVisible(role == ROLE_ADMIN);
        managerBtn.setVisible(role == ROLE_MANAGER);
        staffBtn.setVisible(role == ROLE_STAFF);
        clientBtn.setVisible(role == ROLE_CLIENT);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Container;
    private javax.swing.JPanel Content;
    private javax.swing.JPanel HomePnl;
    private javax.swing.JPanel Navigation;
    private javax.swing.JButton adminBtn;
    private javax.swing.JButton clientBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton logoutBtn;
    private javax.swing.JButton managerBtn;
    private javax.swing.JButton staffBtn;
    // End of variables declaration//GEN-END:variables
}
