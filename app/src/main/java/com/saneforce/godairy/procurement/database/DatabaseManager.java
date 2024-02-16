package com.saneforce.godairy.procurement.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.saneforce.godairy.Model_Class.ProcSubDivison;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    public static final String TABLE_NAME = "proc_sub_division";
    private final Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context applicationContext) {
        context = applicationContext;
    }

    public DatabaseManager open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void saveSubDivision(ArrayList<String> subDivisionNameArray){
        int channalNameSize = subDivisionNameArray.size();

        String INSERT = "insert into "
                + TABLE_NAME+ " (" + DatabaseHelper.USER_SUB_DIV_NAME_COL + ") values (?)";
        SQLiteStatement insert = database.compileStatement(INSERT);

        for (int i = 0; i < channalNameSize; i++) {
            insert.bindString(1, subDivisionNameArray.get(i));
            insert.executeInsert();
        }
    }

    public ArrayList<ProcSubDivison> loadSubDivision(){
        ArrayList<ProcSubDivison> arrayList = new ArrayList<>();

        String select_query= "SELECT *FROM " + TABLE_NAME;

        Cursor cursor = database.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProcSubDivison subDivison = new ProcSubDivison();
                subDivison.setSubdivision_sname(cursor.getString(1));
                arrayList.add(subDivison);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }


    public void deleteAllSubDivision() {
        database.delete(DatabaseHelper.TABLE_NAME, null, null);
    }
}
