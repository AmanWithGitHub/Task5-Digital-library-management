package digitallibrarymanagement.library.model;

import java.util.Date;

public class Loan {

    private int id;
    private int bookId;
    private int userId;
    private Date borrowDate;
    private Date returnDate;
    private Date dueDate;
    private double fineAmount;
    private boolean isReturned;

    public Loan() {
    }

    // Getters and Setters for all fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Date getBorrowDate() { return borrowDate; }
    public void setBorrowDate(Date borrowDate) { this.borrowDate = borrowDate; }
    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public double getFineAmount() { return fineAmount; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }
    public boolean isReturned() { return isReturned; }
    public void setReturned(boolean isReturned) { this.isReturned = isReturned; }
}