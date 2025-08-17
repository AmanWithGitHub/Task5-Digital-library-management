package digitallibrarymanagement.library.database;

import digitallibrarymanagement.library.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void addUser(User user) {
        String sql = "INSERT INTO users (username, password, role, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getEmail());
            pstmt.executeUpdate();
            System.out.println("New user added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    user.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user: " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    public void updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getEmail());
            pstmt.setInt(5, user.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User with ID " + user.getId() + " was updated successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User with ID " + userId + " was deleted successfully!");
            } else {
                System.out.println("No user found with ID " + userId + ".");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        System.out.println("--- Testing UserDAO CRUD Operations ---");
        
        // --- Add a new user for testing ---
        System.out.println("\n--- Testing addUser() ---");
        User testUser = new User();
        testUser.setUsername("test_user_temp");
        testUser.setPassword("password123");
        testUser.setRole("user");
        testUser.setEmail("test@temp.com");
        userDAO.addUser(testUser);
        
        List<User> allUsers = userDAO.getAllUsers();
        if (allUsers.isEmpty()) {
            System.out.println("Failed to retrieve the user for testing.");
            return;
        }
        User createdUser = allUsers.get(allUsers.size() - 1);
        System.out.println("Created user with ID: " + createdUser.getId());
        
        // --- Update the user ---
        System.out.println("\n--- Testing updateUser() ---");
        createdUser.setEmail("updated_email@temp.com");
        userDAO.updateUser(createdUser);
        
        // --- Delete the user ---
        System.out.println("\n--- Testing deleteUser() ---");
        userDAO.deleteUser(createdUser.getId());
        
        // --- Verify deletion ---
        System.out.println("\n--- Verifying deletion with getAllUsers() ---");
        List<User> remainingUsers = userDAO.getAllUsers();
        System.out.println("Number of users remaining: " + remainingUsers.size());
    }
}