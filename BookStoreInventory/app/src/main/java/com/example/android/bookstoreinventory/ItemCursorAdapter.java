package com.example.android.bookstoreinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.bookstoreinventory.data.InventoryContract.InventoryDBEntry;

/**
 * Created by Asen.Pichurov on 20-Jun-18.
 */

public class ItemCursorAdapter extends CursorAdapter {

    Context passedContext;

    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        passedContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(InventoryDBEntry._ID);
        final int rowId = cursor.getInt(columnIndex);
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);

        int nameColumnIndex = cursor.getColumnIndex(InventoryDBEntry.NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryDBEntry.PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryDBEntry.QUANTITY);

        String itemName = cursor.getString(nameColumnIndex);
        String itemPrice = cursor.getString(priceColumnIndex);
        String itemQuantity = cursor.getString(quantityColumnIndex);

        String priceText = view.getResources().getString(R.string.price_text);
        String priceCurrency = view.getResources().getString(R.string.price_currency);
        String price = priceText + itemPrice + priceCurrency;
        String quantityText = view.getResources().getString(R.string.quantity_text);
        String quantity = quantityText + itemQuantity;
        nameTextView.setText(itemName);
        priceTextView.setText(price);
        quantityTextView.setText(quantity);

        Button buyButton = (Button) view.findViewById(R.id.buy_button);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri currentBookUri = ContentUris.withAppendedId(InventoryDBEntry.CONTENT_URI, rowId);
                sellBook(currentBookUri);
            }
        });
    }

    public void sellBook(Uri book) {
        ContentValues values = new ContentValues();

        String[] projection = {InventoryDBEntry.QUANTITY};

        Cursor cursor = passedContext.getContentResolver().query(book, projection, null, null, null);

        if (cursor.moveToFirst()) {
            int quantityColumnIndex = cursor.getColumnIndex(InventoryDBEntry.QUANTITY);
            int quantity = cursor.getInt(quantityColumnIndex);
            if (quantity > 0) {
                quantity -= 1;
            }
            values.put(InventoryDBEntry.QUANTITY, quantity);
            int rowsAffected = passedContext.getContentResolver().update(book, values, null, null);
        }

        cursor.close();
    }
}
