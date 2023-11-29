package dAL;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the data access logic for retrieving verses from a database.
 */
public class VerseDAO implements IDALVerse {

    private static final Logger LOGGER = Logger.getLogger(VerseDAO.class.getName());

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public VerseDAO() {
        loadDatabaseProperties();
    }

    private void loadDatabaseProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            Properties properties = new Properties();
            if (input != null) {
                properties.load(input);
                dbUrl = properties.getProperty("db.url");
                dbUser = properties.getProperty("db.user");
                dbPassword = properties.getProperty("db.password");
            } else {
                LOGGER.log(Level.SEVERE, "Unable to find db.properties file");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading database properties", e);
        }
    }

    /**
     * Retrieves verses from the database based on the given poem ID.
     *
     * @param poemId The ID of the poem to retrieve verses for.
     * @return A list of verses as arrays of strings.
     */
    @Override
    public List<String[]> getVersesByPoemId(String poemId) {
        List<String[]> verses = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Establishing a connection
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // Preparing the SQL query
            String sql = "SELECT verse1, verse2 FROM verses WHERE poem_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, poemId);

            // Executing the query
            resultSet = preparedStatement.executeQuery();

            // Populating the list with the results
            while (resultSet.next()) {
                String verse1 = resultSet.getString("verse1");
                String verse2 = resultSet.getString("verse2");
                verses.add(new String[]{verse1, verse2});
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error executing SQL query or processing results", e);
        } finally {
            // Closing resources in the reverse order of their creation
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing database resources", e);
            }
        }

        return verses;
    }
}
