# Booktracker Management Application

This project was made for the Introduction to Software Engineering assignment.

The program is a simple command-line Java application that works with a SQLite database using JDBC. It manages reading habit data for the Booktracker website. The database is created from the provided dataset and the program includes all required functionalities from the assignment.

## Technologies Used

- Java
- SQLite
- JDBC

## Project Structure

- `src/` contains the Java source files
- `data/booktracker.db` is the SQLite database
- `data/insert_data.sql` contains the SQL statements used to insert the initial data
- `dataset/reading_habits_dataset.xlsx` is the original dataset
- `lib/` contains the SQLite JDBC library

## Files

- `Main.java` handles the menu and user input
- `DatabaseManager.java` connects to the database and creates the tables
- `DataInitializer.java` reads the SQL file and inserts the initial data
- `BooktrackerService.java` contains the main database operations

## How to Run

1. Open the project in VS Code.
2. Make sure the SQLite JDBC `.jar` file is in the `lib` folder and added to the project libraries.
3. Run `Main.java`.
4. The program will create the database and insert the initial data if the database does not already exist.
5. Use the menu in the terminal to choose a functionality.

## Functionalities

The program supports the following functionalities:

1. Add a user to the database
2. Show all reading habit data for a certain user
3. Change the title of a book in the database
4. Delete a row from the `ReadingHabit` table
5. Show the mean age of the users
6. Show the total number of users that have read pages from a specific book
7. Show the total number of pages read by all users
8. Show the total number of users that have read more than one book
9. Add a `Name` column to the `User` table

## Example Outputs

Some example outputs from the program are:

- Mean age of users: `33.0`
- Total pages read by all users: `7210`
- Total users who have read more than one book: `30`

## Notes

- The program uses SQL queries for calculations such as `AVG`, `SUM`, and `COUNT`.
- The application is command-line based, so no graphical interface is included.
- The database is stored locally as a SQLite file.

## Author

This project was created for the PI 6 Introduction to Software Engineering assignment.
