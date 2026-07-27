# Security Implementation — Replication Prompt

**Purpose:** Paste the entire contents of this file as a prompt to Claude in the correct repo.  
It contains full working source for every file that was changed, ready to copy-paste.

---

## PROMPT TO PASTE

You are helping replicate a complete set of security features into a Java Swing desktop app
(NetBeans, SQLite via JDBC, MVC: Controller/, Model/, View/).

This is a CSSECDV (Secure Software Development) academic group project called "SECURITY Svcs".
Person 1 role = Authentication (these changes). Do NOT add authorization/RBAC (Person 2) or
logging to the logs table (Person 5).

**Role codes (do not change):**
- Administrator = 5, Manager = 4, Staff = 3, Client = 2, Disabled = 1

---

### STEP 0 — Add jBCrypt dependency

1. Copy `jbcrypt-0.4.jar` into the project root and into `dist/lib/jbcrypt-0.4.jar`.
2. In `nbproject/project.properties`, find `javac.classpath` and add:
   ```
   file.reference.jbcrypt-0.4.jar=jbcrypt-0.4.jar
   ```
   And include `${file.reference.jbcrypt-0.4.jar}` in both `javac.classpath` and `run.classpath`.
3. Optionally create `.vscode/settings.json`:
   ```json
   { "java.project.referencedLibraries": ["sqlite-jdbc-3.23.1.jar", "jbcrypt-0.4.jar"] }
   ```

---

### STEP 1 — Replace `src/Model/User.java` with this exact content:

```java
package Model;

public class User {
    private int id;
    private String username;
    private String password;
    private int role = 2;
    private int locked = 0;
    private int failedAttempts = 0;
    private String lastLoginTimestamp = "";
    private String lastLoginStatus = "";

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String password, int role, int locked) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.locked = locked;
    }

    public User(int id, String username, String password, int role, int locked,
                int failedAttempts, String lastLoginTimestamp, String lastLoginStatus) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.locked = locked;
        this.failedAttempts = failedAttempts;
        this.lastLoginTimestamp = (lastLoginTimestamp != null) ? lastLoginTimestamp : "";
        this.lastLoginStatus   = (lastLoginStatus   != null) ? lastLoginStatus   : "";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    public int getLocked() { return locked; }
    public void setLocked(int locked) { this.locked = locked; }

    public int getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }

    public String getLastLoginTimestamp() { return lastLoginTimestamp; }
    public void setLastLoginTimestamp(String lastLoginTimestamp) {
        this.lastLoginTimestamp = (lastLoginTimestamp != null) ? lastLoginTimestamp : "";
    }

    public String getLastLoginStatus() { return lastLoginStatus; }
    public void setLastLoginStatus(String lastLoginStatus) {
        this.lastLoginStatus = (lastLoginStatus != null) ? lastLoginStatus : "";
    }
}
```

---

### STEP 2 — Replace `src/Controller/SQLite.java` with this exact content:

