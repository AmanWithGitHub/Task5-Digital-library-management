package digitallibrarymanagement.library.database;

import digitallibrarymanagement.library.model.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, category, quantity, available_quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setInt(4, book.getQuantity());
            pstmt.setInt(5, book.getAvailableQuantity());
            pstmt.executeUpdate();
            System.out.println("A new book was inserted successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategory(rs.getString("category"));
                book.setQuantity(rs.getInt("quantity"));
                book.setAvailableQuantity(rs.getInt("available_quantity"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all books: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }
    
    public void updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, category = ?, quantity = ?, available_quantity = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getCategory());
            pstmt.setInt(4, book.getQuantity());
            pstmt.setInt(5, book.getAvailableQuantity());
            pstmt.setInt(6, book.getId());
            pstmt.executeUpdate();
            System.out.println("Book with ID " + book.getId() + " was updated successfully!");
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book with ID " + bookId + " was deleted successfully!");
            } else {
                System.out.println("No book found with ID " + bookId + ".");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAO();
        System.out.println("--- Testing BookDAO CRUD Operations ---");
        
        // --- Add a new book for testing ---
        System.out.println("\n--- Testing addBook() ---");
        Book testBook = new Book();
        testBook.setTitle("Test Title");
        testBook.setAuthor("Test Author");
        testBook.setCategory("Test Category");
        testBook.setQuantity(10);
        testBook.setAvailableQuantity(10);
        bookDAO.addBook(testBook);
        
        // --- Get the book to get its auto-generated ID ---
        List<Book> allBooks = bookDAO.getAllBooks();
        if (allBooks.isEmpty()) {
            System.out.println("Failed to retrieve the book for testing.");
            return;
        }
        Book createdBook = allBooks.get(allBooks.size() - 1);
        System.out.println("Created book with ID: " + createdBook.getId());

        // --- Update the book ---
        System.out.println("\n--- Testing updateBook() ---");
        createdBook.setTitle("Updated Title");
        bookDAO.updateBook(createdBook);
        
        // --- Delete the book ---
        System.out.println("\n--- Testing deleteBook() ---");
        bookDAO.deleteBook(createdBook.getId());
        
        // --- Verify deletion ---
        System.out.println("\n--- Verifying deletion with getAllBooks() ---");
        List<Book> remainingBooks = bookDAO.getAllBooks();
        System.out.println("Number of books remaining: " + remainingBooks.size());
    }
}