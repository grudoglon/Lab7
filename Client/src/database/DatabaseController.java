package database;

import models.Pen;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseController {

    private final CollectionDBManager collectionDBManager;
    private final UserDBManager userDBManager;

    public DatabaseController(CollectionDBManager collectionDBManager, UserDBManager userDBManager) {
        this.collectionDBManager = collectionDBManager;
        this.userDBManager = userDBManager;
    }

    public ArrayList<Pen> getCollectionFromDB() throws SQLException {
        ArrayList<Pen> collection = collectionDBManager.getCollection();
        if (collection == null)
            throw new SQLException("It was not possible to fetch the collection from database");
        return collection;
    }

    public Object login(Credentials credentials) {
        try {
            int id = userDBManager.checkUserAndGetID(credentials);
            if (id > 0)
                return new Credentials(id, credentials.username, credentials.password);
            else
                return "User/Password given not found or incorrect";
        } catch (SQLException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public Object register(Credentials credentials) {
        try {
            int id = userDBManager.registerUser(credentials);
            if (id > 0)
                return new Credentials(id, credentials.username, credentials.password);
            else
                return credentials;
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }



    public String addPen(Pen pen, Credentials credentials) {
        try {
            return collectionDBManager.add(pen, credentials);
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }

    public String updatePen(int id, Pen pen, Credentials credentials) {
        try {
            return collectionDBManager.update(id, pen, credentials);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public String clearPen(Credentials credentials) {
        try {
            return collectionDBManager.deleteAll(credentials);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    public String removePen(int id, Credentials credentials) {
        try {
            return collectionDBManager.delete(id, credentials);
        } catch (Throwable ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }
}
