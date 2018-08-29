package com.example.android.bookstoreinventory;

import android.util.Log;

/**
 * Created by Asen.Pichurov on 01-Jun-18.
 */

public class BookInventoryItem implements InventoryItem {

    private static final String LOG_TAG = BookInventoryItem.class.getSimpleName();
    private String bookName = "Empty";
    private int bookPrice = 0;
    private int bookQuantity = 0;
    private String bookSupplierName = "Empty";
    private String bookSupplierPhone = "Empty";


    BookInventoryItem(String name, int price, int quantity, String supplierName, String supplierPhone) {
        try {
            this.bookName = name;
            this.bookPrice = price;
            this.bookQuantity = quantity;
            this.bookSupplierName = supplierName;
            this.bookSupplierPhone = supplierPhone;
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "BookInventoryItem: Some of the parameters in the constructor is Null. ", e);
        }

    }

    @Override
    public String getItemName() {
        return this.bookName;
    }

    @Override
    public int getItemPrice() {
        return this.bookPrice;
    }

    @Override
    public int getItemQuantity() {
        return this.bookQuantity;
    }

    @Override
    public String getSupplierName() {
        return this.bookSupplierName;
    }

    @Override
    public String getSupplierPhone() {
        return this.bookSupplierPhone;
    }
}
