package application;

import java.sql.Connection;
import java.sql.DriverManager;

public class database {
    public static Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/flower";
            String user = "root";
            String password = "";

            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
