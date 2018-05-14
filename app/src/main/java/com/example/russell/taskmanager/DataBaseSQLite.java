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
    private static final String DB_NAME = "NOTES_DB";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "NOTES_TABLE";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_DATE = "date";

    private static final String DB_CREATE = "create table " + DB_TABLE + "(" +
           COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_TEXT + " text, " +
            COLUMN_DATE + " text" +
            ");";

    DataBaseSQLite(Context context) {
        this.context = context;
    }

    private class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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

    public void write(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TEXT, note.getText());
        contentValues.put(COLUMN_DATE, note.getDate());
        long id = sqLiteDatabase.insert(DB_TABLE, null, contentValues);
        note.setId((int)id);
    }

    public void update(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TEXT, note.getText());
        contentValues.put(COLUMN_DATE, note.getDate());
        sqLiteDatabase.update(DB_TABLE, contentValues, COLUMN_ID + " = ?", new String[] {String.valueOf(note.getId())});
    }

    public void delete(int id) {
        sqLiteDatabase.delete(DB_TABLE, COLUMN_ID + " = ?", new String[] {String.valueOf(id)});
    }

    public ArrayList<Note> read() {
        //sqLiteDatabase.delete(DB_TABLE, null, null);
        Cursor cursor = sqLiteDatabase.query(DB_TABLE, null, null, null, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return new ArrayList<>();
        }
        ArrayList<Note> notes = new ArrayList<>();
        do {
            Note note = new Note();
            note.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            note.setText(cursor.getString(cursor.getColumnIndex(COLUMN_TEXT)));
            note.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            notes.add(note);
        } while (cursor.moveToNext());
        cursor.close();
        return notes;
    }
}