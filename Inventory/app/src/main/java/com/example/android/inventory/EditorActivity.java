package com.example.android.inventory;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventory.data.BookDbHelper;
import com.example.android.inventory.data.BookContract.BookEntry;

public class EditorActivity extends AppCompatActivity {

    /** EditText field to enter the book's name */
    private EditText mNameEditText;

    /** EditText field to enter the book's price */
    private EditText mPriceEditText;

    /** EditText field to enter the book's quantity in stock */
    private EditText mQuantitytEditText;

    /** EditText field to enter the book's supplier */
    private Spinner mSupplierSpinner;
    
    /** EditText field to enter the book's supplier's phone number */
    private EditText mPhoneEditText;

    /**
     * Supplier of the book. The possible values are:
     * 0 for unknown, 1 for Oxford University Press, 2 for Cambridge University Press, 3 for 
     * Penguin, 4 for MacMillan.
     */
    private int mSupplier = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_product_price);
        mQuantitytEditText = (EditText) findViewById(R.id.edit_product_quantity);
        mSupplierSpinner = (Spinner) findViewById(R.id.spinner_supplier);
        mPhoneEditText = (EditText) findViewById(R.id.edit_phone_number); 

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the book.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSupplierSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mSupplierSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_oxford))) {
                        mSupplier = BookEntry.SUPPLIER_OXFORD; // Oxford University Press
                    } else if (selection.equals(getString(R.string.supplier_cambridge))) {
                        mSupplier = BookEntry.SUPPLIER_CAMBRIDGE; // Cambridge University Press
                    } else if (selection.equals(getString(R.string.supplier_penguin))) {
                        mSupplier = BookEntry.SUPPLIER_PENGUIN; // Penguin Books
                    } else if (selection.equals(getString(R.string.supplier_macmillan))) {
                        mSupplier = BookEntry.SUPPLIER_MACMILLAN; // MacMillan
                    } else {
                        mSupplier = BookEntry.SUPPLIER_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplier = 0; // Unknown
            }
        });
    }

    // Method to add a book
    private void insertBook() {

        // Instantiate BookDbHelper with context
        BookDbHelper mDbHelper = new BookDbHelper(this);

        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantitytEditText.getText().toString().trim();
        String phoneString = mPhoneEditText.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);

        // Create a writeable form of the database this time
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Add data to columns to shove into database
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME, nameString);
        values.put(BookEntry.COLUMN_BOOK_PRICE, priceString);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER, mSupplier);
        values.put(BookEntry.COLUMN_BOOK_PHONE, phoneString);

        // Insert a new row for the new book in the database, returning the ID of that new row.
        // The first argument for db.insert() is the books table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for the new book.
        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error generating new book", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "New book added with row ID " + newRowId, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save book to database
                insertBook();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

