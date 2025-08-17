package digitallibrarymanagement.library.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database credentials and URL - you should change these to match your setup
    private static final String URL = "jdbc:mysql://localhost:3306/digital_library_db";
    private static final String USER = "root";       // Your MySQL username
    private static final String PASSWORD = "88023@"; // Your MySQL password

    // Method to get a connection to the database
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the MySQL JDBC driver
            // This is crucial for the DriverManager to know which driver to use
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Make sure the JAR file is in your Libraries.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }
        return connection;
    }

    // Method to close the connection
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Failed to close the database connection.");
                e.printStackTrace();
            }
        }
    }

    // A simple main method to test the connection
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            // If the connection is successful, you can perform database operations here.
            // For now, we'll just close it.
            closeConnection(conn);
        }
    }
}