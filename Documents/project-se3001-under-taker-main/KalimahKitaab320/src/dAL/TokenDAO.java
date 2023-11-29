package dAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * TokenDAO class provides data access operations for handling tokens in the
 * database.
 */
public class TokenDAO implements IDALToken {
	private static final Logger logger = Logger.getLogger(TokenDAO.class.getName());
	
	private static TokenDAO instance;
	private Connection connection;

	/**
	 * Constructor for TokenDAO.
	 */
	TokenDAO() {
		try {
			setupLogger();

			// Establish database connection (replace placeholders with your database
			// information)
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kalimahkitaab", "root", "");
			addPosColumnToTokensTable();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error initializing TokenDAO", e);
		}
	}
	
	/**
     * Get the single instance of TokenDAO using lazy instantiation.
     *
     * @return The single instance of TokenDAO.
     */
    public static synchronized TokenDAO getInstance() {
        if (instance == null) {
            instance = new TokenDAO();
        }
        return instance;
    }

	/**
	 * Set up the logger for TokenDAO.
	 */
	private void setupLogger() {
		try {
			FileHandler fileHandler = new FileHandler("tokenDAO.log");
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
			logger.setLevel(Level.INFO);
			logger.info("TokenDAO logging initialized.");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error setting up logging for TokenDAO", e);
		}
	}

	/**
	 * Add the 'pos' column to the tokens table if it does not exist.
	 */
	private void addPosColumnToTokensTable() {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SHOW COLUMNS FROM tokens LIKE 'pos'");
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				preparedStatement = connection.prepareStatement("ALTER TABLE tokens ADD COLUMN pos VARCHAR(255)");
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error adding 'pos' column to tokens table", e);
		}
	}

	/**
	 * Retrieves verses data for a given verse ID.
	 *
	 * @param verseId The ID of the verse.
	 * @return List of verses data.
	 */
	public List<String> getVersesData(int verseId) {
		List<String> versesData = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT verse1, verse2 FROM verses WHERE verse_id = ?");
			preparedStatement.setInt(1, verseId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				versesData.add(resultSet.getString("verse1"));
				versesData.add(resultSet.getString("verse2"));
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error getting verses data", e);
		}
		return versesData;
	}

	/**
	 * Retrieves tokens for a given verse ID.
	 *
	 * @param verseId The ID of the verse.
	 * @return List of tokens.
	 */
	public List<String> getTokens(int verseId) {
		List<String> tokens = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT token_text FROM tokens WHERE verse_id = ?");
			preparedStatement.setInt(1, verseId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				tokens.add(resultSet.getString("token_text"));
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error getting tokens", e);
		}
		return tokens;
	}

	/**
	 * Tokenizes verses for a given verse ID.
	 *
	 * @param verseId The ID of the verse to tokenize.
	 */
	public void tokenizeVerses(int verseId) {
		List<String> versesData = getVersesData(verseId);

		if (versesData.isEmpty()) {
			logger.warning("No verses data to tokenize for Verse ID: " + verseId);
			return;
		}

		for (String verseText : versesData) {
			tokenizeVerse(verseId, verseText);
		}
	}

	private void tokenizeVerse(int verseId, String verseText) {
		String[] tokens = verseText.split("\\s+");
		Set<String> existingTokens = new HashSet<>(getTokens(verseId));

		for (String token : tokens) {
			if (!existingTokens.contains(token)) {
				List<net.oujda_nlp_team.entity.Result> pos = net.oujda_nlp_team.AlKhalil2Analyzer.getInstance()
						.processToken(token).getAllResults();

				if (!pos.isEmpty()) {
					String str = pos.get(0).getPartOfSpeech();
					saveToken(verseId, token, str);
				}

				existingTokens.add(token);
			}
		}
	}

	/**
	 * Saves a token with its associated verse ID and POS.
	 *
	 * @param verseId The ID of the verse.
	 * @param token   The token text.
	 * @param pos     The part of speech (POS).
	 */
	public void saveToken(int verseId, String token, String pos) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO tokens (verse_id, token_text, pos) VALUES (?, ?, ?)");
			preparedStatement.setInt(1, verseId);
			preparedStatement.setString(2, token);
			preparedStatement.setString(3, pos);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error saving token", e);
		}
	}

	/**
	 * Updates a token by replacing it with new tokens.
	 *
	 * @param verseId    The ID of the verse.
	 * @param oldTokenId The ID of the old token.
	 * @param newTokens  Array of new token texts.
	 */
	public void updateToken(int verseId, int oldTokenId, String[] newTokens) {
		deleteToken(oldTokenId);

		for (String newToken : newTokens) {
			saveToken(verseId, newToken, getPOSTagForToken(newToken));
		}
	}

	/**
	 * Retrieves the ID of a token for a given verse ID and token text.
	 *
	 * @param verseId The ID of the verse.
	 * @param token   The token text.
	 * @return The ID of the token.
	 */
	public int getTokenId(int verseId, String token) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT token_id FROM tokens WHERE verse_id = ? AND token_text = ?");
			preparedStatement.setInt(1, verseId);
			preparedStatement.setString(2, token);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return resultSet.getInt("token_id");
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error getting token ID", e);
		}
		return -1;
	}

	/**
	 * Deletes a token by its ID.
	 *
	 * @param tokenId The ID of the token to delete.
	 */
	public void deleteToken(int tokenId) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM tokens WHERE token_id = ?");
			preparedStatement.setInt(1, tokenId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error deleting token", e);
		}
	}

	/**
	 * Closes the database connection.
	 */
	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Error closing database connection", e);
		}
	}

	/**
	 * Placeholder method for Arabic POS tagging.
	 *
	 * @param token The token text.
	 * @return The placeholder POS.
	 */
	private String getPOSTagForToken(String token) {
		return "اسم";
	}
}
