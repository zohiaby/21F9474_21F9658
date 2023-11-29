package dAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Data Access Object (DAO) for managing books in the database.
 */
public class BooksDAO implements IDALBooks {
    private static final Logger logger = Logger.getLogger(BooksDAO.class.getName());

    private Connection conn;
    private static BooksDAO instance;

    /**
     * Constructor for BooksDAO. Initializes the database connection.
     */
    public BooksDAO() {
        conn = initializeDatabaseConnection();
        setupLogger();
    }
    
    public static BooksDAO getInstance() {
        if (instance == null) {
            instance = new BooksDAO();
        }
        return instance;
    }

    /**
     * Initializes the database connection.
     *
     * @return The database connection.
     */
    private Connection initializeDatabaseConnection() {
        String DB_URL = "jdbc:mysql://localhost:3306/kalimahkitaab";
        String DB_USER = "root";
        String DB_PASSWORD = "";

        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error initializing database connection", e);
        }

        return null;
    }

    /**
     * Set up the logger for BooksDAO.
     */
    private void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("booksDAO.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);
            logger.info("BooksDAO logging initialized.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error setting up logging for BooksDAO", e);
        }
    }

    /**
     * Retrieves a list of all books from the database.
     *
     * @return List of books.
     */
    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try {
            String selectSQL = "SELECT book_id, title, author, year_author_passed_away FROM books";
            PreparedStatement selectStatement = conn.prepareStatement(selectSQL);
            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int year = resultSet.getInt("year_author_passed_away");
                books.add(new Book(id, title, author, year));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving all books", e);
        }
        return books;
    }

    /**
     * Inserts a new book into the database.
     *
     * @param title  The title of the book.
     * @param author The author of the book.
     * @param year   The publication year of the book.
     */
    @Override
    public void insertBook(String title, String author, int year) {
        try {
            String insertSQL = "INSERT INTO books (title, author, year_author_passed_away) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = conn.prepareStatement(insertSQL);
            insertStatement.setString(1, title);
            insertStatement.setString(2, author);
            insertStatement.setInt(3, year);
            insertStatement.executeUpdate();
            logger.info("Book inserted successfully. Title: " + title + ", Author: " + author + ", Year: " + year);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting book", e);
        }
    }

    /**
     * Updates an existing book in the database.
     *
     * @param id      The ID of the book to update.
     * @param newTitle    The new title for the book.
     * @param newAuthor   The new author for the book.
     * @param newYear     The new publication year for the book.
     */
    @Override
    public void updateBook(int id, String newTitle, String newAuthor, int newYear) {
        try {
            String updateSQL = "UPDATE books SET title = ?, author = ?, year_author_passed_away = ? WHERE book_id = ?";
            PreparedStatement updateStatement = conn.prepareStatement(updateSQL);
            updateStatement.setString(1, newTitle);
            updateStatement.setString(2, newAuthor);
            updateStatement.setInt(3, newYear);
            updateStatement.setInt(4, id);
            updateStatement.executeUpdate();
            logger.info("Book updated successfully. ID: " + id + ", New Title: " + newTitle +
                        ", New Author: " + newAuthor + ", New Year: " + newYear);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating book", e);
        }
    }

    /**
     * Deletes a book from the database.
     *
     * @param id The ID of the book to delete.
     */
    @Override
    public void deleteBook(int id) {
        try {
            String deleteSQL = "DELETE FROM books WHERE book_id = ?";
            PreparedStatement deleteStatement = conn.prepareStatement(deleteSQL);
            deleteStatement.setInt(1, id);
            deleteStatement.executeUpdate();
            logger.info("Book deleted successfully. ID: " + id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting book", e);
        }
    }

    /**
     * Represents a book entity with its attributes.
     */
    public static class Book {
        private int id;
        private String title;
        private String author;
        private int year;

        /**
         * Constructor for creating a Book instance.
         *
         * @param id     The ID of the book.
         * @param title  The title of the book.
         * @param author The author of the book.
         * @param year   The publication year of the book.
         */
        public Book(int id, String title, String author, int year) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.year = year;
        }

        /**
         * Gets the ID of the book.
         *
         * @return The book ID.
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the title of the book.
         *
         * @return The book title.
         */
        public String getTitle() {
            return title;
        }

        /**
         * Gets the author of the book.
         *
         * @return The book author.
         */
        public String getAuthor() {
            return author;
        }

        /**
         * Gets the publication year of the book.
         *
         * @return The publication year.
         */
        public int getYear() {
            return year;
        }
    }
}
