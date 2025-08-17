package digitallibrarymanagement.library.view;

import digitallibrarymanagement.library.database.BookDAO;
import digitallibrarymanagement.library.database.LoanDAO;
import digitallibrarymanagement.library.database.UserDAO;
import digitallibrarymanagement.library.model.Book;
import digitallibrarymanagement.library.model.Loan;
import digitallibrarymanagement.library.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AdminPanel extends JPanel {
    private BookDAO bookDAO;
    private UserDAO userDAO;
    private LoanDAO loanDAO;

    private JTextField titleField, authorField, categoryField, quantityField;
    private JButton addBookBtn, updateBookBtn, deleteBookBtn;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private int selectedBookId = -1;

    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton addUserBtn, updateUserBtn, deleteUserBtn;
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private int selectedUserId = -1;
    
    // Loan Management components
    private JComboBox<String> bookComboBox, userComboBox;
    private JButton issueBookBtn, returnBookBtn;
    private JTable loanTable;
    private DefaultTableModel loanTableModel;

    public AdminPanel() {
        bookDAO = new BookDAO();
        userDAO = new UserDAO();
        loanDAO = new LoanDAO();
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel bookManagementPanel = createBookManagementPanel();
        tabbedPane.addTab("Book Management", null, bookManagementPanel, "Manage books in the library");
        
        JPanel userManagementPanel = createUserManagementPanel();
        tabbedPane.addTab("User Management", null, userManagementPanel, "Manage library users");

        JPanel loanManagementPanel = createLoanManagementPanel();
        tabbedPane.addTab("Loan Management", null, loanManagementPanel, "Manage book loans and fines");

        add(tabbedPane, BorderLayout.CENTER);
        
        loadBookData();
        loadUserData();
        loadLoanData();
    }
    
    // --- Book Management Panel ---
    private JPanel createBookManagementPanel() {
        JPanel bookPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));
        formPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        authorField = new JTextField();
        formPanel.add(authorField);
        formPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        formPanel.add(quantityField);

        JPanel buttonPanel = new JPanel();
        addBookBtn = new JButton("Add Book");
        updateBookBtn = new JButton("Update Book");
        deleteBookBtn = new JButton("Delete Book");
        buttonPanel.add(addBookBtn);
        buttonPanel.add(updateBookBtn);
        buttonPanel.add(deleteBookBtn);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        bookPanel.add(topPanel, BorderLayout.NORTH);

        bookTableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Category", "Quantity", "Available"}, 0);
        bookTable = new JTable(bookTableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        bookPanel.add(scrollPane, BorderLayout.CENTER);
        
        addBookBtn.addActionListener(e -> addBook());
        updateBookBtn.addActionListener(e -> updateBook());
        deleteBookBtn.addActionListener(e -> deleteBook());
        
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedBookId = (int) bookTableModel.getValueAt(selectedRow, 0);
                    titleField.setText((String) bookTableModel.getValueAt(selectedRow, 1));
                    authorField.setText((String) bookTableModel.getValueAt(selectedRow, 2));
                    categoryField.setText((String) bookTableModel.getValueAt(selectedRow, 3));
                    quantityField.setText(String.valueOf(bookTableModel.getValueAt(selectedRow, 4)));
                }
            }
        });
        
        return bookPanel;
    }
    
    private void loadBookData() {
        bookTableModel.setRowCount(0);
        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            bookTableModel.addRow(new Object[]{
                book.getId(), book.getTitle(), book.getAuthor(), book.getCategory(), book.getQuantity(), book.getAvailableQuantity()
            });
        }
    }
    
    private void addBook() {
        try {
            Book newBook = new Book();
            newBook.setTitle(titleField.getText());
            newBook.setAuthor(authorField.getText());
            newBook.setCategory(categoryField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            newBook.setQuantity(quantity);
            newBook.setAvailableQuantity(quantity);
            
            bookDAO.addBook(newBook);
            JOptionPane.showMessageDialog(this, "Book added successfully!");
            loadBookData();
            clearBookForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            Book updatedBook = new Book();
            updatedBook.setId(selectedBookId);
            updatedBook.setTitle(titleField.getText());
            updatedBook.setAuthor(authorField.getText());
            updatedBook.setCategory(categoryField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            updatedBook.setQuantity(quantity);
            updatedBook.setAvailableQuantity(quantity); 
            
            bookDAO.updateBook(updatedBook);
            JOptionPane.showMessageDialog(this, "Book updated successfully!");
            loadBookData();
            clearBookForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int bookIdToDelete = (int) bookTableModel.getValueAt(selectedRow, 0);
            bookDAO.deleteBook(bookIdToDelete);
            JOptionPane.showMessageDialog(this, "Book deleted successfully!");
            loadBookData();
            clearBookForm();
        }
    }
    
    private void clearBookForm() {
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
        quantityField.setText("");
        selectedBookId = -1;
    }
    
    // --- User Management Panel ---
    private JPanel createUserManagementPanel() {
        JPanel userPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);
        formPanel.add(new JLabel("Role:"));
        roleComboBox = new JComboBox<>(new String[]{"user", "admin"});
        formPanel.add(roleComboBox);

        JPanel buttonPanel = new JPanel();
        addUserBtn = new JButton("Add User");
        updateUserBtn = new JButton("Update User");
        deleteUserBtn = new JButton("Delete User");
        buttonPanel.add(addUserBtn);
        buttonPanel.add(updateUserBtn);
        buttonPanel.add(deleteUserBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        userPanel.add(topPanel, BorderLayout.NORTH);
        
        userTableModel = new DefaultTableModel(new String[]{"ID", "Username", "Role", "Email"}, 0);
        userTable = new JTable(userTableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        userPanel.add(scrollPane, BorderLayout.CENTER);
        
        addUserBtn.addActionListener(e -> addUser());
        updateUserBtn.addActionListener(e -> updateUser());
        deleteUserBtn.addActionListener(e -> deleteUser());
        
        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedUserId = (int) userTableModel.getValueAt(selectedRow, 0);
                    usernameField.setText((String) userTableModel.getValueAt(selectedRow, 1));
                    roleComboBox.setSelectedItem((String) userTableModel.getValueAt(selectedRow, 2));
                    emailField.setText((String) userTableModel.getValueAt(selectedRow, 3));
                    passwordField.setText("");
                }
            }
        });

        return userPanel;
    }
    
    private void loadUserData() {
        userTableModel.setRowCount(0);
        List<User> users = userDAO.getAllUsers();
        for (User user : users) {
            userTableModel.addRow(new Object[]{
                user.getId(), user.getUsername(), user.getRole(), user.getEmail()
            });
        }
    }
    
    private void addUser() {
        try {
            User newUser = new User();
            newUser.setUsername(usernameField.getText());
            newUser.setPassword(new String(passwordField.getPassword()));
            newUser.setEmail(emailField.getText());
            newUser.setRole((String) roleComboBox.getSelectedItem());
            
            userDAO.addUser(newUser);
            JOptionPane.showMessageDialog(this, "User added successfully!");
            loadUserData();
            clearUserForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred while adding the user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            User updatedUser = new User();
            updatedUser.setId(selectedUserId);
            updatedUser.setUsername(usernameField.getText());
            if (passwordField.getPassword().length > 0) {
                updatedUser.setPassword(new String(passwordField.getPassword()));
            } else {
                User existingUser = userDAO.getUserByUsername((String) userTableModel.getValueAt(selectedRow, 1));
                updatedUser.setPassword(existingUser.getPassword());
            }
            updatedUser.setEmail(emailField.getText());
            updatedUser.setRole((String) roleComboBox.getSelectedItem());
            
            userDAO.updateUser(updatedUser);
            JOptionPane.showMessageDialog(this, "User updated successfully!");
            loadUserData();
            clearUserForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred while updating the user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int userIdToDelete = (int) userTableModel.getValueAt(selectedRow, 0);
            userDAO.deleteUser(userIdToDelete);
            JOptionPane.showMessageDialog(this, "User deleted successfully!");
            loadUserData();
            clearUserForm();
        }
    }
    
    private void clearUserForm() {
        usernameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        roleComboBox.setSelectedItem("user");
        selectedUserId = -1;
    }
    
    // --- Loan Management Panel ---
    private JPanel createLoanManagementPanel() {
        JPanel loanPanel = new JPanel(new BorderLayout());

        // Top panel for issuing books
        JPanel issuePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        issuePanel.setBorder(BorderFactory.createTitledBorder("Issue a Book"));
        
        bookComboBox = new JComboBox<>();
        userComboBox = new JComboBox<>();
        issueBookBtn = new JButton("Issue Book");
        
        issuePanel.add(new JLabel("Select Book:"));
        issuePanel.add(bookComboBox);
        issuePanel.add(new JLabel("Select User:"));
        issuePanel.add(userComboBox);
        issuePanel.add(issueBookBtn);
        
        loanPanel.add(issuePanel, BorderLayout.NORTH);

        // Center panel for displaying loans
        loanTableModel = new DefaultTableModel(new String[]{"ID", "Book ID", "User ID", "Borrow Date", "Due Date", "Returned"}, 0);
        loanTable = new JTable(loanTableModel);
        JScrollPane scrollPane = new JScrollPane(loanTable);
        
        JPanel loanTablePanel = new JPanel(new BorderLayout());
        loanTablePanel.setBorder(BorderFactory.createTitledBorder("Current Loans"));
        loanTablePanel.add(scrollPane, BorderLayout.CENTER);

        // Panel for return button
        JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        returnBookBtn = new JButton("Return Book");
        returnPanel.add(returnBookBtn);
        loanTablePanel.add(returnPanel, BorderLayout.SOUTH);

        loanPanel.add(loanTablePanel, BorderLayout.CENTER);
        
        // Action listeners for Loan Management
        issueBookBtn.addActionListener(e -> issueBook());
        returnBookBtn.addActionListener(e -> returnBook());
        
        // Populating the combo boxes
        populateComboBoxes();
        
        return loanPanel;
    }

    private void populateComboBoxes() {
        bookComboBox.removeAllItems();
        userComboBox.removeAllItems();

        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            bookComboBox.addItem(book.getId() + " - " + book.getTitle());
        }

        List<User> users = userDAO.getAllUsers();
        for (User user : users) {
            userComboBox.addItem(user.getId() + " - " + user.getUsername());
        }
    }
    
    private void loadLoanData() {
        loanTableModel.setRowCount(0);
        List<Loan> loans = loanDAO.getAllLoans();
        for (Loan loan : loans) {
            loanTableModel.addRow(new Object[]{
                loan.getId(), loan.getBookId(), loan.getUserId(), loan.getBorrowDate(), loan.getDueDate(), loan.isReturned()
            });
        }
    }

    private void issueBook() {
        if (bookComboBox.getSelectedIndex() == -1 || userComboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book and a user.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int bookId = Integer.parseInt(((String) bookComboBox.getSelectedItem()).split(" - ")[0]);
            int userId = Integer.parseInt(((String) userComboBox.getSelectedItem()).split(" - ")[0]);

            Loan newLoan = new Loan();
            newLoan.setBookId(bookId);
            newLoan.setUserId(userId);
            newLoan.setBorrowDate(new Date());
            newLoan.setDueDate(new Date(newLoan.getBorrowDate().getTime() + (14 * 24 * 60 * 60 * 1000L))); // 14 days loan period
            
            loanDAO.issueBook(newLoan);
            JOptionPane.showMessageDialog(this, "Book issued successfully!");
            loadLoanData();
            loadBookData(); // Also refresh the book table as available quantity has changed.
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred while issuing the book.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void returnBook() {
        int selectedRow = loanTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a loan to return.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to return this book?", "Confirm Return", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int loanId = (int) loanTableModel.getValueAt(selectedRow, 0);
            loanDAO.returnBook(loanId);
            JOptionPane.showMessageDialog(this, "Book returned successfully!");
            loadLoanData();
            loadBookData();
        }
    }
}