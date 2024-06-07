package com.saneforce.godairy.procurement.custom_form.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBController extends SQLiteOpenHelper {
    private static final String TAG = DBController.class.getSimpleName();
    Context context;
    private static final String databasename = "dbSynMaster"; // Dtabasename
    public static final String TABLE_COMMON = "tblSynMaster"; // tablename
    private static final String product = "tableName"; // column name
    private static final int VERSION_CODE = 17;
    private static final String category = "tableValue"; // column name

    public DBController(Context context) {
        super(context, databasename, null, VERSION_CODE);
        this.context = context;
    }


    public DBController(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old,
                          int current_version) {
        onCreate(database);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {


    }

    @SuppressLint("Range")
    public String getResponse(String keyName){

        String value = "";
        String[] args = new String[]{keyName};

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_COMMON + " WHERE " + product + " = ?", args);
        try {
            cursor.moveToFirst();

        //    printUsrLog(TAG, "getResponse: keyName : " + keyName);

         //   printUsrLog(TAG, "getResponse: " + cursor.getCount());

            if(cursor.getCount()> 0 && cursor.getColumnCount()> 0)
                return cursor.getString(cursor.getColumnIndex(category));
            //close cursor & database

        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
//            database.close();

        }
        return value;
    }


}
