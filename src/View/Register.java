
package View;

import javax.swing.JOptionPane;

public class Register extends javax.swing.JPanel {

    public Frame frame;

    public Register() {
        initComponents();
    }

    // -----------------------------------------------------------------------
    // Password & username policy helpers (also called from Login / MgmtUser)
    // -----------------------------------------------------------------------

    /**
     * Returns true if the password meets all policy requirements:
     *  - 8–64 characters
     *  - at least one uppercase letter
     *  - at least one lowercase letter
     *  - at least one digit
     *  - at least one special character from the allowed set
     */
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        int len = password.length();
        if (len < 8 || len > 64) return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        String specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?`~";
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))        hasUpper   = true;
            else if (Character.isLowerCase(c))   hasLower   = true;
            else if (Character.isDigit(c))       hasDigit   = true;
            else if (specialChars.indexOf(c) >= 0) hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    /**
     * Returns true if the username is valid:
     *  - 3–30 characters
     *  - only alphanumeric characters and underscores [a-zA-Z0-9_]
     */
    public static boolean isValidUsername(String username) {
        if (username == null) return false;
        return username.matches("[a-zA-Z0-9_]{3,30}");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        registerBtn = new javax.swing.JButton();
        passwordFld = new javax.swing.JPasswordField();
        usernameFld = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        confpassFld = new javax.swing.JPasswordField();
        backBtn = new javax.swing.JButton();

        registerBtn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        registerBtn.setText("REGISTER");
        registerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerBtnActionPerformed(evt);
            }
        });

        passwordFld.setBackground(new java.awt.Color(240, 240, 240));
        passwordFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        passwordFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passwordFld.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "PASSWORD", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        usernameFld.setBackground(new java.awt.Color(240, 240, 240));
        usernameFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        usernameFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        usernameFld.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "USERNAME", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SECURITY Svcs");
        jLabel1.setToolTipText("");

        confpassFld.setBackground(new java.awt.Color(240, 240, 240));
        confpassFld.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        confpassFld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        confpassFld.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "CONFIRM PASSWORD", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        backBtn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        backBtn.setText("<Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(200, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(usernameFld)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(passwordFld, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(confpassFld, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(200, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(registerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backBtn)
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(usernameFld, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordFld, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(confpassFld, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(registerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void registerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerBtnActionPerformed
        String username = usernameFld.getText().trim();
        String password = new String(passwordFld.getPassword());
        String confpass = new String(confpassFld.getPassword());

        // --- Username validation ---
        if (!isValidUsername(username)) {
            JOptionPane.showMessageDialog(this,
                "Username must be 3–30 characters and contain only letters, digits, or underscores.",
                "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Duplicate username check ---
        if (frame.main.sqlite.getUserByUsername(username) != null) {
            JOptionPane.showMessageDialog(this,
                "That username is already taken. Please choose another.",
                "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Password policy ---
        if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(this,
                "Password must be 8–64 characters and contain uppercase, lowercase, digit, and special character.",
                "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Confirm password match ---
        if (!password.equals(confpass)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match.",
                "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- All checks passed — create account ---
        frame.registerAction(username, password);
        JOptionPane.showMessageDialog(this,
            "Account created successfully. Please log in.",
            "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
        passwordFld.setText("");
        confpassFld.setText("");
        usernameFld.setText("");
        frame.loginNav();
    }//GEN-LAST:event_registerBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        passwordFld.setText("");
        confpassFld.setText("");
        frame.loginNav();
    }//GEN-LAST:event_backBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JPasswordField confpassFld;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPasswordField passwordFld;
    private javax.swing.JButton registerBtn;
    private javax.swing.JTextField usernameFld;
    // End of variables declaration//GEN-END:variables
}
