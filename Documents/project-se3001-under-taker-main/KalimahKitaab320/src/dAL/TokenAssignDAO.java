package dAL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import transferObject.PoemsTO;

public class TokenAssignDAO extends DALFascade {

	private ManagingDataBase db = new ManagingDataBase();
	private Connection connection = db.getConnection();

	public TokenAssignDAO(ITokenAssignDAO itokens) {
		setITokenAssignDAO(itokens);
	}

	
	// Function to find the id of verse from verse title
	public int getVerseID(PoemsTO poem) {

		int verseId = 0;
		try {
			Statement statement = connection.createStatement();

			if (connection != null) {

				String getIDFromVerseTitle = "SELECT id " + "FROM verse " + "WHERE misra_1 = '" + poem.getMisra_1()
						+ "' " + "AND misra_2  = '" + poem.getMisra_2() + "'";

				ResultSet result = statement.executeQuery(getIDFromVerseTitle);
				while (result.next()) {
					verseId = result.getInt(1);
				}
				return verseId;
			}
			statement.close();
		} catch (SQLException e) {
			e.getMessage();
		}
		return verseId;
	}

	
	
	// Function to assign Tokens to verses
	public void saveWords(PoemsTO poem, ArrayList<String> tokens) {

		int verseId = getVerseID(poem);
		try {
			Statement statement = connection.createStatement();

			if (connection != null) {

				for (String token : tokens) {

					String insertTokenQuery = "INSERT INTO tokens_verses(token, verse_id) " + "VALUES('" + token
							+ "', '" + verseId + "')";

					statement.executeUpdate(insertTokenQuery);
				}
			}
			statement.close();
		} catch (SQLException e) {

		}
	}

	public void checkWhetherInsertionOrNot(PoemsTO poem, String misraOne, String misraTwo) {

		try {
			int verseId = getVerseID(poem);
			String misra1DB = null, misra2DB = null;
			Statement statement = connection.createStatement();

			if (connection != null) {

				String getVerseData = "SELECT * FROM verse WHERE id = " + verseId;

				ResultSet result = statement.executeQuery(getVerseData);
				while (result.next()) {
					misra1DB = result.getString("misra_1");
					misra2DB = result.getString("misra_2");
				}

				if (!misraOne.equals(misra1DB) || !misraTwo.equals(misra2DB)) {
					
					String createVerseSQLQuery = "INSERT INTO verse (misra_1, misra_2, poem_id) " + "VALUES ('"
							+ poem.getMisra_1() + "', '" + poem.getMisra_2() + "', '" + poem.getPoem_id() + "')";

					statement.executeUpdate(createVerseSQLQuery);
				}
			}
			statement.close();

		} catch (SQLException e) {
			e.getStackTrace();
		}
	}
}