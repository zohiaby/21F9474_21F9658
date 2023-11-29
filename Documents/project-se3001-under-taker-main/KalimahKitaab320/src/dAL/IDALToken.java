package dAL;

import java.util.List;

public interface IDALToken {

	List<String> getVersesData(int verseId);

	List<String> getTokens(int verseId);

	void tokenizeVerses(int verseId);

	void saveToken(int verseId, String token, String pos);

	void updateToken(int verseId, int oldTokenId, String[] newTokens);

	int getTokenId(int verseId, String token);

	void deleteToken(int tokenId);

	void closeConnection();
}
