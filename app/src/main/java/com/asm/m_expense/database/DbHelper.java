package com.asm.m_expense.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.asm.m_expense.database.models.ModelExpense;
import com.asm.m_expense.database.models.ModelTrip;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constants.CREATE_TRIP_TABLE);
        sqLiteDatabase.execSQL(Constants.CREATE_EXPENSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_TRIP_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_EXPENSE_NAME);
        onCreate(sqLiteDatabase);
    }

    // Insert trip data to sqlite
    public void insertTrip(String nameTrip, String destinationTrip, String dateTrip, String riskTrip, String descriptionTrip) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Constants.C_NAME_TRIP, nameTrip);
            values.put(Constants.C_DESTINATION_TRIP, destinationTrip);
            values.put(Constants.C_DATE_TRIP, dateTrip);
            values.put(Constants.C_RISK_TRIP, riskTrip);
            values.put(Constants.C_DESCRIPTION_TRIP, descriptionTrip);
            // Insert the new row, returning the primary key value of the new row
            db.insert(Constants.TABLE_TRIP_NAME, null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // Update trip data to sqlite
    public void updateTrip(String id, String nameTrip, String destinationTrip, String dateTrip, String riskTrip, String descriptionTrip) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Constants.C_NAME_TRIP, nameTrip);
        values.put(Constants.C_DESTINATION_TRIP, destinationTrip);
        values.put(Constants.C_DATE_TRIP, dateTrip);
        values.put(Constants.C_RISK_TRIP, riskTrip);
        values.put(Constants.C_DESCRIPTION_TRIP, descriptionTrip);
        // Update row based id
        db.update(Constants.TABLE_TRIP_NAME, values, Constants.C_ID_TRIP + " =? ", new String[]{id});
        db.close();
    }

    // Get all trip from sqlite to array list
    public ArrayList<ModelTrip> getAllTrip() {
        ArrayList<ModelTrip> tripArrayList = new ArrayList<>();
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        // Sql command
        String selectQuery = "SELECT * FROM " + Constants.TABLE_TRIP_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Lopping through all records and add to list
        if (cursor.moveToFirst()) {
            do {
                ModelTrip modelTrip = new ModelTrip(
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ID_TRIP)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME_TRIP)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DESTINATION_TRIP)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DATE_TRIP)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_RISK_TRIP)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DESCRIPTION_TRIP))
                );
                tripArrayList.add(modelTrip);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tripArrayList;
    }

    // Search trip by name
    public ArrayList<ModelTrip> searchTripByName(String query) {
        ArrayList<ModelTrip> tripArrayList = new ArrayList<>();
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        // Sql command to search
        String queryToSearch = "SELECT * FROM " + Constants.TABLE_TRIP_NAME
                + " WHERE " + Constants.C_NAME_TRIP + " LIKE '%" + query + "%'";
        Cursor cursor = db.rawQuery(queryToSearch, null);
        // Lopping through all records and add to list
        if (cursor.moveToFirst()) {
            do {
                ModelTrip modelTrip = new ModelTrip(
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ID_TRIP)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME_TRIP)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DESTINATION_TRIP)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DATE_TRIP)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_RISK_TRIP)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_DESCRIPTION_TRIP))
                );
                tripArrayList.add(modelTrip);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tripArrayList;
    }

    // Delete all trip from sqlite
    public void deleteAllTrip() {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Query to delete all
            db.execSQL("DELETE FROM " + Constants.TABLE_TRIP_NAME);
            db.execSQL("DELETE FROM " + Constants.TABLE_EXPENSE_NAME);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // Insert expense with id trip fk to sqlite
    public void insertExpense(String typeExpense, String amountExpense, String timeExpense, String commentExpense, String idTripFk) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Constants.C_TYPE_EXPENSE, typeExpense);
            values.put(Constants.C_AMOUNT_EXPENSE, amountExpense);
            values.put(Constants.C_TIME_EXPENSE, timeExpense);
            values.put(Constants.C_COMMENT_EXPENSE, commentExpense);
            values.put(Constants.C_ID_TRIP_FK, idTripFk);
            // Insert the new row, returning the primary key value of the new row
            db.insert(Constants.TABLE_EXPENSE_NAME, null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // Read expense by id trip fk
    public ArrayList<ModelExpense> readByIdTrip(String id) {
        ArrayList<ModelExpense> expenseArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        // Sql command
        String selectQuery = "SELECT * FROM "+ Constants.TABLE_EXPENSE_NAME + " WHERE " + Constants.C_ID_TRIP_FK + " =\"" + id + "\"";
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Lopping through all records and add to list
        if (cursor.moveToFirst()) {
            do {
                ModelExpense modelExpense = new ModelExpense(
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ID_EXPENSE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_TYPE_EXPENSE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_AMOUNT_EXPENSE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_TIME_EXPENSE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_COMMENT_EXPENSE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ID_TRIP_FK))
                );
                expenseArrayList.add(modelExpense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenseArrayList;
    }

    // Sync data to api

}
