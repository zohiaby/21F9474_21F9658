package dAL;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the Data Access Layer (DAL) for retrieving verses and related information for a single poem view.
 */
public class SinglePoemVIewDAO implements IDALSinglePoemVIew {

    private static final Logger LOGGER = Logger.getLogger(SinglePoemVIewDAO.class.getName());

    private Connection connection;

    /**
     * Constructs a SinglePoemVIewDAO and initializes the database connection.
     */
    public SinglePoemVIewDAO() {
        // Initializing the database connection
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
     * Searches for verses based on a given search term and returns the result as a list.
     *
     * @param searchTerm The term to search for in verses.
     * @return An ArrayList of verses matching the search term.
     */
    @Override
    public ArrayList<String> searchVersesList(String searchTerm) {
        // Performing the SQL query and returning the result as a list
        // Adjusting the SQL query based on your database structure
        String query = "SELECT DISTINCT verses.*, poems.poem_id FROM verses "
                + "JOIN verse_root ON verses.verse_id = verse_root.verse_id "
                + "JOIN poems ON verses.poem_id = poems.poem_id "
                + "WHERE verse_root.root_name = ? OR verses.verse1 LIKE ? OR verses.verse2 LIKE ?";

        ArrayList<String> versesList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, searchTerm);
            statement.setString(2, "%" + searchTerm + "%");
            statement.setString(3, "%" + searchTerm + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int poemId = resultSet.getInt("poem_id");
                String verse1 = resultSet.getString("verse1");
                String verse2 = resultSet.getString("verse2");

                String verse = verse1 + "\n" + verse2 + "\n" + poemId + "\n\n";
                versesList.add(verse);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error executing SQL query or processing results", e);
        }

        return versesList;
    }
}
