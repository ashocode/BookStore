package com.example.android.bookstoreinventory;

/**
 * Created by Asen.Pichurov on 01-Jun-18.
 */

public interface InventoryItem {
    String getItemName();

    int getItemPrice();

    int getItemQuantity();

    String getSupplierName();

    String getSupplierPhone();
}
