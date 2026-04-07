package Controller;

import View.Frame;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {

    public SQLite sqlite;

    public static void main(String[] args) {
        new Main().init();
    }

    public void init() {
        // Configure logging: route all output to app.log, remove console handlers
        try {
            Logger rootLogger = Logger.getLogger("");
            for (Handler h : rootLogger.getHandlers()) {
                rootLogger.removeHandler(h);
            }
            FileHandler fh = new FileHandler("app.log", 50000, 1, true);
            fh.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fh);
            rootLogger.setLevel(Level.ALL);
        } catch (Exception ex) {
            // If logging setup fails, continue silently
        }

        sqlite = new SQLite();

        // Ensure the DB and tables exist (safe to call on every run)
        sqlite.createNewDatabase();
        sqlite.createHistoryTable();
        sqlite.createLogsTable();
        sqlite.createProductTable();
        sqlite.createUserTable();

        // Add the three new security columns to existing databases that
        // were created before this schema update.  No-ops on a fresh DB.
        sqlite.migrateDatabase();

        // ---------------------------------------------------------------
        // SAMPLE DATA — uncomment the block below ONCE to seed the DB,
        // then comment it out again (or it will throw UNIQUE constraint
        // errors on the second run).
        //
        // All passwords are now BCrypt-hashed automatically by addUser().
        // ---------------------------------------------------------------
        sqlite.dropHistoryTable();
        sqlite.dropLogsTable();
        sqlite.dropProductTable();
        sqlite.dropUserTable();
        sqlite.createHistoryTable();
        sqlite.createLogsTable();
        sqlite.createProductTable();
        sqlite.createUserTable();
        sqlite.migrateDatabase();

        // Sample history
        sqlite.addHistory("admin",   "Antivirus", 1, "2019-04-03 14:30:00");
        sqlite.addHistory("manager", "Firewall",  1, "2019-04-03 14:30:01");
        sqlite.addHistory("staff",   "Scanner",   1, "2019-04-03 14:30:02");

        // Sample logs
        sqlite.addLogs("NOTICE", "admin",   "User creation successful", "2019-04-03 14:30:00");
        sqlite.addLogs("NOTICE", "manager", "User creation successful", "2019-04-03 14:30:01");
        sqlite.addLogs("NOTICE", "admin",   "User creation successful", "2019-04-03 14:30:02");

        // Sample products
        sqlite.addProduct("Antivirus", 5,  500.0);
        sqlite.addProduct("Firewall",  3, 1000.0);
        sqlite.addProduct("Scanner",  10,  100.0);

        // Sample users — passwords are BCrypt-hashed inside addUser()
        sqlite.addUser("admin",   "qwerty1234A!", 5);   // Administrator
        sqlite.addUser("manager", "qwerty1234A!", 4);   // Manager
        sqlite.addUser("staff",   "qwerty1234A!", 3);   // Staff
        sqlite.addUser("client1", "qwerty1234A!", 2);   // Client
        sqlite.addUser("client2", "qwerty1234A!", 2);   // Client

        // Initialize the UI
        Frame frame = new Frame();
        frame.init(this);
    }
}
