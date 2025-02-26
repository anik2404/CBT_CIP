import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addBookBtn, editBookBtn, deleteBookBtn, viewUsersBtn;

    public AdminDashboard() {
        setTitle("Admin Dashboard - Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create top panel for search and buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchField = new JTextField(20);
        addBookBtn = new JButton("Add Book");
        editBookBtn = new JButton("Edit Book");
        deleteBookBtn = new JButton("Delete Book");
        viewUsersBtn = new JButton("View Users");

        topPanel.add(new JLabel("Search: "));
        topPanel.add(searchField);
        topPanel.add(addBookBtn);
        topPanel.add(editBookBtn);
        topPanel.add(deleteBookBtn);
        topPanel.add(viewUsersBtn);

        // Create table
        createBooksTable();
        JScrollPane scrollPane = new JScrollPane(booksTable);

        // Add components to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addBookBtn.addActionListener(e -> showAddBookDialog());
        editBookBtn.addActionListener(e -> handleEditBook());
        deleteBookBtn.addActionListener(e -> handleDeleteBook());
        viewUsersBtn.addActionListener(e -> showUsersDialog());
        searchField.addActionListener(e -> searchBooks());

        add(mainPanel);
        loadBooks();
    }

    private void createBooksTable() {
        String[] columns = {"ID", "Title", "Author", "ISBN", "Category", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        booksTable = new JTable(tableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void loadBooks() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM books";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getString("category"),
                    rs.getString("status")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading books!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddBookDialog() {
        JDialog dialog = new JDialog(this, "Add New Book", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create form fields
        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField isbnField = new JTextField(20);
        JTextField categoryField = new JTextField(20);

        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        panel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        panel.add(authorField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        panel.add(isbnField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        panel.add(categoryField, gbc);

        JButton saveButton = new JButton("Save");
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO books (title, author, isbn, category, status) VALUES (?, ?, ?, ?, 'available')";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, titleField.getText());
                pstmt.setString(2, authorField.getText());
                pstmt.setString(3, isbnField.getText());
                pstmt.setString(4, categoryField.getText());
                pstmt.executeUpdate();

                dialog.dispose();
                loadBooks();
                JOptionPane.showMessageDialog(this, "Book added successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding book!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void handleEditBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Implementation similar to addBook but with pre-filled values
    }

    private void handleDeleteBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this book?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "DELETE FROM books WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, bookId);
                pstmt.executeUpdate();

                loadBooks();
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting book!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchBooks() {
        String searchTerm = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM books WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ? OR LOWER(isbn) LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            String term = "%" + searchTerm + "%";
            pstmt.setString(1, term);
            pstmt.setString(2, term);
            pstmt.setString(3, term);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getString("category"),
                    rs.getString("status")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching books!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showUsersDialog() {
        JDialog dialog = new JDialog(this, "User Management", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        // Create users table
        String[] columns = {"ID", "Username", "Role", "Status"};
        DefaultTableModel userTableModel = new DefaultTableModel(columns, 0);
        JTable usersTable = new JTable(userTableModel);
        JScrollPane scrollPane = new JScrollPane(usersTable);

        // Load users data
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("role"),
                    rs.getString("status")
                };
                userTableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dialog.add(scrollPane);
        dialog.setVisible(true);
    }
}