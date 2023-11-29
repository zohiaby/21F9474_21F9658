package dAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import transferObject.RootDTO;

public class RootDAO implements IDALRoot {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/kalimahkitaab";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";
	private RootDTO rootDTO;

	@Override
	public void setDTO(RootDTO root) {
		rootDTO = root;
	}

	@Override
	public void addRootsInDB(RootDTO root) {
		try {
			// Establish a database connection
			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

			// Define the SQL query
			String sql = "insert into roots (root_name) values(?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, root.getRoot());

			// Execute the query and get the result set
			int resultSet = preparedStatement.executeUpdate();

			if (resultSet > 0) {
				System.out.println("Root Added.");
			} else {
				System.out.println("Root not Added.");
			}

			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteRootInDB(RootDTO root) {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

			String sql = "delete from roots where root_name=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, root.getRoot());

			int resultSet = preparedStatement.executeUpdate();

			if (resultSet > 0) {
				System.out.println("Root Deleted.");
			} else {
				System.err.println("Root not Deleted.");
			}

			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error deleting root: " + e.getMessage());
		}
	}

	public int getRootIDFromDB(RootDTO root) {
		int rootID = 0;
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

			String sql = "SELECT root_id FROM roots WHERE root_name = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, root.getRoot());

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				rootID = resultSet.getInt("root_id");
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rootID;
	}

	@Override
	public void updateRootInDB(RootDTO root, int id) {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

			String sql = "UPDATE roots SET root_name = ? WHERE root_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, root.getRoot());
			preparedStatement.setInt(2, id);

			int resultSet = preparedStatement.executeUpdate();

			if (resultSet > 0) {
				System.out.println("Root Updated.");
			} else {
				System.err.println("Root not Updated.");
			}

			preparedStatement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error updating root: " + e.getMessage());
		}
	}

	@Override
	public Map<Integer, RootDTO> getRootsAndIDsFromDB() {
	    Map<Integer, RootDTO> rootsWithIDs = new HashMap<>();
	    try {
	        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

	        String sql = "SELECT root_id, root_name FROM roots";
	        PreparedStatement preparedStatement = connection.prepareStatement(sql);

	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            int rootID = resultSet.getInt("root_id");
	            String rootName = resultSet.getString("root_name");

	            RootDTO root = new RootDTO();
	            root.setRootID(rootID);
	            root.setRoot(rootName);

	            rootsWithIDs.put(rootID, root);
	        }

	        preparedStatement.close();
	        connection.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.err.println("Error retrieving roots and IDs: " + e.getMessage());
	    }

	    return rootsWithIDs;
	}


}
