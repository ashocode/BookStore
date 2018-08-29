package com.example.android.bookstoreinventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Asen.Pichurov on 01-Jun-18.
 */

public final class InventoryContract {

    public InventoryContract() {
    }

    /** Defining the name of the content provider*/
    public static final String CONTENT_AUTHORITY = "com.example.android.bookstoreinventory";

    /**Using the CONTENT_AUTHORITY to create the base of all URI*/
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**Defining a possible path the ContentProvider will work with*/
    public static final String PATH_ITEMS = "items";

    public static final class InventoryDBEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_ITEMS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
        public static final String TABLE_NAME = "items";
        public static final String _ID = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String PRICE = "price";
        public static final String QUANTITY = "quantity";
        public static final String SUPPLIER_NAME = "supplier_name";
        public static final String SUPPLIER_PHONE = "supplier_phone";
    }
}
