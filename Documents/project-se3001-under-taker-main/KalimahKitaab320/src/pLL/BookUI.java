package pLL;

import java.awt.*;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import bLL.IBLLFascade;
import dAL.BooksDAO;

/**
 * This class represents the user interface for managing books.
 */
public class BookUI {
    private JFrame mainFrame;
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private IBLLFascade bll;
    private static BookUI instance;

    /**
     * Constructs a new BookUI instance.
     *
     * @param bll The business logic layer facade.
     */
    public BookUI(IBLLFascade bll) {
        this.bll = bll;
        prepareMainMenu();
    }
    
    public static BookUI getInstance(IBLLFascade bll) {
        if (instance == null) {
            instance = new BookUI(bll);
        }
        return instance;
    }

    private void prepareMainMenu() {
        mainFrame = new JFrame("KalmahKitab");
        mainFrame.setSize(600, 400);
        mainFrame.setResizable(false);
        mainFrame.getContentPane().setBackground(new Color(0x5E7986));
        mainFrame.setForeground(Color.WHITE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(new Color(0x5E7986));

        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Year"}, 0);
        booksTable = new JTable(tableModel);
        booksTable.setBackground(new Color(0x5E7986));
        booksTable.setForeground(Color.WHITE);
        booksTable.setSelectionBackground(Color.WHITE);
        booksTable.setSelectionForeground(new Color(0x5E7986));
        JScrollPane scrollPane = new JScrollPane(booksTable);

        displayBooks();

        JButton viewPoemsButton = createStyledButton("View Poems");
        JButton insertButton = createStyledButton("Insert Book");
        JButton updateButton = createStyledButton("Update Book");
        JButton deleteButton = createStyledButton("Delete Book");
        JButton backButton = createStyledButton("Back");

        viewPoemsButton.addActionListener(e -> {
            mainFrame.setVisible(false);
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookId = (int) tableModel.getValueAt(selectedRow, 0);
                openViewPoemsWindow(bookId);
            } else {
                System.out.println("Select a book to view poems.");
            }
        });
        insertButton.addActionListener(e -> openInsertWindow());
        updateButton.addActionListener(e -> updateBook());
        deleteButton.addActionListener(e -> deleteBook());
        

        backButton.addActionListener(e -> {
            mainFrame.setVisible(false);
            MainGUI main = new MainGUI();
            main.setVisible(true);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(viewPoemsButton);
        bottomPanel.add(insertButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(backButton);

        Dimension buttonSize = new Dimension(80, 30);
        insertButton.setPreferredSize(buttonSize);
        updateButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        viewPoemsButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);

        mainFrame.add(scrollPane, BorderLayout.CENTER);
        mainFrame.add(bottomPanel, BorderLayout.SOUTH);

        mainFrame.setVisible(true);
    }

    private void openViewPoemsWindow(int bookId) {
        PoemsGUI poemsGUI = new PoemsGUI(bookId);
        poemsGUI.setVisible(true);
    }

    private void displayBooks() {
        List<BooksDAO.Book> books = bll.getAllBooks();
        tableModel.setRowCount(0);

        for (BooksDAO.Book book : books) {
            tableModel.addRow(new Object[]{book.getId(), book.getTitle(), book.getAuthor(), book.getYear()});
        }
    }

    private void openInsertWindow() {
        JFrame insertFrame = new JFrame("Insert Book");
        insertFrame.setSize(600, 400);
        insertFrame.setLayout(new GridLayout(6, 2));

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JDateChooser yearChooser = new JDateChooser();

        insertFrame.add(new JLabel("Title:"));
        insertFrame.add(titleField);
        insertFrame.add(new JLabel("Author:"));
        insertFrame.add(authorField);
        insertFrame.add(new JLabel("Year:"));
        insertFrame.add(yearChooser);

        JButton insertButton = new JButton("Insert");
        insertButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            Date selectedDate = yearChooser.getDate();

            if (!title.isEmpty() && !author.isEmpty() && selectedDate != null) {
                try {
                    int year = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
                    bll.insertBook(title, author, year);
                    System.out.println("Book inserted successfully.");
                    insertFrame.dispose();
                    displayBooks();
                } catch (Exception ex) {
                    System.out.println("Invalid date. Please enter a valid date.");
                }
            }
        });

        insertFrame.add(insertButton);
        insertFrame.setVisible(true);
    }

    private void updateBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow != -1) {
            JFrame updateFrame = new JFrame("Update Book");
            updateFrame.setSize(600, 400);
            updateFrame.setLayout(new GridLayout(7, 2));

            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            JDateChooser newYearChooser = new JDateChooser();

            updateFrame.add(new JLabel("New Title:"));
            updateFrame.add(titleField);
            updateFrame.add(new JLabel("New Author:"));
            updateFrame.add(authorField);
            updateFrame.add(new JLabel("New Year:"));
            updateFrame.add(newYearChooser);

            JButton updateButton = new JButton("Update");
            JButton viewPoemsButton = new JButton("View Poems");

            final int[] selectedRowArray = {selectedRow};

            updateButton.addActionListener(e -> {
                String newTitle = titleField.getText();
                String newAuthor = authorField.getText();
                Date newSelectedDate = newYearChooser.getDate();

                if (!newTitle.isEmpty() && !newAuthor.isEmpty() && newSelectedDate != null) {
                    try {
                        int id = (int) tableModel.getValueAt(selectedRowArray[0], 0);
                        int newYear = newSelectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
                        bll.updateBook(id, newTitle, newAuthor, newYear);
                        System.out.println("Book updated successfully.");
                        updateFrame.dispose();
                        displayBooks();
                    } catch (Exception ex) {
                        System.out.println("Invalid date. Please enter a valid date.");
                    }
                }
            });

            viewPoemsButton.addActionListener(e -> {
                int selectedRowValue = selectedRowArray[0];
                if (selectedRowValue != -1) {
                    int bookId = (int) tableModel.getValueAt(selectedRowValue, 0);
                    openViewPoemsWindow(bookId);
                } else {
                    System.out.println("Select a book to view poems.");
                }
            });

            updateFrame.add(new JLabel());
            updateFrame.add(viewPoemsButton);

            updateFrame.add(new JLabel());
            updateFrame.add(updateButton);

            updateFrame.setVisible(true);
        } else {
            System.out.println("Select a book to update.");
        }
    }

    private void deleteBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            bll.deleteBook(id);
            System.out.println("Book deleted successfully.");
            displayBooks();
        } else {
            System.out.println("Select a book to delete.");
        }
    }

    private JButton createStyledButton(String label) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(150, 30));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(0x5E7986));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                button.setBackground(new Color(0x5E7986));
                button.setForeground(new Color(0x5E7986));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setBackground(Color.WHITE);
                button.setForeground(new Color(0x5E7986));
            }
        });

        return button;
    }
}
