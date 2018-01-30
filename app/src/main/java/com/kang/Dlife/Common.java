package com.kang.Dlife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;


public class Common {


    //會員帳號
    public final static String DEFAULT_PREFFERENCES_FILE_NAME = "preferences";
    public final static String PREFFERENCES_USER_ACCOUNT = "userAccount";
    public final static String PREFFERENCES_USER_PASSWORD = "userPassword";
    public final static String PREFFERENCES_UUID = "userUUID";
    public final static String PREFFERENCES_NICKNAME = "nickname";
    public final static String PREFFERENCES_USER_LAST_LOGIN_DATE = "loginDate";
    public final static String PREFFERENCES_BIRTHDAY = "birthday";

    public final static String TAG = "Common";

//    public static final String URL = "http://10.0.2.2:8080/Dlife/";
    public static final String URL = "http://192.168.1.13:8080/Dlife/";

    public final static String WEBLOGIN = "login";
    public final static String WEBDIARY = "diary";
    public final static String WEBPHOTO = "photo";
    public static final String WEBGPS = "gps";
    public static final String DBNAME = "dlife";
    public final static String WEBSUMMARY = "summary";

    public static String getAccount(Context c){
        String account = getPrefferencesData(c, PREFFERENCES_USER_ACCOUNT);
        return account;
    }

    public static String getPWD(Context c){
        String pwd = getPrefferencesData(c, PREFFERENCES_USER_PASSWORD);
        return pwd;
    }

    public static boolean checkNetConnected(Context c){

        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;

    }

    public static String getPrefferencesData(Context c,String key){

        SharedPreferences preferences =
                c.getSharedPreferences(DEFAULT_PREFFERENCES_FILE_NAME, MODE_PRIVATE);

        return  preferences.getString(key,"");
    }
    public static void setPrefferencesString(Context c,String key, String value){

        SharedPreferences preferences =
                c.getSharedPreferences(Common.DEFAULT_PREFFERENCES_FILE_NAME, MODE_PRIVATE);
        preferences.edit().putString(key, value).apply();

    }

    public static void updateLoginPreferences(Context c,String userAccount,String userPassword, String uuid){

        SharedPreferences preferences =
                c.getSharedPreferences(Common.DEFAULT_PREFFERENCES_FILE_NAME, MODE_PRIVATE);

        preferences.edit()
                .putString(Common.PREFFERENCES_USER_ACCOUNT, userAccount)
                .putString(Common.PREFFERENCES_USER_PASSWORD, userPassword)
                .putString(Common.PREFFERENCES_UUID, uuid)
                .putString(Common.PREFFERENCES_USER_LAST_LOGIN_DATE, Common.getNowDateString())
                .apply();

    }

    public static String getUUID(Context c) {

        String uuid = getPrefferencesData( c, PREFFERENCES_UUID);
        if(uuid.length() == 0){
            uuid = UUID.randomUUID().toString();
            setPrefferencesString(c, PREFFERENCES_UUID, uuid);
            return uuid;
        }else{
            return uuid;
        }

    }
    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }


    public static void showToast(Context c, String message) {
        Toast mToast = Toast.makeText(c, message, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static String getNowDateString(){
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date current = new Date();
        return sdFormat.format(current);
    }

    public static String getNowDayString(){
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date current = new Date();
        return sdFormat.format(current);
    }

    public static String getTimestampString() {
        String datetime = String.valueOf(System.currentTimeMillis());
        return datetime;
    }

    public static String dateStringToDay(String date){

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date today = dateFormat.parse(date);
            DateFormat chtDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = chtDateFormat.format(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(date);
        return date;

    }

    public static String dateStringToHM(String date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date today = dateFormat.parse(date);
            DateFormat chtDateFormat = new SimpleDateFormat("HH:mm");
            date = chtDateFormat.format(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(date);
        return date;

    }

    public static boolean checkLoginAccountEmail(String account){
        if(account.contains("@")){
            return true;
        }else{
            return false;
        }
    }

    public static void startTabActivity(Context c){
        Intent intent = new Intent();
        intent.setClass(c  , TabActivity.class);
        c.startActivity(intent);
    }

    public static byte[] bitmapToPNG(Bitmap srcBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 轉成PNG不會失真，所以quality參數值會被忽略
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap downSize(Bitmap srcBitmap, int newSize) {
        // 如果欲縮小的尺寸過小，就直接定為128
        if (newSize <= 50) {
            newSize = 128;
        }
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        String text = "source image size = " + srcWidth + "x" + srcHeight;
        Log.d(TAG, text);
        int longer = Math.max(srcWidth, srcHeight);

        if (longer > newSize) {
            double scale = longer / (double) newSize;
            int dstWidth = (int) (srcWidth / scale);
            int dstHeight = (int) (srcHeight / scale);
            srcBitmap = Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, false);
            System.gc();
            text = "\nscale = " + scale + "\nscaled image size = " +
                    srcBitmap.getWidth() + "x" + srcBitmap.getHeight();
            Log.d(TAG, text);
        }
        return srcBitmap;
    }
}