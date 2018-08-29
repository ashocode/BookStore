package com.example.android.bookstoreinventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.bookstoreinventory.data.InventoryContract.InventoryDBEntry;

/**
 * Created by Asen.Pichurov on 20-Jun-18.
 */

public class ItemProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = ItemProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the pets table
     */
    private static final int ITEMS = 100;

    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int ITEM_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code
     */
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_ITEMS, ITEMS);

        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_ITEMS + "/#", ITEM_ID);
    }

    private InventoryDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor;

        int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                cursor = database.query(InventoryDBEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case ITEM_ID:
                selection = InventoryDBEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(InventoryDBEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return InventoryDBEntry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return InventoryDBEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String name = values.getAsString(InventoryDBEntry.NAME);
        if (name == null) {
            throw new IllegalArgumentException("The item requires a name");
        }

        Integer price = values.getAsInteger(InventoryDBEntry.PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("The item requires a valid price");
        }

        Integer quantity = values.getAsInteger(InventoryDBEntry.QUANTITY);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("The item requires a valid quantity");
        }

        String supplierName = values.getAsString(InventoryDBEntry.SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("The item requires a supplier's name");
        }

        String supplierPhone = values.getAsString(InventoryDBEntry.SUPPLIER_PHONE);
        if (supplierPhone == null) {
            throw new IllegalArgumentException("The item requires a supplier's phone");
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long id = database.insert(InventoryDBEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        /** Notify all listeners that the data has changed for the pet content URI*/
        getContext().getContentResolver().notifyChange(uri, null);

        /** Return the new URI with the ID (of the newly inserted row) appended at the end*/
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = uriMatcher.match(uri);

        switch (match) {
            case ITEMS:
                /** Delete all rows*/
                rowsDeleted = database.delete(InventoryDBEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM_ID:
                /** Delete a single row*/
                selection = InventoryDBEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(InventoryDBEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        /** Return the number of rows deleted*/
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEM_ID:
                selection = InventoryDBEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(InventoryDBEntry.NAME)) {
            String name = values.getAsString(InventoryDBEntry.NAME);
            if (name == null) {
                throw new IllegalArgumentException("The item requires a name");
            }
        }

        if (values.containsKey(InventoryDBEntry.PRICE)) {
            Integer price = values.getAsInteger(InventoryDBEntry.PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("The item requires a valid price");
            }
        }

        if (values.containsKey(InventoryDBEntry.QUANTITY)) {
            Integer quantity = values.getAsInteger(InventoryDBEntry.QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("The item requires a valid quantity");
            }
        }

        if (values.containsKey(InventoryDBEntry.SUPPLIER_NAME)) {
            String supplierName = values.getAsString(InventoryDBEntry.SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("The item requires a supplier's name");
            }
        }

        if (values.containsKey(InventoryDBEntry.SUPPLIER_PHONE)) {
            String supplierPhone = values.getAsString(InventoryDBEntry.SUPPLIER_PHONE);
            if (supplierPhone == null) {
                throw new IllegalArgumentException("The item requires a supplier's phone");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int rowsUpdated = database.update(InventoryDBEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
