package Model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Logs {

    private int id;
    private String event;
    private String username;
    private String desc;  // keep as-is
    private Timestamp timestamp;
    private String level; // new field

    private static final SimpleDateFormat DATE_FORMAT_NO_MILLIS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT_WITH_MILLIS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public Logs(int id, String event, String username, String desc, String timestamp) {
    this(id, event, username, desc, timestamp, "INFO");
    }

    // Constructor with default level INFO
    public Logs(String event, String username, String desc) {
        this(event, username, desc, "INFO");
    }

    // Constructor with specified level
    public Logs(String event, String username, String desc, String level) {
        this.event = event;
        this.username = username != null ? username : "NONE";
        this.desc = desc;
        this.level = level != null ? level : "INFO";
        this.timestamp = new Timestamp(new Date().getTime());
    }

    // Constructor with ID and timestamp string (for DB)
    public Logs(int id, String event, String username, String desc, String timestamp, String level) {
        this.id = id;
        this.event = event;
        this.username = username != null ? username : "NONE";
        this.desc = desc;
        this.timestamp = parseTimestamp(timestamp);
        this.level = level != null ? level : "INFO";
    }

    private Timestamp parseTimestamp(String timestamp) {
        try {
            if (timestamp != null && timestamp.contains(".")) {
                return new Timestamp(DATE_FORMAT_WITH_MILLIS.parse(timestamp).getTime());
            }
            return new Timestamp(DATE_FORMAT_NO_MILLIS.parse(timestamp).getTime());
        } catch (ParseException ex) {
            Logger.getLogger(Logs.class.getName()).log(Level.WARNING, "Timestamp parse failed, using current time", ex);
            return new Timestamp(new Date().getTime());
        }
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}