import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooktrackerService {
    private final DatabaseManager dbManager;

    public BooktrackerService() {
        dbManager = new DatabaseManager();
    }

    public void addUser(int userID, int age, String gender, String name) {
        String sql = "INSERT INTO User (userID, age, gender, Name) VALUES (?, ?, ?, ?);";

        try (Connection conn = dbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);
            pstmt.setInt(2, age);
            pstmt.setString(3, gender);
            pstmt.setString(4, name);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User added successfully.");
            } else {
                System.out.println("User could not be added.");
            }

        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    public void showReadingHabitsForUser(int userID) {
        String sql = """
                SELECT u.userID, u.Name, u.age, u.gender,
                       r.habitID, r.book, r.pagesRead, r.submissionMoment
                FROM User u
                JOIN ReadingHabit r ON u.userID = r.userID
                WHERE u.userID = ?;
                """;

        try (Connection conn = dbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("------------------------------");
                System.out.println("User ID: " + rs.getInt("userID"));
                System.out.println("Name: " + rs.getString("Name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Habit ID: " + rs.getInt("habitID"));
                System.out.println("Book: " + rs.getString("book"));
                System.out.println("Pages Read: " + rs.getInt("pagesRead"));
                System.out.println("Submission Moment: " + rs.getString("submissionMoment"));
            }

            if (!found) {
                System.out.println("No reading habits found for this user.");
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving reading habits: " + e.getMessage());
        }
    }

    public void changeBookTitle(String oldTitle, String newTitle) {
        String sql = "UPDATE ReadingHabit SET book = ? WHERE book = ?;";

        try (Connection conn = dbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newTitle);
            pstmt.setString(2, oldTitle);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book title updated successfully.");
                System.out.println("Rows updated: " + rowsAffected);
            } else {
                System.out.println("No matching book title found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating book title: " + e.getMessage());
        }
    }

    public void deleteReadingHabit(int habitID) {
        String sql = "DELETE FROM ReadingHabit WHERE habitID = ?;";

        try (Connection conn = dbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, habitID);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reading habit deleted successfully.");
            } else {
                System.out.println("No record found with this habit ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting reading habit: " + e.getMessage());
        }
    }

    public void getMeanAge() {
        String sql = "SELECT AVG(age) AS mean_age FROM User;";

        try (Connection conn = dbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                System.out.println("Mean age of users: " + rs.getDouble("mean_age"));
            }

        } catch (SQLException e) {
            System.out.println("Error calculating mean age: " + e.getMessage());
        }
    }

    public void getTotalUsersForSpecificBook(String bookTitle) {
        String sql = "SELECT COUNT(DISTINCT userID) AS total_users FROM ReadingHabit WHERE book = ?;";

        try (Connection conn = dbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookTitle);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Total users who read pages from this book: " + rs.getInt("total_users"));
            }

        } catch (SQLException e) {
            System.out.println("Error counting users for book: " + e.getMessage());
        }
    }

    public void getTotalPagesReadByAllUsers() {
        String sql = "SELECT SUM(pagesRead) AS total_pages FROM ReadingHabit;";

        try (Connection conn = dbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                System.out.println("Total pages read by all users: " + rs.getInt("total_pages"));
            }

        } catch (SQLException e) {
            System.out.println("Error calculating total pages: " + e.getMessage());
        }
    }

    public void getUsersWhoReadMoreThanOneBook() {
        String sql = """
                SELECT COUNT(*) AS total_users
                FROM (
                    SELECT userID
                    FROM ReadingHabit
                    GROUP BY userID
                    HAVING COUNT(DISTINCT book) > 1
                ) AS multi_book_users;
                """;

        try (Connection conn = dbManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                System.out.println("Total users who have read more than one book: " + rs.getInt("total_users"));
            }

        } catch (SQLException e) {
            System.out.println("Error counting users who read more than one book: " + e.getMessage());
        }
    }

    public void addNameColumn() {
        dbManager.addNameColumnToUserTable();
    }
}
