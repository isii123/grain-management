import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class test_db_connection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://127.0.0.1:5432/grain_management";
        String username = "grain_management_user";
        String password = "369515";

        try {
            System.out.println("Testing PostgreSQL connection...");
            System.out.println("URL: " + url);
            System.out.println("Username: " + username);

            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successful!");

            // Test query
            java.sql.Statement stmt = connection.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery("SELECT 1");
            if (rs.next()) {
                System.out.println("Query result: " + rs.getInt(1));
            }

            connection.close();
            System.out.println("Connection closed.");

        } catch (SQLException e) {
            System.out.println("Connection failed!");
            System.out.println("Error message: " + e.getMessage());
            System.out.println("SQL state: " + e.getSQLState());
            e.printStackTrace();
        }
    }
}