package com.saneforce.godairy.procurement.database;

import static com.saneforce.godairy.procurement.database.DatabaseHelper.TABLE_NAME;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;

public class DatabaseManager {
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context c){
        context = c;
    }

    public DatabaseManager open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void saveProcAgronomist(String mSFCode,
                                   String mUserName,
                                   String mCompany,
                                   String mPlant,
                                   String mCenterName,
                                   String mFarmerCode,
                                   String mTypeOfProduct,
                                   String mTeatTip,
                                   String mTypeOfService,
                                   String mFarmersMeetingImage,
                                   String mCSRActivityImage,
                                   String mFodderAcres,
                                   String mFodderDevAcresImage,
                                   String mNoOfFarmersEnrolled,
                                   String mNoOfFarmerInducted,
                                   String mTimeDate){

        String INSERT = "insert into "
                + TABLE_NAME+ " (" + DatabaseHelper.USER_ID + "," +
                DatabaseHelper.USER_NAME +  "," +
                DatabaseHelper.COMPANY_NAME_COL +  "," +
                DatabaseHelper.PLANT_COL +  "," +
                DatabaseHelper.CENTER_NAME_COL +  "," +
                DatabaseHelper.FARMER_CODE_COL +  "," +
                DatabaseHelper.TYPE_OF_PRODUCT_COL +  "," +
                DatabaseHelper.TEAT_CUP_COL +  "," +
                DatabaseHelper.TYPE_OF_SERVICE_COL +  "," +
                DatabaseHelper.FARMERS_MEETING_IMAGE +  "," +
                DatabaseHelper.CSR_ACTIVITY_IMAGE +  "," +
                DatabaseHelper.FODDER_DEV_ACRES_COL +  "," +
                DatabaseHelper.FODDER_DEV_ACRES_IMAGE +  "," +
                DatabaseHelper.NO_OF_FARMERS_ENROLLED_COL +  "," +
                DatabaseHelper.NO_OF_FARMERS_INDUCTED_COL +  "," +
                DatabaseHelper.TIME_DATE + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        SQLiteStatement insert = database.compileStatement(INSERT);

            insert.bindString(1, mSFCode);
            insert.bindString(2, mUserName);
            insert.bindString(3, mCompany);
            insert.bindString(4, mPlant);
            insert.bindString(5, mCenterName);
            insert.bindString(6, mFarmerCode);
            insert.bindString(7, mTypeOfProduct);
            insert.bindString(8, mTeatTip);
            insert.bindString(9, mTypeOfService);
            insert.bindString(10, mFarmersMeetingImage);
            insert.bindString(11, mCSRActivityImage);
            insert.bindString(12, mFodderAcres);
            insert.bindString(13, mFodderDevAcresImage);
            insert.bindString(14, mNoOfFarmersEnrolled);
            insert.bindString(15, mNoOfFarmerInducted);
            insert.bindString(16, mTimeDate);
            insert.executeInsert();
    }
}
