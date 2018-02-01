package com.test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MySQLAccess {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connect = DriverManager.getConnection("jdbc:mysql://localhost/mydb?user=root&password=pokmalac");

        statement = connect.createStatement();
    }

    public HashMap<Long, Double> getPrices() throws SQLException {
        ResultSet queryResult = statement.executeQuery("select * from mydb.prices");

        HashMap<Long, Double> result = new HashMap<>();
        while(queryResult.next())
            result.put(queryResult.getLong("id"),queryResult.getDouble("price"));

        return result;
    }

    public void registerPurchase(PurchasedItem purchasedItem) throws SQLException {
        preparedStatement = connect.prepareStatement("insert into  mydb.transactions values (default, ?,?,?)");
        preparedStatement.setBigDecimal(1, new BigDecimal(purchasedItem.trxId));
        preparedStatement.setLong(2, purchasedItem.itemId);
        preparedStatement.setDouble(3, purchasedItem.getTotalPrice());
        preparedStatement.executeUpdate();
    }

    public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

    public HashMap<Long, Double> getTransactions() throws SQLException {
        ResultSet queryResult = statement.executeQuery("select trxId,sum(price) from transactions group by trxId;");

        HashMap<Long, Double> result = new HashMap<>();
        while(queryResult.next())
            result.put(queryResult.getLong(1),queryResult.getDouble(2));
        return result;
    }
}