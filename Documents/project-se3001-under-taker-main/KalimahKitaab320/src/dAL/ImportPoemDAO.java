package dAL;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.oujda_nlp_team.AlKhalil2Analyzer;

/**
 * Data Access Object (DAO) for importing poems into the database.
 */
public class ImportPoemDAO implements IDALImportPoems {

	  private String dbUrl;
	    private String dbUser;
	    private String dbPassword;

    private String poemtitle = "title";
    private int poemId = 1;

    private static final Logger LOGGER = Logger.getLogger(ImportPoemDAO.class.getName());

    public ImportPoemDAO() {
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
     * Parses a poem from the specified file and adds it to the database.
     *
     * @param filePath The path to the file containing the poem.
     * @return True if the data is added successfully, false otherwise.
     */
    @Override
    public boolean parsePoem(String filePath) {
        boolean ignoreLines = false;
        String poemTitle = null, misra1 = null, misra2 = null, line;

        try (BufferedReader breader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            while ((line = breader.readLine()) != null) {
                if (line.contains("=========")) {
                    ignoreLines = false;
                    continue;
                }

                if (ignoreLines) {
                    continue;
                }

                if (line.startsWith("[")) {
                    Pattern titlePattern = Pattern.compile("\\[(.*?)\\]");
                    Matcher titleMatcher = titlePattern.matcher(line);
                    if (titleMatcher.find()) {
                        poemTitle = titleMatcher.group(1);
                    }
                } else if (line.startsWith("(")) {
                    Pattern versePattern = Pattern.compile("\\((.*?)\\)");
                    Matcher verseMatcher = versePattern.matcher(line);
                    if (verseMatcher.find()) {
                        String verseText = verseMatcher.group(1);
                        String[] misras = verseText.split("\\.\\.\\.");
                        if (misras.length >= 1) {
                            misra1 = misras[0].trim();
                        }
                        if (misras.length >= 2) {
                            misra2 = misras[1].trim();
                        }
                        storePoem(poemTitle, misra1, misra2);
                    }
                } else if (line.contains("_________")) {
                    ignoreLines = true;
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading file: " + filePath, e);
            return false;
        }
        return true;
    }

    /**
     * Stores a poem in the database.
     *
     * @param poemTitle The title of the poem.
     * @param misra1    The first part of the poem.
     * @param misra2    The second part of the poem.
     */
    public void storePoem(String poemTitle, String misra1, String misra2) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            if (!poemTitle.equals(poemtitle)) {
                String insertPoemSQL = "INSERT INTO Poems (book_id, title) VALUES (?, ?)";
                try (PreparedStatement poemStatement = connection.prepareStatement(insertPoemSQL,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                    poemStatement.setInt(1, 1);
                    poemStatement.setString(2, poemTitle);
                    poemStatement.executeUpdate();

                    ResultSet generatedKeys = poemStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        poemId = generatedKeys.getInt(1);
                    }
                }
                poemtitle = poemTitle;
            }

            String insertVerseSQL = "INSERT INTO Verses (poem_id, verse1, verse2) VALUES (?, ?, ?)";
            try (PreparedStatement verseStatement = connection.prepareStatement(insertVerseSQL,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                verseStatement.setInt(1, poemId);
                verseStatement.setString(2, misra1);
                verseStatement.setString(3, misra2);
                verseStatement.executeUpdate();

                ResultSet generatedKeys = verseStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int verseId = generatedKeys.getInt(1);
                    creatRoots(verseId, misra1, misra2);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error storing poem in the database", e);
        }
    }

    /**
     * Creates roots for the verses and associates them with the verses.
     *
     * @param verseId The ID of the verse.
     * @param verse1  The first part of the verse.
     * @param verse2  The second part of the verse.
     */
    public void creatRoots(int verseId, String verse1, String verse2) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String verses = verse1 + " " + verse2;
            String[] splittedVerse = verses.split(" ");

            for (String word : splittedVerse) {
                List<String> roots = AlKhalil2Analyzer.getInstance().processToken(word).getAllRoots();
                for (String suggRoot : roots) {
                    if (!suggRoot.contains("-") && !suggRoot.contains(" ") && !suggRoot.contains(":")) {
                        storeRoot(connection, suggRoot);
                        int rootId = getGeneratedRootId(connection);
                        associateRootWithVerse(connection, verseId, rootId, suggRoot);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating roots for verses", e);
        }
    }

    /**
     * Stores a root in the database.
     *
     * @param connection The database connection.
     * @param rootName   The name of the root.
     * @throws SQLException If a database error occurs.
     */
    private void storeRoot(Connection connection, String rootName) throws SQLException {
        String insertRootSQL = "INSERT INTO Roots (root_name) VALUES (?)";
        try (PreparedStatement rootStatement = connection.prepareStatement(insertRootSQL,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            rootStatement.setString(1, rootName);
            rootStatement.executeUpdate();
        }
    }

    /**
     * Gets the generated ID of the last inserted root.
     *
     * @param connection The database connection.
     * @return The generated root ID.
     * @throws SQLException If a database error occurs.
     */
    private int getGeneratedRootId(Connection connection) throws SQLException {
        int rootId = 0;
        String query = "SELECT LAST_INSERT_ID() as root_id";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                rootId = resultSet.getInt("root_id");
            }
        }
        return rootId;
    }

    /**
     * Associates a root with a verse in the database.
     *
     * @param connection The database connection.
     * @param verseId    The ID of the verse.
     * @param rootId     The ID of the root.
     * @param rootName   The name of the root.
     * @throws SQLException If a database error occurs.
     */
    private void associateRootWithVerse(Connection connection, int verseId, int rootId, String rootName)
            throws SQLException {
        String insertAssociationSQL = "INSERT INTO Verse_Root (verse_id, root_id, root_name) VALUES (?, ?, ?)";
        try (PreparedStatement associationStatement = connection.prepareStatement(insertAssociationSQL)) {
            associationStatement.setInt(1, verseId);
            associationStatement.setInt(2, rootId);
            associationStatement.setString(3, rootName);
            associationStatement.executeUpdate();
        }
    }

    /**
     * Tokenizes a verse and stores the tokens in the database.
     *
     * @param verseId The ID of the verse.
     * @param verse1  The first part of the verse.
     * @param verse2  The second part of the verse.
     */
    public void tokenizeVerse(int verseId, String verse1, String verse2) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String verses = verse1 + " " + verse2;
            String[] tokens = verses.split("\\s+");

            for (String token : tokens) {
                if (!isTokenExist(connection, verseId, token)) {
                    List<net.oujda_nlp_team.entity.Result> pos = AlKhalil2Analyzer.getInstance().processToken(token)
                            .getAllResults();
                    String partOfSpeech = pos.get(0).getPartOfSpeech();
                    saveToken(connection, verseId, token, partOfSpeech);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error tokenizing verse", e);
        }
    }

    /**
     * Checks if a token already exists for a verse in the database.
     *
     * @param connection The database connection.
     * @param verseId    The ID of the verse.
     * @param token      The token to check.
     * @return True if the token already exists, false otherwise.
     * @throws SQLException If a database error occurs.
     */
    private boolean isTokenExist(Connection connection, int verseId, String token) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM Tokens WHERE verse_id = ? AND token_text = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, verseId);
            statement.setString(2, token);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        }
        return false;
    }

    /**
     * Saves a token in the database.
     *
     * @param connection    The database connection.
     * @param verseId       The ID of the verse.
     * @param token         The token to save.
     * @param partOfSpeech  The part of speech of the token.
     * @throws SQLException If a database error occurs.
     */
    private void saveToken(Connection connection, int verseId, String token, String partOfSpeech) throws SQLException {
        String insertTokenSQL = "INSERT INTO Tokens (verse_id, token_text, pos) VALUES (?, ?, ?)";
        try (PreparedStatement tokenStatement = connection.prepareStatement(insertTokenSQL)) {
            tokenStatement.setInt(1, verseId);
            tokenStatement.setString(2, token);
            tokenStatement.setString(3, partOfSpeech);
            tokenStatement.executeUpdate();
        }
    }
}
