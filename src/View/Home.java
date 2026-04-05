package View;

import java.awt.Color;

public class Home extends javax.swing.JPanel {

    private String welcomeText;

    public Home(String name, Color color) {
        initComponents();
        this.welcomeText = name;
        userLbl.setText(name);
        setBackground(color);
    }

    /**
     * Updates the welcome panel to show the previous session's last-login info.
     * Uses HTML inside the existing JLabel so no layout changes are needed.
     *
     * @param lastLoginTimestamp  timestamp from the previous session, or null/empty for first login
     * @param lastLoginStatus     "SUCCESS" or "FAILED", or null/empty for first login
     */
    public void setLastLoginInfo(String lastLoginTimestamp, String lastLoginStatus) {
        String info;
        if (lastLoginTimestamp == null || lastLoginTimestamp.trim().isEmpty()) {
            info = "Welcome! This is your first login.";
        } else {
            info = "Last login: " + lastLoginTimestamp + "  \u2014  " + lastLoginStatus;
        }
        userLbl.setText("<html><center>"
                + welcomeText
                + "<br><br><font size='4'>" + info + "</font>"
                + "</center></html>");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        userLbl = new javax.swing.JLabel();

        userLbl.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        userLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userLbl.setText("WELCOME USER!");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(userLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(userLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel userLbl;
    // End of variables declaration//GEN-END:variables
}
