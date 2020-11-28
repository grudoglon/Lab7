package database;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static utils.AppConstant.*;

@Slf4j
public class DBConfigure {

    private Connection dbConnection = null;

    public void connect(){
        try {
            String login = System.getProperty("login");
            String password = System.getProperty("password");
            if (login != null && password != null) {
                System.out.println(DB_URL + " : " + login + " : " + password);
                dbConnection = DriverManager.getConnection(DB_URL, login, password);
                System.out.println("Successfully connected to: " + DB_URL);
            }else{
                System.out.println("Ну не получилось, попробуйте в следующий раз");
            }
        }catch (SQLException e) {
            System.out.println("Unable to connect to database");
            e.printStackTrace();
            System.exit(-1);
        }

    }

    public void disconnect() {
        System.out.println("Disconnecting the database...");
        try {
            dbConnection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getDbConnection(){
        return this.dbConnection;
    }
}
