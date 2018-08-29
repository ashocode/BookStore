package com.example.android.bookstoreinventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bookstoreinventory.data.InventoryContract.InventoryDBEntry;

/**
 * Created by Asen.Pichurov on 01-Jun-18.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_INVENTORY_TABLE = "CREATE TABLE " + InventoryDBEntry.TABLE_NAME + " ("
                + InventoryDBEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryDBEntry.NAME + " TEXT NOT NULL, "
                + InventoryDBEntry.PRICE + " INTEGER NOT NULL, "
                + InventoryDBEntry.QUANTITY + " INTEGER NOT NULL, "
                + InventoryDBEntry.SUPPLIER_NAME + " TEXT NOT NULL, "
                + InventoryDBEntry.SUPPLIER_PHONE + " TEXT NOT NULL);";

        db.execSQL(CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