```java
package Controller;

import Model.History;
import Model.Logs;
import Model.Product;
import Model.User;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

public class SQLite {

    public int DEBUG_MODE = 0;
    String driverURL = "jdbc:sqlite:" + "database.db";

    // -------------------------------------------------------------------------
    // DATABASE / TABLE CREATION
    // -------------------------------------------------------------------------

    public void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(driverURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database database.db created.");
            }
        } catch (Exception ex) {
            System.out.println("createNewDatabase error: " + ex.getMessage());
        }
    }

    public void createHistoryTable() {
        String sql = "CREATE TABLE IF NOT EXISTS history (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL,\n"
            + " name TEXT NOT NULL,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " timestamp TEXT NOT NULL\n"
            + ");";
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db created.");
        } catch (Exception ex) {
            System.out.println("createHistoryTable error: " + ex.getMessage());
        }
    }

    public void createLogsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS logs (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " event TEXT NOT NULL,\n"
            + " username TEXT NOT NULL,\n"
            + " desc TEXT NOT NULL,\n"
            + " timestamp TEXT NOT NULL\n"
            + ");";
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db created.");
        } catch (Exception ex) {
            System.out.println("createLogsTable error: " + ex.getMessage());
        }
    }

    public void createProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS product (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " name TEXT NOT NULL UNIQUE,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " price REAL DEFAULT 0.00\n"
            + ");";
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db created.");
        } catch (Exception ex) {
            System.out.println("createProductTable error: " + ex.getMessage());
        }
    }

    /** Creates the users table including the three new security columns. */
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL UNIQUE,\n"
            + " password TEXT NOT NULL,\n"
            + " role INTEGER DEFAULT 2,\n"
            + " locked INTEGER DEFAULT 0,\n"
            + " failed_attempts INTEGER DEFAULT 0,\n"
            + " last_login_timestamp TEXT,\n"
            + " last_login_status TEXT\n"
            + ");";
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db created.");
        } catch (Exception ex) {
            System.out.println("createUserTable error: " + ex.getMessage());
        }
    }

    /**
     * Adds new security columns to an existing users table.
     * Each ALTER TABLE is wrapped in its own try-catch; a "duplicate column"
     * error means the column already exists — safe to ignore.
     */
    public void migrateDatabase() {
        String[] migrations = {
            "ALTER TABLE users ADD COLUMN failed_attempts INTEGER DEFAULT 0",
            "ALTER TABLE users ADD COLUMN last_login_timestamp TEXT",
            "ALTER TABLE users ADD COLUMN last_login_status TEXT"
        };
        try (Connection conn = DriverManager.getConnection(driverURL)) {
            for (String sql : migrations) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                    System.out.println("Migration applied: " + sql);
                } catch (Exception ex) {
                    // Column already exists — not an error
                }
            }
        } catch (Exception ex) {
            System.out.println("migrateDatabase error: " + ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // DROP TABLES
    // -------------------------------------------------------------------------

    public void dropHistoryTable() {
        String sql = "DROP TABLE IF EXISTS history;";
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history dropped.");
        } catch (Exception ex) {
            System.out.println("dropHistoryTable error: " + ex.getMessage());
        }
    }

    public void dropLogsTable() {
        String sql = "DROP TABLE IF EXISTS logs;";
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs dropped.");
        } catch (Exception ex) {
            System.out.println("dropLogsTable error: " + ex.getMessage());
        }
    }

    public void dropProductTable() {
        String sql = "DROP TABLE IF EXISTS product;";
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product dropped.");
        } catch (Exception ex) {
            System.out.println("dropProductTable error: " + ex.getMessage());
        }
    }

    public void dropUserTable() {
        String sql = "DROP TABLE IF EXISTS users;";
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users dropped.");
        } catch (Exception ex) {
            System.out.println("dropUserTable error: " + ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // INSERT OPERATIONS  (all use PreparedStatement)
    // -------------------------------------------------------------------------

    public void addHistory(String username, String name, int stock, String timestamp) {
        String sql = "INSERT INTO history(username,name,stock,timestamp) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setInt(3, stock);
            pstmt.setString(4, timestamp);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("addHistory error: " + ex.getMessage());
        }
    }

    public void addLogs(String event, String username, String desc, String timestamp) {
        String sql = "INSERT INTO logs(event,username,desc,timestamp) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event);
            pstmt.setString(2, username);
            pstmt.setString(3, desc);
            pstmt.setString(4, timestamp);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("addLogs error: " + ex.getMessage());
        }
    }

    public void addProduct(String name, int stock, double price) {
        String sql = "INSERT INTO product(name,stock,price) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, stock);
            pstmt.setDouble(3, price);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("addProduct error: " + ex.getMessage());
        }
    }

    /** Registers a new user (role defaults to 2 / Client). Password is BCrypt-hashed. */
    public void addUser(String username, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String sql = "INSERT INTO users(username,password) VALUES(?,?)";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("addUser error: " + ex.getMessage());
        }
    }

    /** Registers a new user with an explicit role. Password is BCrypt-hashed. */
    public void addUser(String username, String password, int role) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String sql = "INSERT INTO users(username,password,role) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setInt(3, role);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("addUser(role) error: " + ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // SELECT OPERATIONS
    // -------------------------------------------------------------------------

    public ArrayList<History> getHistory() {
        String sql = "SELECT id, username, name, stock, timestamp FROM history";
        ArrayList<History> histories = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            System.out.println("getHistory error: " + ex.getMessage());
        }
        return histories;
    }

    public ArrayList<Logs> getLogs() {
        String sql = "SELECT id, event, username, desc, timestamp FROM logs";
        ArrayList<Logs> logs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                logs.add(new Logs(rs.getInt("id"),
                        rs.getString("event"),
                        rs.getString("username"),
                        rs.getString("desc"),
                        rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            System.out.println("getLogs error: " + ex.getMessage());
        }
        return logs;
    }

    public ArrayList<Product> getProduct() {
        String sql = "SELECT id, name, stock, price FROM product";
        ArrayList<Product> products = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getFloat("price")));
            }
        } catch (Exception ex) {
            System.out.println("getProduct error: " + ex.getMessage());
        }
        return products;
    }

    /** Returns a Product by name.  Uses PreparedStatement to prevent SQL injection. */
    public Product getProduct(String name) {
        String sql = "SELECT name, stock, price FROM product WHERE name=?";
        Product product = null;
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    product = new Product(rs.getString("name"),
                            rs.getInt("stock"),
                            rs.getFloat("price"));
                }
            }
        } catch (Exception ex) {
            System.out.println("getProduct(name) error: " + ex.getMessage());
        }
        return product;
    }

    /** Returns all users including the new security fields. */
    public ArrayList<User> getUsers() {
        String sql = "SELECT id, username, password, role, locked, "
                   + "failed_attempts, last_login_timestamp, last_login_status FROM users";
        ArrayList<User> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("role"),
                        rs.getInt("locked"),
                        rs.getInt("failed_attempts"),
                        rs.getString("last_login_timestamp"),
                        rs.getString("last_login_status")));
            }
        } catch (Exception ex) {
            System.out.println("getUsers error: " + ex.getMessage());
        }
        return users;
    }

    /**
     * Looks up a single user by username.
     * Uses PreparedStatement — safe against SQL injection.
     * Returns null if the user does not exist.
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT id, username, password, role, locked, "
                   + "failed_attempts, last_login_timestamp, last_login_status "
                   + "FROM users WHERE username=?";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getInt("role"),
                            rs.getInt("locked"),
                            rs.getInt("failed_attempts"),
                            rs.getString("last_login_timestamp"),
                            rs.getString("last_login_status"));
                }
            }
        } catch (Exception ex) {
            System.out.println("getUserByUsername error: " + ex.getMessage());
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // UPDATE OPERATIONS  (all use PreparedStatement)
    // -------------------------------------------------------------------------

    /** Stores a BCrypt-hashed password for the given user. */
    public void updatePassword(String username, String hashedPassword) {
        String sql = "UPDATE users SET password=? WHERE username=?";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("updatePassword error: " + ex.getMessage());
        }
    }

    /** Increments failed_attempts by 1 for the given user. */
    public void incrementFailedAttempts(String username) {
        String sql = "UPDATE users SET failed_attempts = failed_attempts + 1 WHERE username=?";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("incrementFailedAttempts error: " + ex.getMessage());
        }
    }

    /** Resets failed_attempts to 0 for the given user (called on successful login). */
    public void resetFailedAttempts(String username) {
        String sql = "UPDATE users SET failed_attempts=0 WHERE username=?";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("resetFailedAttempts error: " + ex.getMessage());
        }
    }

    /** Sets role=1 (Disabled) for the given user — used for automatic login lockout. */
    public void disableUser(String username) {
        String sql = "UPDATE users SET role=1 WHERE username=?";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("disableUser error: " + ex.getMessage());
        }
    }

    /** Records the timestamp and status (SUCCESS / FAILED) of the last login attempt. */
    public void updateLastLogin(String username, String timestamp, String status) {
        String sql = "UPDATE users SET last_login_timestamp=?, last_login_status=? WHERE username=?";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, timestamp);
            pstmt.setString(2, status);
            pstmt.setString(3, username);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("updateLastLogin error: " + ex.getMessage());
        }
    }

    /** Changes a user's role.  Used by admin EDIT ROLE action. */
    public void updateUserRole(String username, int role) {
        String sql = "UPDATE users SET role=? WHERE username=?";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, role);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("updateUserRole error: " + ex.getMessage());
        }
    }

    /**
     * Sets the locked column for a user.
     * Pass 1 to lock, 0 to unlock.
     * This is the manual admin lock — separate from the automatic role=1 disable.
     */
    public void setUserLocked(String username, int locked) {
        String sql = "UPDATE users SET locked=? WHERE username=?";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, locked);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("setUserLocked error: " + ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // DELETE OPERATIONS  (PreparedStatement)
    // -------------------------------------------------------------------------

    public void removeUser(String username) {
        String sql = "DELETE FROM users WHERE username=?";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            System.out.println("User " + username + " has been deleted.");
        } catch (Exception ex) {
            System.out.println("removeUser error: " + ex.getMessage());
        }
    }
}
```

