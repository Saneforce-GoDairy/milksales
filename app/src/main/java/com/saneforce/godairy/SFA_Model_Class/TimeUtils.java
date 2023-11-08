package com.saneforce.godairy.SFA_Model_Class;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class TimeUtils {

    private static final String TAG = TimeUtils.class.getSimpleName();
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT1 = "yyyy-MM-dd";
    public static final String FORMAT2 = "dd/MM/yyyy";

    public static final String FORMAT3 = "dd/MM/yyyy HH:mm:ss";


    public static final String FORMAT5 = "dd-MMM-yyyy";
    public static final String FORMAT6 = "EEE";
    public static final String FORMAT4 = "MMM";
    public static final String FORMAT7 = "yyyy";
    public static final String FORMAT8 = "dd";
    public static final String FORMAT9 = "MM";
    public static final String FORMAT10 = " [ MMMM - yyyy ] ";
    public static final String FORMAT11 = "MM/dd/yyyy";
    public static final String FORMAT12 = "MM/dd/yy";
    public static final String FORMAT13 = "dd MMMM";
    public static final String FORMAT14 = "MMMM";

    public static long getTimeStamp(String date, String format){

        Date date2 = null;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
            date2 = sdf.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date2.getTime();
    }

    public static String getCurrentTimeStamp(String format){

        String stringDate ="";

        long timestampMilliseconds =System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        stringDate = simpleDateFormat.format(new Date(timestampMilliseconds));

        return stringDate;
    }


    public static String getCurrentTimeStamp(long timeStamp, String format){


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return simpleDateFormat.format(new Date(timeStamp));
    }
    public static String getCurrentTime(String format){
//       2021-01-20 11:33:05
        long timestampMilliseconds =System.currentTimeMillis();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String stringDate = simpleDateFormat.format(new Date(timestampMilliseconds));
        Log.d(TAG, "getCurrentTimeZone: => "+ stringDate);
        return stringDate;
    }

    public static String getFormattedDate(String currentFormat, String requiredFormat, String date){
//       2021-01-20 11:33:05

        SimpleDateFormat currentDateFormat = new SimpleDateFormat(currentFormat, Locale.ENGLISH);
        SimpleDateFormat requiredDateFormat = new SimpleDateFormat(requiredFormat, Locale.ENGLISH);
        currentDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String outputDate = "";
        try {
            Date convertedDate = currentDateFormat.parse(date);
            outputDate = requiredDateFormat.format(convertedDate);
            Log.d(TAG, "outputDate: => "+ outputDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    public static String formatdate(String fdate)
    {
        if(fdate == null || fdate.equals(""))
            return "";

        String datetime="";
        DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        SimpleDateFormat d= new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        }catch (ParseException e)
        {
            e.printStackTrace();
        }
        return  datetime;
    }


    public static int minDifference(Date date1, Date date2) {

        final int MILLI_TO_HOUR = 1000 * 60;
        return (int) (date2.getTime() - date1.getTime()) / MILLI_TO_HOUR;
    }


    public static int dayDifference(Date date1, Date date2) {

//        final int MILLI_TO_DAY = 1000 * 60 * 60 * 24;
//        (int) (date2.getTime() - date1.getTime()) / MILLI_TO_DAY
        return (int) TimeUnit.MILLISECONDS.toDays(date2.getTime() - date1.getTime());
    }

    public static Date getDate(String format, String dateString) {
        SimpleDateFormat formatter1=new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter1.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String getDayOfWeek(String date, String pattern){
        String finalDay= null;
        try {
            String input_date=date;
            SimpleDateFormat format1=new SimpleDateFormat(FORMAT2, Locale.ENGLISH);
            Date dt1=format1.parse(input_date);
            DateFormat format2=new SimpleDateFormat(pattern, Locale.ENGLISH);
            finalDay = format2.format(dt1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDay;
    }

    public static String getDayFromMonth(String date){
        String finalDay= null;
        try {
            String input_date=date;
            SimpleDateFormat format1=new SimpleDateFormat(FORMAT1, Locale.ENGLISH);
            Date dt1=format1.parse(input_date);
            DateFormat format2=new SimpleDateFormat(FORMAT4, Locale.ENGLISH);
            finalDay = format2.format(dt1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDay;
    }

    public static String getDayFromDate(String date){
        String finalDay= null;
        try {
            String input_date=date;
            SimpleDateFormat format1=new SimpleDateFormat(FORMAT1, Locale.ENGLISH);
            Date dt1=format1.parse(input_date);
            DateFormat format2=new SimpleDateFormat("d", Locale.ENGLISH);
            finalDay = format2.format(dt1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDay;
    }

    public static String getDayFromDate(Calendar date){
        String finalDay= null;
        DateFormat format2=new SimpleDateFormat("d", Locale.ENGLISH);
        finalDay = format2.format(date.getTime());
        return finalDay;
    }

    public static String getDateFromCalendar(Calendar date){
        String finalDay= null;
        DateFormat format2=new SimpleDateFormat(FORMAT1, Locale.ENGLISH);
        finalDay = format2.format(date.getTime());
        return finalDay;
    }


    public static int compareCurrentAndLoginDate(String dateTime){
        DateFormat dateFormat = new SimpleDateFormat(FORMAT1, Locale.ENGLISH);
        try {
            if(dateTime!=null && !dateTime.equals("")){
                Date date1 = dateFormat.parse(dateTime);
                Date date =  dateFormat.parse(TimeUtils.getCurrentTime(FORMAT1));
                return date1.compareTo(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 1;

    }




}
