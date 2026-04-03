import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();
        DataInitializer dataInitializer = new DataInitializer();
        BooktrackerService service = new BooktrackerService();
        Scanner scanner = new Scanner(System.in);

        dbManager.createUserTable();
        dbManager.createReadingHabitTable();
        dbManager.addNameColumnToUserTable();
        dataInitializer.insertDataFromSqlFile();

        int choice;

        do {
            System.out.println("\n=== Booktracker Menu ===");
            System.out.println("1. Add a user");
            System.out.println("2. Show reading habit data for a user");
            System.out.println("3. Change a book title");
            System.out.println("4. Delete a reading habit record");
            System.out.println("5. Show mean age of users");
            System.out.println("6. Show number of users for a specific book");
            System.out.println("7. Show total number of pages read");
            System.out.println("8. Show number of users who read more than one book");
            System.out.println("9. Add Name column to User table");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter user ID: ");
                    int userID = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter age: ");
                    int age = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter gender (m/f): ");
                    String gender = scanner.nextLine();

                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();

                    service.addUser(userID, age, gender, name);
                    break;

                case 2:
                    System.out.print("Enter user ID: ");
                    int searchUserID = Integer.parseInt(scanner.nextLine());
                    service.showReadingHabitsForUser(searchUserID);
                    break;

                case 3:
                    System.out.print("Enter old book title: ");
                    String oldTitle = scanner.nextLine();

                    System.out.print("Enter new book title: ");
                    String newTitle = scanner.nextLine();

                    service.changeBookTitle(oldTitle, newTitle);
                    break;

                case 4:
                    System.out.print("Enter habit ID to delete: ");
                    int habitID = Integer.parseInt(scanner.nextLine());
                    service.deleteReadingHabit(habitID);
                    break;

                case 5:
                    service.getMeanAge();
                    break;

                case 6:
                    System.out.print("Enter book title: ");
                    String bookTitle = scanner.nextLine();
                    service.getTotalUsersForSpecificBook(bookTitle);
                    break;

                case 7:
                    service.getTotalPagesReadByAllUsers();
                    break;

                case 8:
                    service.getUsersWhoReadMoreThanOneBook();
                    break;

                case 9:
                    service.addNameColumn();
                    break;

                case 0:
                    System.out.println("Program closed.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 0);

        scanner.close();
    }
}
