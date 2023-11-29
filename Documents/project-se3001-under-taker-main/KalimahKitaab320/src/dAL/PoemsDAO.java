package dAL;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Data Access Object (DAO) for managing poems in the database.
 */
public class PoemsDAO implements IDALPoems {

    private static final Logger LOGGER = Logger.getLogger(PoemsDAO.class.getName());

    private static PoemsDAO instance;

    
    /**
     * Get the singleton instance of PoemsDAO.
     *
     * @return The singleton instance.
     */
    public static PoemsDAO getInstance() {
        if (instance == null) {
            instance = new PoemsDAO();
        }
        return instance;
    }
    
    
    private Connection connection;

    /**
     * Constructor for initializing the PoemsDAO.
     */
    public PoemsDAO() {
        try {
            loadDatabaseProperties();
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing database connection", e);
        }
    }

    private String databaseUrl;
    private String databaseUser;
    private String databasePassword;

    /**
     * Load database properties from the "db.properties" file.
     */
    private void loadDatabaseProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            Properties properties = new Properties();
            if (input != null) {
                properties.load(input);
                databaseUrl = properties.getProperty("db.url");
                databaseUser = properties.getProperty("db.user");
                databasePassword = properties.getProperty("db.password");
            } else {
                LOGGER.log(Level.SEVERE, "Unable to find db.properties file");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading database properties", e);
        }
    }

    /**
     * Add a poem to the database.
     *
     * @param poemTitle The title of the poem.
     * @param bookId    The ID of the book to which the poem belongs.
     * @return The ID of the added poem, or -1 if an error occurs.
     */
    public int addPoem(String poemTitle, int bookId) {
        try {
            String poemQuery = "INSERT INTO poems (title) VALUES (?)";
            PreparedStatement poemStatement = connection.prepareStatement(poemQuery, Statement.RETURN_GENERATED_KEYS);
            poemStatement.setString(1, poemTitle);
            poemStatement.executeUpdate();

            ResultSet generatedKeys = poemStatement.getGeneratedKeys();
            int poemId = -1;
            if (generatedKeys.next()) {
                poemId = generatedKeys.getInt(1);
            }

            poemStatement.close();

            if (poemId != -1) {
                String associationQuery = "UPDATE poems SET book_id = ? WHERE poem_id = ?";
                try (PreparedStatement associationStatement = connection.prepareStatement(associationQuery)) {
                    associationStatement.setInt(1, bookId);
                    associationStatement.setInt(2, poemId);
                    associationStatement.executeUpdate();
                }
            }

            return poemId;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding poem to the database", ex);
            return -1;
        }
    }

    /**
     * Add a verse to the database.
     *
     * @param verse1 The first line of the verse.
     * @param verse2 The second line of the verse.
     */
    public void addVerse(String verse1, String verse2) {
        try {
            String misraQuery = "INSERT INTO verses (verse1, verse2) VALUES (?, ?)";
            PreparedStatement misraStatement = connection.prepareStatement(misraQuery);
            misraStatement.setString(1, verse1);
            misraStatement.setString(2, verse2);
            misraStatement.executeUpdate();

            misraStatement.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding verse to the database", ex);
        }
    }

    /**
     * Assign poem ID to verses in the database.
     *
     * @param poemId The ID of the poem.
     */
    public void assignPoemIdToVerses(int poemId) {
        try {
            String updateQuery = "UPDATE verses SET poem_id = ? WHERE poem_id IS NULL";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setInt(1, poemId);
            updateStatement.executeUpdate();

            updateStatement.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error assigning poem ID to verses in the database", ex);
        }
    }

    /**
     * Get all poem titles for a given book from the database.
     *
     * @param bookId The ID of the book.
     * @return A JTable containing poem IDs and titles.
     */
    public JTable getAllPoemTitles(int bookId) {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Poem ID");
        tableModel.addColumn("Title");

        try {
            String query = "SELECT poem_id, title FROM poems WHERE book_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, bookId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int poemId = resultSet.getInt("poem_id");
                    String title = resultSet.getString("title");

                    tableModel.addRow(new Object[]{poemId, title});
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error getting all poem titles from the database", ex);
        }

        return new JTable(tableModel);
    }

    /**
     * Get verses of a poem from the database.
     *
     * @param poemId The ID of the poem.
     * @return A JTable containing verse IDs, verse 1, and verse 2.
     */
    @Override
    public JTable getPoemVerses(int poemId) {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Verse ID");
        tableModel.addColumn("Verse 1");
        tableModel.addColumn("Verse 2");

        try {
            String query = "SELECT verse_id, verse1, verse2 FROM verses WHERE poem_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, poemId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int verseId = resultSet.getInt("verse_id");
                String verse1 = resultSet.getString("verse1");
                String verse2 = resultSet.getString("verse2");

                tableModel.addRow(new Object[]{verseId, verse1, verse2});
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error getting poem verses from the database", ex);
        }

        return new JTable(tableModel);
    }

    /**
     * Delete a poem and its associated verses from the database.
     *
     * @param poemId The ID of the poem to be deleted.
     */
    @Override
    public void deletePoem(int poemId) {
        try {

            try (PreparedStatement deleteVersesStatement = connection.prepareStatement("DELETE FROM verses WHERE poem_id = ?")) {
                deleteVersesStatement.setInt(1, poemId);
                deleteVersesStatement.executeUpdate();
            }

            try (PreparedStatement deletePoemStatement = connection.prepareStatement("DELETE FROM poems WHERE poem_id = ?")) {
                deletePoemStatement.setInt(1, poemId);
                deletePoemStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting poem from the database", ex);
        }
    }

    /**
     * Update the title of a poem in the database.
     *
     * @param poemId   The ID of the poem to be updated.
     * @param newTitle The new title for the poem.
     */
    @Override
    public void updatePoemTitle(int poemId, String newTitle) {
        try {

            try (PreparedStatement updateTitleStatement = connection.prepareStatement("UPDATE poems SET title = ? WHERE poem_id = ?")) {
                updateTitleStatement.setString(1, newTitle);
                updateTitleStatement.setInt(2, poemId);
                updateTitleStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating poem title in the database", ex);
        }
    }

    /**
     * Delete a verse and its associated tokens from the database.
     *
     * @param verseId The ID of the verse to be deleted.
     * @param poemId  The ID of the poem to which the verse belongs.
     */
    @Override
    public void deleteVerse(int verseId, int poemId) {
        try {

            try (PreparedStatement deleteTokensStatement = connection.prepareStatement(
                    "DELETE FROM tokens WHERE verse_id = ?")) {
                deleteTokensStatement.setInt(1, verseId);
                deleteTokensStatement.executeUpdate();
            }

            try (PreparedStatement deleteVerseStatement = connection.prepareStatement(
                    "DELETE FROM verses WHERE verse_id = ?")) {
                deleteVerseStatement.setInt(1, verseId);
                deleteVerseStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting verse from the database", ex);
        }
    }

    /**
     * Update the content of a verse in the database.
     *
     * @param verse1     The original first line of the verse.
     * @param verse2     The original second line of the verse.
     * @param newVerse1  The new first line for the verse.
     * @param newVerse2  The new second line for the verse.
     */
    @Override
    public void updateVerse(String verse1, String verse2, String newVerse1, String newVerse2) {
        try {

            try (PreparedStatement updateVerseStatement = connection.prepareStatement(
                    "UPDATE verses SET verse1 = ?, verse2 = ? WHERE verse1 = ? AND verse2 = ?")) {
                updateVerseStatement.setString(1, newVerse1);
                updateVerseStatement.setString(2, newVerse2);
                updateVerseStatement.setString(3, verse1);
                updateVerseStatement.setString(4, verse2);
                updateVerseStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating verse content in the database", ex);
        }
    }

    /**
     * Add a verse to a poem in the database.
     *
     * @param verse1         The first line of the verse.
     * @param verse2         The second line of the verse.
     * @param currentPoemId  The ID of the poem to which the verse should be added.
     */
    @Override
    public void addVerseToPoem(String verse1, String verse2, int currentPoemId) {
        try {

            try (PreparedStatement addVerseStatement = connection.prepareStatement(
                    "INSERT INTO verses (poem_id, verse1, verse2) VALUES (?, ?, ?)")) {

                addVerseStatement.setInt(1, currentPoemId);
                addVerseStatement.setString(2, verse1);
                addVerseStatement.setString(3, verse2);

                addVerseStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding verse to poem in the database", ex);
        }
    }

    /**
     * Get the content of a poem by its ID from the database.
     *
     * @param num The ID of the poem.
     * @return A list of string arrays containing poem ID, verse 1, and verse 2.
     */
    @Override
    public List<String[]> getPoemContentByID(int num) {
        List<String[]> versesList = new ArrayList<>();
        try {
            String query = "SELECT poem_id, verse1, verse2 FROM verses WHERE poem_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, num);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int poemId = resultSet.getInt("poem_id");
                String verse1 = resultSet.getString("verse1");
                String verse2 = resultSet.getString("verse2");
                String[] verses = {String.valueOf(poemId), verse1, verse2};
                versesList.add(verses);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error getting poem content by ID from the database", ex);
        }
        return versesList;
    }

    /**
     * Get the ID of the last inserted verse from the database.
     *
     * @return The ID of the last inserted verse, or -1 if none is found.
     */
    @Override
    public int getLastInsertedVerseId() {
        int lastInsertedVerseId = -1;

        try {

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(
                         "SELECT verse_id FROM verses ORDER BY verse_id DESC LIMIT 1")) {

                if (resultSet.next()) {
                    lastInsertedVerseId = resultSet.getInt("verse_id");
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error getting last inserted verse ID from the database", ex);
        }

        return lastInsertedVerseId;
    }
}
