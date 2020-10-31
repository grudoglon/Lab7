package database;

import models.Pen;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CollectionDBManager {

    private final Connection connection;

    public CollectionDBManager(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<Pen> getCollection() throws SQLException {
         ArrayList<Pen> collection = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.PEN);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            LocalDate creationDate = rs.getTimestamp("creation_date").toLocalDateTime().toLocalDate();

            Pen pen= new Pen(
                    rs.getLong("id"),
                    rs.getString("name"),
                    creationDate,
                    rs.getDouble("width_of_kernel"),
                    rs.getInt("amount"),
                    rs.getDouble("length_of_kernel"),
                    rs.getDouble("weight"),
                    rs.getBoolean("exists")
            );
            collection.add(pen);
        }
        return collection;
    }

    public boolean hasPermissions(Credentials credentials, int penID) throws SQLException {
        if (credentials.username.equals(UserDBManager.ROOT_USERNAME))
            return true;
        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.USER_HAS_PERMISSIONS);
        int pointer = 0;
        preparedStatement.setInt(1, credentials.id);
        preparedStatement.setInt(2, penID);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("exists");
        }
        return false;
    }

    public String add(Pen pen, Credentials credentials) throws SQLException {
        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Add.PEN);
            preparedStatement.setString(1, pen.getName());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(pen.getCreationDate().atStartOfDay()));
            preparedStatement.setDouble(3, pen.getWidth_of_kernel());
            preparedStatement.setInt(4, pen.getAmount());
            preparedStatement.setDouble(5, pen.getLength_of_kernel());
            preparedStatement.setDouble(6, pen.getWeight());
            preparedStatement.setBoolean(7, pen.getExists());

            ResultSet rs = preparedStatement.executeQuery();
            int penID = 0;
            if (rs.next()) penID = rs.getInt(1);

//            preparedStatement.setInt(1, penID);
//            preparedStatement.executeUpdate();
//
//            preparedStatement.setInt(1, penID);
//            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(SqlQuery.Add.PEN_USER_RELATIONSHIP);
            preparedStatement.setInt(1, credentials.id);
            preparedStatement.setInt(2,penID);
            preparedStatement.executeUpdate();

            connection.commit();

            return String.valueOf(penID);
        } catch (Throwable e) {
            e.printStackTrace();
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }


    public String update(int id, Pen pen, Credentials credentials) throws SQLException {
        if (!hasPermissions(credentials, id))
            return "You don't have permissions";

        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Update.PEN);
            preparedStatement.setString(1, pen.getName());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(pen.getCreationDate().atStartOfDay()));
            preparedStatement.setDouble(3, pen.getWidth_of_kernel());
            preparedStatement.setInt(4, pen.getAmount());
            preparedStatement.setDouble(5, pen.getLength_of_kernel());
            preparedStatement.setDouble(6, pen.getWeight());
            preparedStatement.setBoolean(7, pen.getExists());
            preparedStatement.setInt(8, id);
            preparedStatement.executeUpdate();
//
//            preparedStatement.setInt(3, id);
//            preparedStatement.executeUpdate();
//
//            preparedStatement.setInt(1, id);
//            preparedStatement.executeUpdate();

            connection.commit();

            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }

    public int getPenByID(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Get.PEN_BY_ID);
        int pointer = 0;
        preparedStatement.setInt(++pointer, id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next())
            return rs.getInt(1);
        return -1;
    }

    public String deleteAll(Credentials credentials) throws SQLException {
        if (!credentials.username.equals(UserDBManager.ROOT_USERNAME))
            return "You don't have permissions to delete all database, only root";

        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Delete.ALL_PEN);
            preparedStatement.executeUpdate();
            connection.commit();
            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }


    public String delete(int id, Credentials credentials) throws SQLException {
        final boolean oldAutoCommit = connection.getAutoCommit();
        try {
            int dragonID = getPenByID(id);
            if (!hasPermissions(credentials, dragonID))
                return "You don't have permissions";

            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery.Delete.PEN_BY_ID);
            int pointer = 0;
            preparedStatement.setInt(++pointer, id);
            preparedStatement.executeUpdate();

            connection.commit();

            return null;
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
        }
    }

}
