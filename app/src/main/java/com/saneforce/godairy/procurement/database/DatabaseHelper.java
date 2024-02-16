package com.saneforce.godairy.procurement.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "godairy_procurement";
    static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "proc_sub_division";
    public static final String ID_COL = "id";
    public static final String USER_SUB_DIV_NAME_COL = "subdivision_sname";

    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_SUB_DIV_NAME_COL + " TEXT)";

    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
