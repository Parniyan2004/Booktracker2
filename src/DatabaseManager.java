import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:data/booktracker.db";

    public Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            return conn;
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public void createUserTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS User (
                    userID INTEGER PRIMARY KEY,
                    age INTEGER NOT NULL,
                    gender TEXT NOT NULL
                );
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("User table created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating User table: " + e.getMessage());
        }
    }

    public void createReadingHabitTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS ReadingHabit (
                    habitID INTEGER PRIMARY KEY,
                    userID INTEGER NOT NULL,
                    pagesRead INTEGER NOT NULL,
                    book TEXT NOT NULL,
                    submissionMoment TEXT NOT NULL,
                    FOREIGN KEY (userID) REFERENCES User(userID)
                );
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("ReadingHabit table created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating ReadingHabit table: " + e.getMessage());
        }
    }

    public void addNameColumnToUserTable() {
        if (nameColumnExists()) {
            System.out.println("Name column already exists in User table.");
            return;
        }

        String sql = "ALTER TABLE User ADD COLUMN Name TEXT;";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Name column added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding Name column: " + e.getMessage());
        }
    }

    private boolean nameColumnExists() {
        String sql = "PRAGMA table_info(User);";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                if ("Name".equalsIgnoreCase(rs.getString("name"))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking User table columns: " + e.getMessage());
        }

        return false;
    }
}
