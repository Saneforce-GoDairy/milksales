package com.saneforce.godairy.procurement.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "godairy_db";
    static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "proc_agronomist";
    public static final String ID_COL = "id";
    public static final String COMPANY_NAME_COL = "company";
    public static final String PLANT_COL = "plant";
    public static final String CENTER_NAME_COL = "center";
    public static final String FARMER_CODE_COL = "farmer_code";
    public static final String TYPE_OF_PRODUCT_COL = "type_of_product";
    public static final String TEAT_CUP_COL = "teat_dip_cup";
    public static final String TYPE_OF_SERVICE_COL = "type_of_service";
    public static final String FARMERS_MEETING_IMAGE = "farmers_meeting_image";
    public static final String CSR_ACTIVITY_IMAGE = "csr_activity_image";
    public static final String FODDER_DEV_ACRES_COL = "fodder_dev_acres";
    public static final String FODDER_DEV_ACRES_IMAGE = "fodder_dev_acres_image";
    public static final String NO_OF_FARMERS_ENROLLED_COL = "no_of_farmers_enrolled";
    public static final String NO_OF_FARMERS_INDUCTED_COL = "no_of_farmers_inducted";
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String TIME_DATE = "time_date";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_ID + " TEXT,"
            + USER_NAME + " TEXT,"
            + COMPANY_NAME_COL + " TEXT,"
            + PLANT_COL + " TEXT,"
            + CENTER_NAME_COL + " TEXT,"
            + FARMER_CODE_COL + " TEXT,"
            + TYPE_OF_PRODUCT_COL + " TEXT,"
            + TEAT_CUP_COL + " TEXT,"
            + TYPE_OF_SERVICE_COL + " TEXT,"
            + FARMERS_MEETING_IMAGE + " TEXT,"
            + CSR_ACTIVITY_IMAGE + " TEXT,"
            + FODDER_DEV_ACRES_COL + " TEXT,"
            + FODDER_DEV_ACRES_IMAGE + " TEXT,"
            + NO_OF_FARMERS_ENROLLED_COL + " TEXT,"
            + NO_OF_FARMERS_INDUCTED_COL + " TEXT,"
            + TIME_DATE + " TEXT)";

    public DatabaseHelper(Context context) {
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
