package com.example.adam.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.DatabaseErrorHandler;
import android.util.Log;

/**
 * Created by Forrest on 3/25/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "property_table";
    private static final String COL0 = "address";
    private static final String COL1 = "city";
    private static final String COL2 = "state";
    private static final String COL3 = "zipcode";
    private static final String COL4 = "type";
    private static final String COL5 = "price";
    private static final String COL6 = "payment";
    private static final String COL7 = "apr";
    private static final String COL8 = "terms";
    private static final String COL9 = "monthly_payment";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL0 + " TEXT, " + COL1 + " TEXT, "
                + COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT, " + COL5 + " TEXT, " + COL6 +
                " TEXT, " + COL7 + " TEXT, " + COL8 + " TEXT, " + COL9 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTable);
        onCreate(db);
    }

    public boolean addData(String address, String city, String state, String zipcode, String type,
                           String price, String payment, String apr, String terms, String monthly_payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL0, address);
        contentValues.put(COL1, city);
        contentValues.put(COL2, state);
        contentValues.put(COL3, zipcode);
        contentValues.put(COL4, type);
        contentValues.put(COL5, price);
        contentValues.put(COL6, payment);
        contentValues.put(COL7, apr);
        contentValues.put(COL8, terms);
        contentValues.put(COL9, monthly_payment);

        Log.d(TAG, "addDate: Adding " + address + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getRecord(String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL0 + "= '" + address + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //清掉
    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+ TABLE_NAME;
        db.execSQL(clearDBQuery);
    }

    public void deleteRow(String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE " + COL0 + "= '" + address + "'");
    }

    public void updateRow(String address) {
        SQLiteDatabase db = this.getWritableDatabase();
    }
}
