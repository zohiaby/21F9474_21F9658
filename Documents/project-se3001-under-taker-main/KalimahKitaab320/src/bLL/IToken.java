package bLL;

import java.util.List;

public interface IToken {

	List<String> getVersesData(int verseId);

	List<String> getTokens(int verseId);

	void tokenizeVerses(int verseId);

	int getTokenId(int verseId, String token);

	void deleteToken(int tokenId);

	void saveToken(int verseId, String token, String pos);

	void updateToken(int verseId, String oldToken, String[] newTokens);

	void closeDatabaseConnection();

	String getPOSTagForToken(String token);
}
