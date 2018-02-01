package com.test;

import java.sql.Date;

public class PurchasedItem {
    public final long trxId;
    public final Date purchaseTime;
    public final long itemId;
    public final short quantity;
    public final String itemName;
    public final String description;
    private final double price;

    public PurchasedItem(
            long trxId,
            Date purchaseTime,
            long itemId,
            short quantity,
            String itemName,
            String description,
            double price
    ) {

        this.trxId = trxId;
        this.purchaseTime = purchaseTime;
        this.itemId = itemId;
        this.quantity = quantity;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
    }

    double getTotalPrice() {
        return quantity*price;
    }
}
