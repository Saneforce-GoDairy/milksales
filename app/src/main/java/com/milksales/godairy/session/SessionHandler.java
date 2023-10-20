package com.milksales.godairy.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionHandler {
    private static final String PREF_NAME = "saneforce";
    private static final String KEY_MY_DAY_PLAN = "my_day_plan";
    private static final String KEY_CHECK_IN = "check_in";
    private static final String KEY_CHECK_IN_TIME_STAMP = "check_in_time_stamp";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    public void setMyDayPlanEnabled(String enabled){
        mEditor.putString(KEY_MY_DAY_PLAN, enabled);
        mEditor.commit();
    }

    public void setMyCheckIn(String enabled, String timeStamp){
        mEditor.putString(KEY_CHECK_IN, enabled);
        mEditor.putString(KEY_CHECK_IN_TIME_STAMP, timeStamp);
        mEditor.commit();
    }

    public User getUserDetails(){
        User user = new User();
        user.setMyDayPlan(mPreferences.getString(KEY_MY_DAY_PLAN, KEY_EMPTY));
        user.setCheckInEnabled(mPreferences.getString(KEY_CHECK_IN, KEY_EMPTY));
        user.setCheckInTimeStamp(mPreferences.getString(KEY_CHECK_IN_TIME_STAMP, KEY_EMPTY));
        return user;
    }
}
