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
