package ro.ugal.aciee.bettingagency.utils.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String URL_KEY = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME_KEY = "postgres";
    private static final String PASSWORD_KEY = "naifor";

    public static Connection open(){
        try{
            return DriverManager.getConnection(URL_KEY, USERNAME_KEY, PASSWORD_KEY);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
