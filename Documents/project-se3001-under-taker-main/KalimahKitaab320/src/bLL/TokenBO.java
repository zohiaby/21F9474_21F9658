package bLL;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import dAL.TokenDAO;

/**
 * TokenBO class handles the business logic operations for tokenizing verses.
 */
public class TokenBO implements IToken {
	private static final Logger logger = Logger.getLogger(TokenBO.class.getName());

	private TokenDAO tokenDAO;
	private static TokenBO instance;

	/**
	 * Constructor for TokenBO.
	 */
	public TokenBO() {
        try {
            setupLogger();
            this.tokenDAO = TokenDAO.getInstance();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error initializing TokenBO", e);
        }
    }
	
	/**
     * Get the single instance of TokenBO using lazy instantiation.
     *
     * @return The single instance of TokenBO.
     */
    public static synchronized TokenBO getInstance() {
        if (instance == null) {
            instance = new TokenBO();
        }
        return instance;
    }

	/**
	 * Set up the logger for TokenBO.
	 */
	private void setupLogger() {
		try {
			 
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error setting up logging for TokenBO", e);
		}
	}

	/**
	 * Retrieves verses data for a given verse ID.
	 *
	 * @param verseId The ID of the verse.
	 * @return List of verses data.
	 */
	public List<String> getVersesData(int verseId) {
		return tokenDAO.getVersesData(verseId);
	}

	/**
	 * Retrieves tokens for a given verse ID.
	 *
	 * @param verseId The ID of the verse.
	 * @return List of tokens.
	 */
	public List<String> getTokens(int verseId) {
		return tokenDAO.getTokens(verseId);
	}

	/**
	 * Tokenizes verses for a given verse ID.
	 *
	 * @param verseId The ID of the verse to tokenize.
	 */
	public void tokenizeVerses(int verseId) {
		tokenDAO.tokenizeVerses(verseId);
		logger.info("Verses tokenized successfully for Verse ID: " + verseId);
	}

	/**
	 * Retrieves the ID of a token for a given verse ID and token text.
	 *
	 * @param verseId The ID of the verse.
	 * @param token   The token text.
	 * @return The ID of the token.
	 */
	public int getTokenId(int verseId, String token) {
		return tokenDAO.getTokenId(verseId, token);
	}

	/**
	 * Deletes a token by its ID.
	 *
	 * @param tokenId The ID of the token to delete.
	 */
	public void deleteToken(int tokenId) {
		tokenDAO.deleteToken(tokenId);
		logger.info("Token deleted successfully. Token ID: " + tokenId);
	}

	/**
	 * Saves a token with its associated verse ID and POS.
	 *
	 * @param verseId The ID of the verse.
	 * @param token   The token text.
	 * @param pos     The part of speech (POS).
	 */
	public void saveToken(int verseId, String token, String pos) {
		tokenDAO.saveToken(verseId, token, pos);
		logger.info("Token saved successfully. Token: " + token + ", POS: " + pos);
	}

	/**
	 * Updates a token by replacing it with new tokens.
	 *
	 * @param verseId   The ID of the verse.
	 * @param oldToken  The old token text to update.
	 * @param newTokens Array of new token texts.
	 */
	public void updateToken(int verseId, String oldToken, String[] newTokens) {
		int oldTokenId = tokenDAO.getTokenId(verseId, oldToken);
		tokenDAO.updateToken(verseId, oldTokenId, newTokens);
		logger.info(
				"Token updated successfully. Old Token: " + oldToken + ", New Tokens: " + String.join(", ", newTokens));
	}

	/**
	 * Closes the database connection.
	 */
	public void closeDatabaseConnection() {
		tokenDAO.closeConnection();
		logger.info("Database connection closed.");
	}

	/**
	 * Provides a placeholder part of speech (POS) for a given token.
	 *
	 * @param token The token text.
	 * @return The placeholder POS.
	 */
	public String getPOSTagForToken(String token) {
		return "اسم";
	}
}
