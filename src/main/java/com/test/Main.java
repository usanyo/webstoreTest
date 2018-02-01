package com.test;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MySQLAccess mySQLAccess = new MySQLAccess();
        try {
            System.out.println("Hello Gradle!");
            mySQLAccess.connect();

            JSONParser jsonParser = new JSONParser();
            Object root = jsonParser.parse(new FileReader("src/main/resources/input.json"));
            JSONArray jsonItems = (JSONArray) ((JSONObject) root).get("items");
            List<PurchasedItem> purchasedItems = new ArrayList<>();

            HashMap<Long, Double> priceTable = mySQLAccess.getPrices();

            for (Object o : jsonItems)
                purchasedItems.add(objectToPurchasedItem((JSONObject) o, priceTable));

            purchasedItems.forEach(purchasedItem -> {
                try {
                    mySQLAccess.registerPurchase(purchasedItem);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            final BufferedWriter writer = new BufferedWriter(new FileWriter("report.csv"));
            mySQLAccess.getTransactions().forEach((aLong, aDouble) -> {
                try {
                    writer.write(aLong.toString() + "," + aDouble.toString() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            mySQLAccess.close();
        }
    }

    private static PurchasedItem objectToPurchasedItem(JSONObject o, HashMap<Long, Double> priceTable) {
        return new PurchasedItem(
                Long.valueOf(o.get("trxId").toString()),
                Date.valueOf(o.get("purchaseTime").toString()),
                Long.valueOf(o.get("itemId").toString()),
                Short.valueOf(o.get("quantity").toString()),
                o.get("itemName").toString(),
                o.get("description").toString(),
                priceTable.get(Long.valueOf(o.get("itemId").toString()))
                );
    }
}

