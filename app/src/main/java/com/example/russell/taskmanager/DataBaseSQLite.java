package com.example.russell.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DataBaseSQLite {
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private DBHelper dbHelper;
    public static final String DB_NAME = "NOTES_DB";
    public static final int DB_VERSION = 1;
    public static final String DB_TABLE = "NOTES_TABLE";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    public static final String DB_CREATE = "create table " + DB_TABLE + "(" +
           COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " text" +
            ");";

    public DataBaseSQLite(Context context) {
        this.context = context;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void open() {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void write(String note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, note);
        sqLiteDatabase.insert(DB_TABLE, null, contentValues);
    }

    public void delete(String text) {
        sqLiteDatabase.delete(DB_TABLE, COLUMN_NAME + " = ?", new String[] {text});
    }

    public ArrayList<String> read() {
        Cursor cursor = sqLiteDatabase.query(DB_TABLE, null, null, null, null, null, null);
        if (!cursor.moveToFirst()) return null;
        ArrayList<String> notes = new ArrayList<>();
        do {
            String note = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            notes.add(note);
        } while (cursor.moveToNext());
        cursor.close();
        return notes;
    }
}