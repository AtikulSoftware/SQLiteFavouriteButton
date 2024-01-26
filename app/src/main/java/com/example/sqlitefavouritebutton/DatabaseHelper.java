package com.example.sqlitefavouritebutton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "users";
    public static final String DB_TABLE_NAME = "users_table";
    public static final int DB_VERSION = 1;
    Context context;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, number TEXT, isFavourite INTEGER) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
    }

    public boolean insertData(String name, String number) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("number", number);
        contentValues.put("isFavourite", 0);

        // data insert করা হয়েছে ।
        long result = database.insert(DB_TABLE_NAME, null, contentValues);

        if (result <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getUsersData() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DB_TABLE_NAME, null);
        if (cursor.getCount() != 0) {
            return cursor;
        }
        return null;
    }

    public void updateAddFavourite(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int value = 1;
            db.execSQL("UPDATE " + DB_TABLE_NAME + " SET isFavourite ='" + value + "' WHERE id =" + id);
            Toast.makeText(context, "Favourite", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateRemoveFavourite(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int value = 0;
            db.execSQL("UPDATE " + DB_TABLE_NAME + " SET isFavourite ='" + value + "' WHERE id =" + id);
            Toast.makeText(context, "Un Favourite", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + DB_TABLE_NAME + " WHERE id LIKE "+id);
            Toast.makeText(context, "Delete successful", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

} // DatabaseHelper end here ==================
