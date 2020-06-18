package com.example.examen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyDB";

    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "todolist";

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "+TABLE_NAME+"( _id integer primary key, image BLOB, name text not null,date text not null,complete text not null);";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        database.execSQL(DATABASE_CREATE);

    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(MyDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(database);
    }
}
