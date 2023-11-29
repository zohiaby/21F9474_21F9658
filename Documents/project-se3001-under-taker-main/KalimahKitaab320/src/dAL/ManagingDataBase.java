package dAL;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ManagingDataBase {

    private static Connection connection;
    private static final String PROPERTIES_FILE = "db.properties";
    private static Properties properties = new Properties();

    // Load Database Info(URL, Username, Password) from DB.properties file
    public ManagingDataBase() {
        try {
            InputStream input = ManagingDataBase.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);

            if (input == null) {
            	
            }

            properties.load(input);

//            Class.forName(properties.getProperty("db.driver"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String url = properties.getProperty("db.url");
                String username = properties.getProperty("db.username");
                String password = properties.getProperty("db.password");
                
                if (password == null) {
                    password = ""; // Set an empty password if not provided
                }

                connection = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