---

### STEP 3 — Replace `src/Controller/Main.java` with this exact content:

```java
package Controller;

import View.Frame;

public class Main {

    public SQLite sqlite;

    public static void main(String[] args) {
        new Main().init();
    }

    public void init() {
        sqlite = new SQLite();

        // Ensure the DB and tables exist (safe to call on every run)
        sqlite.createNewDatabase();
        sqlite.createHistoryTable();
        sqlite.createLogsTable();
        sqlite.createProductTable();
        sqlite.createUserTable();

        // Add the three new security columns to existing databases.  No-ops on a fresh DB.
        sqlite.migrateDatabase();

        // ---------------------------------------------------------------
        // SAMPLE DATA — uncomment the block below ONCE to seed the DB,
        // then comment it out again (or it will throw UNIQUE constraint
        // errors on the second run).
        // ---------------------------------------------------------------
//        sqlite.dropHistoryTable();
//        sqlite.dropLogsTable();
//        sqlite.dropProductTable();
//        sqlite.dropUserTable();
//        sqlite.createHistoryTable();
//        sqlite.createLogsTable();
//        sqlite.createProductTable();
//        sqlite.createUserTable();
//        sqlite.migrateDatabase();
//
//        // Sample history
//        sqlite.addHistory("admin",   "Antivirus", 1, "2019-04-03 14:30:00");
//        sqlite.addHistory("manager", "Firewall",  1, "2019-04-03 14:30:01");
//        sqlite.addHistory("staff",   "Scanner",   1, "2019-04-03 14:30:02");
//
//        // Sample logs
//        sqlite.addLogs("NOTICE", "admin",   "User creation successful", "2019-04-03 14:30:00");
//        sqlite.addLogs("NOTICE", "manager", "User creation successful", "2019-04-03 14:30:01");
//        sqlite.addLogs("NOTICE", "admin",   "User creation successful", "2019-04-03 14:30:02");
//
//        // Sample products
//        sqlite.addProduct("Antivirus", 5,  500.0);
//        sqlite.addProduct("Firewall",  3, 1000.0);
//        sqlite.addProduct("Scanner",  10,  100.0);
//
//        // Sample users — passwords are BCrypt-hashed inside addUser()
//        sqlite.addUser("admin",   "qwerty1234A!", 5);   // Administrator
//        sqlite.addUser("manager", "qwerty1234A!", 4);   // Manager
//        sqlite.addUser("staff",   "qwerty1234A!", 3);   // Staff
//        sqlite.addUser("client1", "qwerty1234A!", 2);   // Client
//        sqlite.addUser("client2", "qwerty1234A!", 2);   // Client

        // Initialize the UI
        Frame frame = new Frame();
        frame.init(this);
    }
}
```

