package com.kang.Dlife.sever;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import com.kang.Dlife.Common;

import com.kang.Dlife.data_base.LocationTrace;

import java.util.ArrayList;
import java.util.List;

public class LocationDao extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "location_trace";

    private static final String COL_sk = "sk";
    private static final String COL_longitude = "longitude";
    private static final String COL_latitude = "latitude";
    private static final String COL_altitude = "altitude";
    private static final String COL_speed = "speed";
    private static final String COL_accuracy = "accuracy";
    private static final String COL_forward_sk = "forward_sk";
    private static final String COL_distance = "distance";
    private static final String COL_is_update = "is_update";
    private static final String COL_update_date = "update_date";
    private static final String COL_post_stamp = "post_stamp";
    private static final String COL_post_date = "post_date";

    private static final String[] args = {
            COL_sk,COL_longitude,COL_latitude,COL_altitude,COL_speed,
            COL_accuracy,COL_forward_sk,COL_distance,COL_is_update,COL_update_date,
            COL_post_stamp,COL_post_date};

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ( " +
                    COL_sk + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_longitude + " TEXT NOT NULL, " +
                    COL_latitude + " TEXT, " +
                    COL_altitude + " TEXT, " +
                    COL_speed + " TEXT, " +
                    COL_accuracy + " TEXT, " +
                    COL_forward_sk + " TEXT, " +
                    COL_distance + " TEXT, " +
                    COL_is_update + " TEXT, " +
                    COL_update_date + " TEXT, " +
                    COL_post_stamp + " TEXT, " +
                    COL_post_date + " TEXT ); ";

    private static final double MAX_DICT = 100.00;


    public LocationDao(Context context) {
        super(context, Common.DBNAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {db.execSQL(TABLE_CREATE);}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public long insert(LocationTrace locationTrace) {

        if(locationTrace != null){
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_longitude, locationTrace.getLongitude());
            values.put(COL_latitude, locationTrace.getLatitude());
            values.put(COL_altitude, locationTrace.getAltitude());
            values.put(COL_speed, locationTrace.getSpeed());
            values.put(COL_accuracy, locationTrace.getAccuracy());
            values.put(COL_forward_sk, locationTrace.getForward_sk());
            values.put(COL_distance, locationTrace.getDistance());
            values.put(COL_is_update, "0");
            values.put(COL_update_date, "0000-00-00 00:00:00");
            values.put(COL_post_stamp, Common.getTimestampString());
            values.put(COL_post_date, Common.getNowDateString());
            return db.insert(TABLE_NAME, null, values);
        }else{
            return 0;
        }

    }

    public long insertNewLocation(Location location){

        LocationTrace insertLocationTrace = new LocationTrace();
        if(location == null){
            return 0;
        }else {
            LocationTrace latestLocationTrace = queryCompareLocationTrace();

            insertLocationTrace.setLongitude(location.getLongitude());
            insertLocationTrace.setLatitude(location.getLatitude());
            insertLocationTrace.setAltitude(location.getAltitude());
            insertLocationTrace.setSpeed(location.getSpeed());
            insertLocationTrace.setAccuracy(location.getAccuracy());

            if(latestLocationTrace.getSk() == 0){

                insertLocationTrace.setForward_sk(0);
                insertLocationTrace.setDistance(0.00);

            }else{

                //計算與上次的距離
                float[] results = new float[1];
                Location.distanceBetween(latestLocationTrace.getLatitude(), latestLocationTrace.getLongitude(),
                        location.getLatitude(), location.getLongitude(),
                        results);

                if(results[0] > MAX_DICT){

                    insertLocationTrace.setForward_sk(0);
                    insertLocationTrace.setDistance(results[0]);

                }else{

                    insertLocationTrace.setForward_sk(latestLocationTrace.getSk());
                    insertLocationTrace.setDistance(results[0]);

                }
            }

            return insert(insertLocationTrace);

        }
    }

    public LocationTrace queryLatest() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * "
                + " from " + TABLE_NAME
                //+ " where sk < 0"
                + " order by " + COL_sk + " desc"
                + " limit 1";
        String[] args = {};     //使用*號query 欄位需帶入空集合
        Cursor cursor = db.rawQuery(sql,args);
        LocationTrace locationTrace = new LocationTrace();

        if(cursor.moveToNext()){
            locationTrace = setFromCursor(cursor);

        }else{

            locationTrace = getLocationTraceInit();

        }
        return locationTrace;
    }

    public LocationTrace queryCompareLocationTrace(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * "
                + " from " + TABLE_NAME
                + " where " + COL_forward_sk +" < 0"
                + " order by " + COL_sk + " desc"
                + " limit 1";
        String[] args = {};     //使用*號query 欄位需帶入空集合
        Cursor cursor = db.rawQuery(sql,args);
        LocationTrace locationTrace = new LocationTrace();

        if(cursor.moveToNext()){
            locationTrace = setFromCursor(cursor);
        }else{
            locationTrace = queryLatest();
        }
        return locationTrace;
    }

    private LocationTrace getLocationTraceInit(){
        LocationTrace locationTrace = new LocationTrace();

        locationTrace.setSk(0);
        locationTrace.setLongitude(0.0);
        locationTrace.setLatitude(0.0);
        locationTrace.setAltitude(0.0);
        locationTrace.setSpeed(0.0);
        locationTrace.setAccuracy(0.0);
        locationTrace.setForward_sk(0);
        locationTrace.setDistance(0);
        locationTrace.setIs_update(0);
        locationTrace.setUpdate_date("0000-00-00 00:00:00");
        locationTrace.setPost_stamp("0");
        locationTrace.setPost_date("0000-00-00 00:00:00");

        return locationTrace;
    }

    private LocationTrace setFromCursor(Cursor cursor){
        LocationTrace locationTrace = new LocationTrace();
        locationTrace.setSk(cursor.getInt(0));
        locationTrace.setLongitude(cursor.getDouble(1));
        locationTrace.setLatitude(cursor.getDouble(2));
        locationTrace.setAltitude(cursor.getDouble(3));
        locationTrace.setSpeed(cursor.getDouble(4));
        locationTrace.setAccuracy(cursor.getDouble(5));
        locationTrace.setForward_sk(cursor.getInt(6));
        locationTrace.setDistance(cursor.getDouble(7));
        locationTrace.setIs_update(cursor.getInt(8));
        locationTrace.setUpdate_date(cursor.getString(9));
        locationTrace.setPost_stamp(cursor.getString(10));
        locationTrace.setPost_date(cursor.getString(11));
        return locationTrace;
    }

    public List<LocationTrace> getNunUpdate() {
        List<LocationTrace> tList = new ArrayList<LocationTrace>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * "
                + " from " + TABLE_NAME
                + " where " + COL_is_update +" = 0"
                + " order by " + COL_sk;
        String[] args = {};     //使用*號query 欄位需帶入空集合
        Cursor cursor = db.rawQuery(sql,args);

        while(cursor.moveToNext()){
            tList.add(setFromCursor(cursor));
        }

        return tList;
    }
    public List<LocationToDiary>autoDiary(Context context) {

        List<LocationToDiary> ltDiary = new ArrayList<LocationToDiary>();

        int timesCount = 0;
        double sumLongitude = 0.0;
        double sumLatitude = 0.0;
        double sumAltitude = 0.0;
        LocationTrace ltLast = new LocationTrace();
        LocationTrace ltforward = new LocationTrace();

        LocationDao ltGetAll = new LocationDao(context);
        List<LocationTrace> ltLocationTrace = ltGetAll.getNunUpdate();

        for(LocationTrace l:ltLocationTrace) {

            if(l.getForward_sk() == 0) {

                if(timesCount > 1) {
                    ltDiary.add(autoDiary(ltLast, ltforward, timesCount, sumLongitude, sumLatitude,sumAltitude));
                }
                ltLast = new LocationTrace(l);
                ltforward = new LocationTrace(l);
                timesCount = 1;
                sumLongitude = l.getLongitude();
                sumLatitude = l.getLatitude();
                sumAltitude = l.getAltitude();
            }else if(l.getForward_sk() > 0) {
                ltLast = new LocationTrace(l);
                timesCount++;
                sumLongitude += l.getLongitude();
                sumLatitude += l.getLatitude();
                sumAltitude += l.getAltitude();
            }else {
                if(timesCount > 1) {
                    ltDiary.add(autoDiary(ltLast, ltforward, timesCount, sumLongitude, sumLatitude,sumAltitude));
                }
                timesCount = 0;
                ltLast = null;
                ltforward = null;
                sumLongitude = 0.0;
                sumLatitude = 0.0;
                sumAltitude = 0.0;
            }
        }

        return ltDiary;
    }

    private LocationToDiary autoDiary(LocationTrace ltLast, LocationTrace ltForward, int timesCount, double sumLongitude,
                                  double sumLatitude, double sumAltitude) {

        LocationToDiary newDiary = new LocationToDiary();
        newDiary.setStartStamp(ltForward.getPost_stamp());
        newDiary.setStartDate(ltForward.getPost_date());
        newDiary.setEndStamp(ltLast.getPost_stamp());
        newDiary.setEndDate(ltLast.getPost_date());
        newDiary.setPostDay(Common.getNowDayString());
        newDiary.setPostDate(Common.getNowDateString());
        newDiary.setLongitude(sumLongitude/timesCount);
        newDiary.setLatitude(sumLatitude/timesCount);
        newDiary.setAltitude(sumAltitude/timesCount);
        newDiary.setEndLocationSK(ltLast.getSk());

        return newDiary;
    }
    public String queryMessage(LocationTrace queryLocationTrace) {
        String message = "";
        message += "sk : " + String.valueOf(queryLocationTrace.getSk()) + "\n";
        message += "longitude : " + String.valueOf(queryLocationTrace.getLongitude()) + "\n";
        message += "latitude : " + String.valueOf(queryLocationTrace.getLatitude()) + "\n";
        message += "altitude : " + String.valueOf(queryLocationTrace.getAltitude()) + "\n";
        message += "speed : " + String.valueOf(queryLocationTrace.getSpeed()) + "\n";
        message += "accuracy : " + String.valueOf(queryLocationTrace.getAccuracy()) + "\n";
        message += "forward_sk : " + String.valueOf(queryLocationTrace.getForward_sk()) + "\n";
        message += "distance : " + String.valueOf(queryLocationTrace.getDistance()) + "\n";
        message += "post_stamp : " + queryLocationTrace.getPost_stamp() + "\n";
        message += "post_date : " + queryLocationTrace.getPost_date() + "\n";
        return message;
    }
    public int deleteById(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = COL_sk + " = ?;";
        String[] whereArgs = {String.valueOf(id)};
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }
}