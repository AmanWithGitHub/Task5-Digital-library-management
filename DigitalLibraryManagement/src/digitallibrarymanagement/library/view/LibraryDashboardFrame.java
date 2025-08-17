package digitallibrarymanagement.library.view;

import digitallibrarymanagement.library.model.User;
import javax.swing.*;
import java.awt.*;

public class LibraryDashboardFrame extends JFrame {

    public LibraryDashboardFrame(User user) {
        setTitle("Digital Library - Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        if ("admin".equalsIgnoreCase(user.getRole())) {
            add(new AdminPanel());
        } else if ("user".equalsIgnoreCase(user.getRole())) {
            add(new UserPanel(user));
        } else {
            add(new JLabel("Invalid user role."));
        }
    }
}