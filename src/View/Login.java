
package View;

import Model.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;

public class Login extends javax.swing.JPanel {

    public Frame frame;

    /**
     * A pre-computed BCrypt hash used when the supplied username does not exist.
     * Calling BCrypt.checkpw() against this hash ensures the response time is
     * indistinguishable from a real failed attempt, preventing timing-based
     * username enumeration.  Computed once at class-load time so the hash is
     * always structurally valid.
     */
    private static final String DUMMY_HASH =
            BCrypt.hashpw("_timing_defense_placeholder_XyZ9!", BCrypt.gensalt(12));

    private static final DateTimeFormatter TS_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Login() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1     = new javax.swing.JLabel();
        usernameFld = new javax.swing.JTextField();
        passwordFld = new javax.swing.JPasswordField();
        registerBtn = new javax.swing.JButton();
        loginBtn    = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SECURITY Svcs");
        jLabel1.setToolTipText("");

        usernameFld.setBackground(new java.awt.Color(240, 240, 240));
        usernameFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        usernameFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usernameFld.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "USERNAME", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        passwordFld.setBackground(new java.awt.Color(240, 240, 240));
        passwordFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        passwordFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passwordFld.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "PASSWORD", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        registerBtn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        registerBtn.setText("REGISTER");
        registerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerBtnActionPerformed(evt);
            }
        });

        loginBtn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        loginBtn.setText("LOGIN");
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(200, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(registerBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(usernameFld)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(passwordFld, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(200, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(usernameFld, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordFld, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(registerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loginBtn,    javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(126, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        // Step 1 — read field values; trim username, never trim password
        String username        = usernameFld.getText().trim();
        String enteredPassword = new String(passwordFld.getPassword());

        // Clear password field immediately so it isn't readable in memory longer than needed
        passwordFld.setText("");

        // Step 2 — basic empty-field guard
        if (username.isEmpty() || enteredPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Invalid username and/or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 3 — look up user in DB
        User user = frame.main.sqlite.getUserByUsername(username);

        // Step 4 — if username does not exist, perform a dummy BCrypt check to
        //           maintain consistent response time (prevents timing enumeration),
        //           then show the same generic error as a wrong password.
        if (user == null) {
            BCrypt.checkpw(enteredPassword, DUMMY_HASH);   // constant-time defence
            JOptionPane.showMessageDialog(this,
                    "Invalid username and/or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 5 — check if the account is already disabled (role == 1)
        if (user.getRole() == 1) {
            JOptionPane.showMessageDialog(this,
                    "This account has been disabled. Please contact the administrator.",
                    "Account Disabled", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 6 — verify password with BCrypt
        String currentTimestamp = LocalDateTime.now().format(TS_FMT);
        boolean passwordMatches = BCrypt.checkpw(enteredPassword, user.getPassword());

        if (!passwordMatches) {
            // Increment and reload to get the fresh count
            frame.main.sqlite.incrementFailedAttempts(username);
            User refreshed = frame.main.sqlite.getUserByUsername(username);
            int attempts   = (refreshed != null) ? refreshed.getFailedAttempts() : 5;

            if (attempts >= 5) {
                // Lock the account on the 5th failure
                frame.main.sqlite.disableUser(username);
                frame.main.sqlite.updateLastLogin(username, currentTimestamp, "FAILED");
                JOptionPane.showMessageDialog(this,
                        "This account has been disabled. Please contact the administrator.",
                        "Account Disabled", JOptionPane.ERROR_MESSAGE);
            } else {
                frame.main.sqlite.updateLastLogin(username, currentTimestamp, "FAILED");
                JOptionPane.showMessageDialog(this,
                        "Invalid username and/or password.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        // Step 7 — successful login
        // Capture the PREVIOUS session values before overwriting them
        String prevTimestamp = user.getLastLoginTimestamp();
        String prevStatus    = user.getLastLoginStatus();

        // Reset lockout counter and record this login
        frame.main.sqlite.resetFailedAttempts(username);
        frame.main.sqlite.updateLastLogin(username, currentTimestamp, "SUCCESS");

        // Re-fetch so sessionUser holds the fully up-to-date record
        User updatedUser = frame.main.sqlite.getUserByUsername(username);
        if (updatedUser == null) updatedUser = user;   // fallback; should never happen

        // Navigate — mainNav stores sessionUser and routes to the correct role panel
        frame.mainNav(updatedUser, prevTimestamp, prevStatus);
    }//GEN-LAST:event_loginBtnActionPerformed

    private void registerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerBtnActionPerformed
        frame.registerNav();
    }//GEN-LAST:event_registerBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginBtn;
    private javax.swing.JPasswordField passwordFld;
    private javax.swing.JButton registerBtn;
    private javax.swing.JTextField usernameFld;
    // End of variables declaration//GEN-END:variables
}
