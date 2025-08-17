package digitallibrarymanagement.library.database;

import digitallibrarymanagement.library.model.Loan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoanDAO {

    public void issueBook(Loan loan) {
        String sql = "INSERT INTO loans (book_id, user_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loan.getBookId());
            pstmt.setInt(2, loan.getUserId());
            pstmt.setDate(3, new java.sql.Date(loan.getBorrowDate().getTime()));
            pstmt.setDate(4, new java.sql.Date(loan.getDueDate().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setUserId(rs.getInt("user_id"));
                loan.setBorrowDate(rs.getDate("borrow_date"));
                loan.setReturnDate(rs.getDate("return_date"));
                loan.setDueDate(rs.getDate("due_date"));
                loan.setFineAmount(rs.getDouble("fine_amount"));
                loan.setReturned(rs.getBoolean("is_returned"));
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }
    
    public List<Loan> getLoansByUserId(int userId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Loan loan = new Loan();
                    loan.setId(rs.getInt("id"));
                    loan.setBookId(rs.getInt("book_id"));
                    loan.setUserId(rs.getInt("user_id"));
                    loan.setBorrowDate(rs.getDate("borrow_date"));
                    loan.setReturnDate(rs.getDate("return_date"));
                    loan.setDueDate(rs.getDate("due_date"));
                    loan.setFineAmount(rs.getDouble("fine_amount"));
                    loan.setReturned(rs.getBoolean("is_returned"));
                    loans.add(loan);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public void returnBook(int loanId) {
        String sql = "UPDATE loans SET return_date = ?, is_returned = ?, fine_amount = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Logic to calculate fine would go here, for now we'll set it to 0
            double fine = 0.0;
            
            pstmt.setDate(1, new java.sql.Date(new Date().getTime()));
            pstmt.setBoolean(2, true);
            pstmt.setDouble(3, fine);
            pstmt.setInt(4, loanId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}