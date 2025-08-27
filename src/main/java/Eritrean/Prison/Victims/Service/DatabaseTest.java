package Eritrean.Prison.Victims.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://database-1.c2hmeuqo6il0.us-east-1.rds.amazonaws.com:3306/Eritrean_Prison_Victims_DB";
        String username = "admin";
        String password = "Fvalcon1728$";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            if (connection != null) {
                System.out.println(" Connection successful!");
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}

