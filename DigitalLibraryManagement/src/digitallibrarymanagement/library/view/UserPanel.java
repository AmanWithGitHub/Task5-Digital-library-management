package digitallibrarymanagement.library.view;

import digitallibrarymanagement.library.database.BookDAO;
import digitallibrarymanagement.library.database.LoanDAO;
import digitallibrarymanagement.library.model.Book;
import digitallibrarymanagement.library.model.Loan;
import digitallibrarymanagement.library.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class UserPanel extends JPanel {
    private BookDAO bookDAO;
    private LoanDAO loanDAO;
    private User currentUser;

    private JTable bookTable, loansTable;
    private DefaultTableModel bookTableModel, loansTableModel;
    private JTextField searchField;
    private JButton searchButton;

    public UserPanel(User user) {
        this.bookDAO = new BookDAO();
        this.loanDAO = new LoanDAO();
        this.currentUser = user;
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        
        // --- Tab 1: View Books ---
        JPanel viewBooksPanel = createViewBooksPanel();
        tabbedPane.addTab("View Books", null, viewBooksPanel, "View and search for books");
        
        // --- Tab 2: View My Loans ---
        JPanel myLoansPanel = createMyLoansPanel();
        tabbedPane.addTab("My Loans", null, myLoansPanel, "View your loans");

        add(tabbedPane, BorderLayout.CENTER);
        
        loadBookData("");
        loadMyLoansData();
    }

    private JPanel createViewBooksPanel() {
        JPanel viewBooksPanel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search by Title or Author:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        viewBooksPanel.add(searchPanel, BorderLayout.NORTH);

        bookTableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Category", "Available"}, 0);
        bookTable = new JTable(bookTableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        viewBooksPanel.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText();
            loadBookData(searchTerm);
        });

        return viewBooksPanel;
    }

    private JPanel createMyLoansPanel() {
        JPanel myLoansPanel = new JPanel(new BorderLayout());

        loansTableModel = new DefaultTableModel(new String[]{"Loan ID", "Book ID", "Borrow Date", "Due Date", "Returned"}, 0);
        loansTable = new JTable(loansTableModel);
        JScrollPane scrollPane = new JScrollPane(loansTable);
        myLoansPanel.add(scrollPane, BorderLayout.CENTER);

        return myLoansPanel;
    }

    private void loadBookData(String searchTerm) {
        bookTableModel.setRowCount(0);
        List<Book> allBooks = bookDAO.getAllBooks();
        
        List<Book> filteredBooks = allBooks.stream()
            .filter(book -> searchTerm.isEmpty() ||
                             book.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                             book.getAuthor().toLowerCase().contains(searchTerm.toLowerCase()))
            .collect(Collectors.toList());
        
        for (Book book : filteredBooks) {
            bookTableModel.addRow(new Object[]{
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getAvailableQuantity()
            });
        }
    }
    
    private void loadMyLoansData() {
        loansTableModel.setRowCount(0);
        // You'll need to add a method to LoanDAO to get loans by user ID
        List<Loan> myLoans = loanDAO.getLoansByUserId(currentUser.getId());
        
        for (Loan loan : myLoans) {
            loansTableModel.addRow(new Object[]{
                loan.getId(),
                loan.getBookId(),
                loan.getBorrowDate(),
                loan.getDueDate(),
                loan.isReturned()
            });
        }
    }
}