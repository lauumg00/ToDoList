package com.example.examen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

public class MyDB {
    private MyDatabaseHelper dbHelper;

    private static SQLiteDatabase database;

    public final static String EMP_TABLE = "todolist"; // name of table

    public final static String EMP_ID = "_id";
    public final static String EMP_NAME = "name";
    public final static String EMP_DATE = "date";
    public final static String EMP_COMPLETE = "complete";
    public final static String EMP_IMAGE = "image";

    private Date d;

    /**
     * @param context
     */
    public MyDB(Context context) {
        dbHelper = new MyDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        //dbHelper.onCreate(database);
    }


    public static long createRecords(byte[] image, String name, String date, String complete) {
        int id = nextId();
        ContentValues values = new ContentValues();
        values.put(EMP_ID, id);
        values.put(EMP_IMAGE, image);
        values.put(EMP_NAME, name);
        values.put(EMP_DATE, date);
        values.put(EMP_COMPLETE, complete);
        return database.insert(EMP_TABLE, null, values);
    }


    public static Cursor selectRecords(boolean completed) {
        String[] cols = new String[]{EMP_ID, EMP_IMAGE, EMP_NAME, EMP_DATE, EMP_COMPLETE};
        Cursor mCursor;
        if (completed){
            mCursor = database.query(true, EMP_TABLE,cols, null
                    , null, null, null, "date asc", null);
        } else{
            mCursor = database.query(true, EMP_TABLE, cols, "complete = ?"
            , new String[]{"false"}, null, null, "date asc", null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public void deleteItem(int id) {
        database.delete(EMP_TABLE, "_id = ?", new String[]{String.valueOf(id)});
    }

    public static long updateItem(int id, byte[] image, String name, String date, String complete) {
        ContentValues values = new ContentValues();
        values.put(EMP_IMAGE, image);
        values.put(EMP_NAME,name);
        values.put(EMP_DATE,date);
        values.put(EMP_COMPLETE, complete);
        return database.update(EMP_TABLE, values, "_id = ?", new String[]{String.valueOf(id)});
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    private static int nextId() {
        int id;
        try {
            String[] cols = new String[]{EMP_ID};
            Cursor mCursor = database.query(true, EMP_TABLE, cols, null
                    , null, null, null, "_id desc", null);
            mCursor.moveToFirst();
            id = mCursor.getInt(0) + 1;
            return id;
        }catch (CursorIndexOutOfBoundsException e){
            id=0;
        }
        return id;
    }
}
