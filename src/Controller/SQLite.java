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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class SQLite {

    private static final Logger LOGGER = Logger.getLogger(SQLite.class.getName());

    public int DEBUG_MODE = 0;
    String driverURL = "jdbc:sqlite:" + "database.db";

    // -------------------------------------------------------------------------
    // DATABASE / TABLE CREATION
    // -------------------------------------------------------------------------

    public void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(driverURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                LOGGER.log(Level.INFO, "Database initialized");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.INFO, "Table initialized");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.INFO, "Table initialized");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.INFO, "Table initialized");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.INFO, "Table initialized");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
    }

    /**
     * Adds new security columns to an existing users table that was created
     * before the schema update.  SQLite does not support IF NOT EXISTS in
     * ALTER TABLE, so each attempt is wrapped in its own try-catch; a
     * "duplicate column" error means the column already exists — safe to ignore.
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
                    LOGGER.log(Level.INFO, "Migration applied");
                } catch (Exception ex) {
                    LOGGER.log(Level.FINE, "Column already exists", ex);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.INFO, "Table dropped");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
    }

    public void dropLogsTable() {
        String sql = "DROP TABLE IF EXISTS logs;";
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            LOGGER.log(Level.INFO, "Table dropped");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
    }

    public void dropProductTable() {
        String sql = "DROP TABLE IF EXISTS product;";
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            LOGGER.log(Level.INFO, "Table dropped");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
    }

    public void dropUserTable() {
        String sql = "DROP TABLE IF EXISTS users;";
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            LOGGER.log(Level.INFO, "Table dropped");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
    }

    // -------------------------------------------------------------------------
    // SELECT OPERATIONS
    // -------------------------------------------------------------------------

    public ArrayList<History> getHistory() {
        String sql = "SELECT id, username, name, stock, timestamp FROM history";
        ArrayList<History> histories = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
        return histories;
    }

    /** Returns purchase history for a single username only. */
    public ArrayList<History> getHistoryByUsername(String username) {
        String sql = "SELECT id, username, name, stock, timestamp FROM history WHERE username=?";
        ArrayList<History> histories = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    histories.add(new History(rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("name"),
                            rs.getInt("stock"),
                            rs.getString("timestamp")));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
        return histories;
    }

    public ArrayList<Logs> getLogs() {
        String sql = "SELECT id, event, username, desc, timestamp FROM logs";
        ArrayList<Logs> logs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                logs.add(new Logs(rs.getInt("id"),
                        rs.getString("event"),
                        rs.getString("username"),
                        rs.getString("desc"),
                        rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
        return logs;
    }

    public void clearLogs() {
        String sql = "DELETE FROM logs";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
    }

    public ArrayList<Product> getProduct() {
        String sql = "SELECT id, name, stock, price FROM product";
        ArrayList<Product> products = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getFloat("price")));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
    }

    /** Updates product details identified by oldName. */
    public void updateProduct(String oldName, String newName, int stock, double price) {
        String sql = "UPDATE product SET name=?, stock=?, price=? WHERE name=?";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, stock);
            pstmt.setDouble(3, price);
            pstmt.setString(4, oldName);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
    }

    /**
     * Attempts to purchase product quantity atomically.
     * Returns true on success, false if product not found or insufficient stock.
     */
    public boolean purchaseProduct(String name, int quantity) {
        String selectSql = "SELECT stock FROM product WHERE name=?";
        String updateSql = "UPDATE product SET stock = stock - ? WHERE name=?";
        try (Connection conn = DriverManager.getConnection(driverURL)) {
            conn.setAutoCommit(false);
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setString(1, name);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    int currentStock = rs.getInt("stock");
                    if (quantity <= 0 || quantity > currentStock) {
                        conn.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, quantity);
                updateStmt.setString(2, name);
                updateStmt.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
        return false;
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
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
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
            LOGGER.log(Level.INFO, "User removed");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
    }

    public void removeProduct(String name) {
        String sql = "DELETE FROM product WHERE name=?";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Database operation failed", ex);
        }
    }
}
