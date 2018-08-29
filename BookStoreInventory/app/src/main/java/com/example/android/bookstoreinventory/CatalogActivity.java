package com.example.android.bookstoreinventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.bookstoreinventory.data.InventoryContract.InventoryDBEntry;


public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = CatalogActivity.class.getSimpleName();
    private static final int BOOK_LOADER = 0;
    ItemCursorAdapter cursorAdapter;
    InventoryItem firstBook;
    InventoryItem secondBook;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        contentValues = new ContentValues();

        String firstBookName = getResources().getString(R.string.dummy_book_one_name);
        int firstBookPrice = Integer.parseInt(getResources().getString(R.string.dummy_book_one_price));
        int firstBookQuantity = Integer.parseInt(getResources().getString(R.string.dummy_book_one_quantity));
        String firstBookSupplierName = getResources().getString(R.string.dummy_book_one_supplier_name);
        String firstBookSupplierPhone = getResources().getString(R.string.dummy_book_one_supplier_phone);
        String secondBookName = getResources().getString(R.string.dummy_book_two_name);
        int secondBookPrice = Integer.parseInt(getResources().getString(R.string.dummy_book_two_price));
        int secondBookQuantity = Integer.parseInt(getResources().getString(R.string.dummy_book_two_quantity));
        String secondBookSupplierName = getResources().getString(R.string.dummy_book_two_supplier_name);
        String secondBookSupplierPhone = getResources().getString(R.string.dummy_book_two_supplier_phone);

        firstBook = new BookInventoryItem(firstBookName, firstBookPrice,
                firstBookQuantity, firstBookSupplierName, firstBookSupplierPhone);

        secondBook = new BookInventoryItem(secondBookName, secondBookPrice,
                secondBookQuantity, secondBookSupplierName, secondBookSupplierPhone);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView bookListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        cursorAdapter = new ItemCursorAdapter(this, null);
        bookListView.setAdapter(cursorAdapter);

        // Setup the item click listener
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri currentBookUri = ContentUris.withAppendedId(InventoryDBEntry.CONTENT_URI, id);
                intent.setData(currentBookUri);

                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertData(firstBook, contentValues);
                insertData(secondBook, contentValues);
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllBooks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertData(InventoryItem item, ContentValues values) {

        values.put(InventoryDBEntry.NAME, item.getItemName());
        values.put(InventoryDBEntry.PRICE, item.getItemPrice());
        values.put(InventoryDBEntry.QUANTITY, item.getItemQuantity());
        values.put(InventoryDBEntry.SUPPLIER_NAME, item.getSupplierName());
        values.put(InventoryDBEntry.SUPPLIER_PHONE, item.getSupplierPhone());

        Uri newUri = getContentResolver().insert(InventoryDBEntry.CONTENT_URI, values);
        values.clear();
    }

    private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(InventoryDBEntry.CONTENT_URI, null, null);
        Log.v(LOG_TAG, rowsDeleted + getResources().getString(R.string.log_rows_deleted));
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                InventoryDBEntry._ID,
                InventoryDBEntry.NAME,
                InventoryDBEntry.PRICE,
                InventoryDBEntry.QUANTITY,
                InventoryDBEntry.SUPPLIER_NAME,
                InventoryDBEntry.SUPPLIER_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,    // Parent activity context
                InventoryDBEntry.CONTENT_URI,   // Provider content URI to query
                projection,                     // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                // No selection arguments
                null);                 // Default sort order
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
