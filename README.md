The Digital Library Management System is a comprehensive desktop application designed to streamline and automate the core functions of a library. Developed in Java, the project follows a layered architectural approach, which means the user interface, business logic, and data storage are all organized into separate, interconnected parts. This clean structure makes the system reliable and easy to maintain.

At its heart, the application serves two primary types of users: administrators and regular members.

Core Functionalities
Secure Authentication: The system begins with a secure login screen that validates user credentials. Based on a user's role, the application grants access to a personalized dashboard.

Administrator Dashboard: The administrator has complete control over the library's operations. Through an intuitive, tabbed interface, they can:

Manage Books: Easily add new books to the catalog, update existing book details, and remove books no longer in circulation.

Manage Users: Handle all user accounts, including adding new members and modifying or removing existing ones.

Manage Loans: Issue new book loans to users and process returns. This feature connects book and user data to track who has borrowed which book.

Regular User Dashboard: A regular user's experience is focused on browsing and interacting with the library's collection. They have a streamlined interface to:

View and Search Books: Browse the entire library catalog and search for specific books by title or author.

View Personal Loans: See a list of all the books they have currently borrowed and their due dates.

The application's backend is powered by a MySQL database, which securely stores all the information about books, users, and loans. The user interface is built using Java Swing, providing a familiar and responsive graphical environment.

This project successfully demonstrates a full-stack development capability, bringing together a robust database, a clean data access layer, and an interactive user interface to create a complete and practical solution for library management.
