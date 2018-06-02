package com.example.android.inventory.data;

import android.provider.BaseColumns;

public final class BookContract {

    private BookContract() {}

    public static final class BookEntry implements BaseColumns {

        public static final String TABLE_NAME = "books";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_BOOK_NAME = "name";
        public static final String COLUMN_BOOK_PRICE = "price";
        public static final String COLUMN_BOOK_QUANTITY = "quantity";
        public static final String COLUMN_BOOK_SUPPLIER = "supplier";
        public static final String COLUMN_BOOK_PHONE = "phone";

        public static final int SUPPLIER_UNKNOWN = 0;
        public static final int SUPPLIER_OXFORD = 1;
        public static final int SUPPLIER_CAMBRIDGE = 2;
        public static final int SUPPLIER_PENGUIN = 3;
        public static final int SUPPLIER_MACMILLAN = 4;
    }
}