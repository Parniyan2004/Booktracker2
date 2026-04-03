import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataInitializer {
    private final DatabaseManager dbManager;

    public DataInitializer() {
        dbManager = new DatabaseManager();
    }

    public void insertDataFromSqlFile() {
        Path sqlFilePath = Path.of("data", "insert_data.sql");

        String checkUserSql = "SELECT COUNT(*) FROM User;";
        String checkHabitSql = "SELECT COUNT(*) FROM ReadingHabit;";

        try (Connection conn = dbManager.connect();
             Statement stmt = conn.createStatement()) {

            ResultSet userResult = stmt.executeQuery(checkUserSql);
            int userCount = userResult.next() ? userResult.getInt(1) : 0;
            userResult.close();

            ResultSet habitResult = stmt.executeQuery(checkHabitSql);
            int habitCount = habitResult.next() ? habitResult.getInt(1) : 0;
            habitResult.close();

            if (userCount > 0 || habitCount > 0) {
                System.out.println("Initial data already exists.");
                return;
            }

            String sqlContent = Files.readString(sqlFilePath);

            // remove comment lines
            StringBuilder cleanedSql = new StringBuilder();
            String[] lines = sqlContent.split("\\R");

            for (String line : lines) {
                String trimmedLine = line.trim();
                if (!trimmedLine.startsWith("--") && !trimmedLine.isEmpty()) {
                    cleanedSql.append(line).append("\n");
                }
            }

            conn.setAutoCommit(false);

            String[] commands = cleanedSql.toString().split(";");

            for (String command : commands) {
                String trimmedCommand = command.trim();

                if (!trimmedCommand.isEmpty()
                        && !trimmedCommand.equalsIgnoreCase("BEGIN TRANSACTION")
                        && !trimmedCommand.equalsIgnoreCase("COMMIT")) {
                    stmt.executeUpdate(trimmedCommand);
                }
            }

            conn.commit();
            conn.setAutoCommit(true);

            System.out.println("Initial data inserted successfully.");

        } catch (SQLException e) {
            System.out.println("Error inserting initial data: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error reading SQL file: " + e.getMessage());
        }
    }
}
