package bLL;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import dAL.BooksDAO;
import dAL.IDALFascade;

/**
 * Business logic operations for managing books.
 */
public class BooksBO implements IBooks {
    private static final Logger logger = Logger.getLogger(BooksBO.class.getName());

    private IDALFascade dal;
    private static BooksBO instance;
    
    public BooksBO() {
 
    }
    
    public static BooksBO getInstance() {
        if (instance == null) {
            instance = new BooksBO();
        }
        return instance;
    }
    /**
     * Sets the data access layer for the business logic operations.
     *
     * @param dal The data access layer.
     */
    public void setIDAL(IDALFascade dal) {
        this.dal = dal;
    }

    /**
     * Retrieves a list of all books.
     *
     * @return List of books.
     */
    @Override
    public List<BooksDAO.Book> getAllBooks() {
        return dal.getAllBooks();
    }

    /**
     * Inserts a new book.
     *
     * @param title  The title of the book.
     * @param author The author of the book.
     * @param year   The publication year of the book.
     */
    @Override
    public void insertBook(String title, String author, int year) {
        dal.insertBook(title, author, year);
        logger.info("Book inserted successfully. Title: " + title + ", Author: " + author + ", Year: " + year);
    }

    /**
     * Updates an existing book.
     *
     * @param id      The ID of the book to update.
     * @param newTitle    The new title for the book.
     * @param newAuthor   The new author for the book.
     * @param newYear     The new publication year for the book.
     */
    @Override
    public void updateBook(int id, String newTitle, String newAuthor, int newYear) {
        dal.updateBook(id, newTitle, newAuthor, newYear);
        logger.info("Book updated successfully. ID: " + id + ", New Title: " + newTitle +
                    ", New Author: " + newAuthor + ", New Year: " + newYear);
    }

    /**
     * Deletes a book.
     *
     * @param id The ID of the book to delete.
     */
    @Override
    public void deleteBook(int id) {
        dal.deleteBook(id);
        logger.info("Book deleted successfully. ID: " + id);
    }
}