---

### STEP 4 — Replace `src/View/Login.java` with this exact content:

```java

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
```

---

### STEP 5 — Replace `src/View/Register.java` with this exact content:

```java

package View;

import javax.swing.JOptionPane;

public class Register extends javax.swing.JPanel {

    public Frame frame;

    public Register() {
        initComponents();
    }

    // -----------------------------------------------------------------------
    // Password & username policy helpers (also called from MgmtUser)
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
```

---

### STEP 6 — Apply these targeted changes to `src/View/Frame.java`

In `Frame.java`, make these 4 changes (do NOT rewrite the whole file — only touch what's listed):

**6a.** Add this field right after the class declaration (before the constructor):
```java
/** The user currently logged in. Set by Login on success; cleared on logout. */
public User sessionUser = null;
```
And add the import at the top: `import Model.User;`

**6b.** In `logoutBtnActionPerformed`, add `sessionUser = null;` as the first line:
```java
private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {
    sessionUser = null;
    frameView.show(Container, "loginPnl");
}
```

**6c.** Replace the existing `mainNav()` method (and any old version that takes no args) with:
```java
public void mainNav(User user, String prevTimestamp, String prevStatus) {
    sessionUser = user;
    frameView.show(Container, "homePnl");

    int role = user.getRole();
    if (role == 5) {
        adminHomePnl.updateLastLogin(prevTimestamp, prevStatus);
        adminHomePnl.showPnl("home");
        contentView.show(Content, "adminHomePnl");
    } else if (role == 4) {
        managerHomePnl.updateLastLogin(prevTimestamp, prevStatus);
        managerHomePnl.showPnl("home");
        contentView.show(Content, "managerHomePnl");
    } else if (role == 3) {
        staffHomePnl.updateLastLogin(prevTimestamp, prevStatus);
        staffHomePnl.showPnl("home");
        contentView.show(Content, "staffHomePnl");
    } else {
        clientHomePnl.updateLastLogin(prevTimestamp, prevStatus);
        clientHomePnl.showPnl("home");
        contentView.show(Content, "clientHomePnl");
    }
}
```

**6d.** In `init(Main controller)`, change all four `xxxHomePnl.init(main.sqlite)` calls to pass `this` as second argument:
```java
adminHomePnl.init(main.sqlite, this);
clientHomePnl.init(main.sqlite, this);
managerHomePnl.init(main.sqlite, this);
staffHomePnl.init(main.sqlite, this);
```

**6e.** Replace `registerAction` to accept 2 args (validation is now done in Register.java):
```java
public void registerAction(String username, String password) {
    main.sqlite.addUser(username, password);
}
```

---

### STEP 7 — Apply these targeted changes to `src/View/Home.java`

**7a.** Add a `private String welcomeText;` field.

**7b.** In the constructor, store `welcomeText`:
```java
public Home(String name, Color color) {
    initComponents();
    this.welcomeText = name;
    userLbl.setText(name);
    setBackground(color);
}
```

**7c.** Add this new method after the constructor:
```java
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
```

---

### STEP 8 — Apply these IDENTICAL changes to all four home panels:
`AdminHome.java`, `ManagerHome.java`, `StaffHome.java`, `ClientHome.java`

For each one:

**8a.** Add `private Home home;` as a field.

**8b.** Change `init(SQLite sqlite)` to `init(SQLite sqlite, Frame frame)` and inside it:
- Change `mgmtUser = new MgmtUser(sqlite)` → `mgmtUser = new MgmtUser(sqlite, frame)`
- After creating MgmtUser, create the Home panel and add it to Content:
  ```java
  home = new Home("WELCOME ADMIN!", new java.awt.Color(51, 153, 255));  // use role-appropriate label/color
  Content.add(home, "home");
  ```
  The existing panels (mgmtUser, mgmtHistory, etc.) are still added the same way.

**8c.** Add this method:
```java
public void updateLastLogin(String lastLoginTimestamp, String lastLoginStatus) {
    if (home != null) {
        home.setLastLoginInfo(lastLoginTimestamp, lastLoginStatus);
    }
}
```

**Role-specific Home labels and colors:**
| Panel | Label | Color |
|---|---|---|
| AdminHome | `"WELCOME ADMIN!"` | `new java.awt.Color(51, 153, 255)` |
| ManagerHome | `"WELCOME MANAGER!"` | `new java.awt.Color(51, 204, 51)` (or whatever is in original) |
| StaffHome | `"WELCOME STAFF!"` | (original color) |
| ClientHome | `"WELCOME CLIENT!"` | (original color) |

Check the original Home construction in each file to preserve the exact label text and color.

---

### STEP 9 — Replace `src/View/MgmtUser.java` with this exact content:

```java
package View;

import Controller.SQLite;
import Model.User;
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

    public MgmtUser(SQLite sqlite, Frame frame) {
        initComponents();
        this.sqlite = sqlite;
        this.frame  = frame;
        tableModel  = (DefaultTableModel) table.getModel();
        table.getTableHeader().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
    }

    public void init() {
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
            sqlite.updateUserRole(targetUser, newRole);
            init();
        }
    }//GEN-LAST:event_editRoleBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        if (table.getSelectedRow() < 0) return;

        String targetUser = (String) tableModel.getValueAt(table.getSelectedRow(), 0);

        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete " + targetUser + "?",
                "DELETE USER", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        if (!reAuthenticate()) return;

        sqlite.removeUser(targetUser);
        init();
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void lockBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lockBtnActionPerformed
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
        init();
    }//GEN-LAST:event_lockBtnActionPerformed

    private void chgpassBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chgpassBtnActionPerformed
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
                    "Password must be 8–64 characters and contain uppercase, lowercase, digit, and special character.",
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
        JOptionPane.showMessageDialog(this,
                "Password updated successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        init();
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
```

---

### STEP 10 — First-run seeding

1. Delete any existing `database.db` in the project root.
2. In `Main.java`, uncomment the seeding block.
3. Run the project once — it will create the DB and seed all sample users with BCrypt-hashed passwords.
4. Re-comment the seeding block.
5. Rebuild and run normally.

**Sample credentials (all use password `qwerty1234A!`):**
| Username | Role |
|---|---|
| admin | 5 (Administrator) |
| manager | 4 (Manager) |
| staff | 3 (Staff) |
| client1 | 2 (Client) |
| client2 | 2 (Client) |

---

### Architecture notes (for context, do not change)

- BCrypt work factor: 12
- Timing defence: `DUMMY_HASH` computed at class load — prevents username enumeration via response time
- Last login display: `prevTimestamp`/`prevStatus` captured BEFORE writing new login — shows PREVIOUS session
- Session: `Frame.sessionUser` — set on login, cleared on logout
- Disabled vs Locked: `disableUser()` sets `role=1` (blocks login). `setUserLocked()` toggles `locked` column (admin manual action, separate mechanism). Login only checks `role==1`.
- Re-auth scope: all 4 MgmtUser buttons (Edit Role, Delete, Lock/Unlock, Change Pass)
- SQL injection prevention: every method taking user input uses `PreparedStatement`
