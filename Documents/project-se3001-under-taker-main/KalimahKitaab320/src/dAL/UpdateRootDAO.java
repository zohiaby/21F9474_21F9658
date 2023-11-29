package dAL;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import transferObject.PoemsTO;

public class UpdateRootDAO extends DALFascade {

	private static final String PROPERTIES_FILE = "db.properties";
	private static Properties properties = new Properties();
	private ManagingDataBase db = new ManagingDataBase();
	private Connection connection;// = db.getConnection();

	public UpdateRootDAO(IUpdateRootDAO iroots) {
		setIUpdateRootDAO(iroots);
		try {
			InputStream input = ManagingDataBase.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);

			if (input == null) {

			}
			try {
				properties.load(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (connection == null || connection.isClosed()) {
				String url = properties.getProperty("db.url");
				String username = properties.getProperty("db.username");
				String password = properties.getProperty("db.password");

				if (password == null) {
					password = ""; // Set an empty password if not provided
				}

				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kalimahkitaab", "root", "");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Function to find the id of verse from verse title
	@Override
	public int getVerseID(PoemsTO poem) {

		int verseId = 0;
		try {
			Statement statement = connection.createStatement();

			if (connection != null) {

				String getIDFromVerseTitle = "SELECT verse_id " + "FROM verse " + "WHERE misra_1 = '"
						+ poem.getMisra_1() + "' " + "AND misra_2  = '" + poem.getMisra_2() + "'";

				ResultSet result = statement.executeQuery(getIDFromVerseTitle);
				while (result.next()) {
					verseId = result.getInt(1);
				}
				return verseId;
			}
			statement.close();
		} catch (SQLException e) {

		}
		return verseId;
	}

	// Function to assign Tokens to verses
	public void saveWords(PoemsTO poem, ArrayList<String> suggRoots) {

		int verseId = getVerseID(poem);
		try {
			Statement statement = connection.createStatement();

			if (connection != null) {

				for (String root : suggRoots) {

					String insertTokenQuery = "INSERT INTO verses_root(verse_id,root_id) " + "VALUES('" + verseId
							+ "', '" + root + "')";

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

				String getVerseData = "SELECT verse1,verse2 FROM verse WHERE id = " + verseId;

				ResultSet result = statement.executeQuery(getVerseData);
				while (result.next()) {
					misra1DB = result.getString("verse1");
					misra2DB = result.getString("verse2");
				}

				if (!misraOne.equals(misra1DB) || !misraTwo.equals(misra2DB)) {

					String createVerseSQLQuery = "INSERT INTO verse (poem_id, verse1, verse2) " + "VALUES ('"
							+ poem.getPoem_id() + "', '" + poem.getMisra_1() + "', '" + poem.getMisra_2() + "')";

					statement.executeUpdate(createVerseSQLQuery);
				}
			}
			statement.close();

		} catch (SQLException e) {
			e.getStackTrace();
		}
	}
}
