package com.kang.Dlife.sever;


import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.kang.Dlife.data_base.LocationDao;

public class GPSService extends Service {

    public final static int DB_DELAY_TIME = 6000;              //毫秒
    public final static int GPS_INTERVAL = 3000;               //毫秒
    public final static int GPS_SET_SMALLEST_DISPLACEMENT = 10;  //公尺

    private static GPSThread gpsThread;
    private Boolean isRun = true;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        gpsThread = new GPSThread(this);
        gpsThread.start();

        return flags;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}

    public class GPSThread extends Thread {

        private Context c;
        private final static int REQUEST_CODE_RESOLUTION = 1;
        private final static String TAG = "DiaryTab";
        private GoogleApiClient googleApiClient;
        private Location lastLocation;
        private LocationDao locationDao;


        public GPSThread(Context c) {
            this.c = c;
            locationDao = new LocationDao(c);
        }

        @Override
        public void run() {
            //取得googleApiClient 並註冊ConnectionCallbacks 與 ConnectionFailedListener
            //無論連結API功能是否成功都會糊假時做好的對應方法

            if (googleApiClient == null) {
                googleApiClient = new GoogleApiClient.Builder(c)
                        .addApi(LocationServices.API)                               //指定使用何種API功能
                        .addConnectionCallbacks(connectionCallbacks)                //註冊connectionCallbacks監聽連結事件
                        .addOnConnectionFailedListener(onConnectionFailedListener)  //註冊connectionFailed 連結事件
                        .build();
            }
            googleApiClient.connect();      //呼叫connect()連結google開始API功能


            while (isRun) {

                try {
                    //先每分鐘抓一次
                    Thread.sleep(DB_DELAY_TIME);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }

        private LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastLocation = location;
                locationDao.insertNewLocation(location);
            }
        };

        private GoogleApiClient.ConnectionCallbacks connectionCallbacks =
                new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.i(TAG, "GoogleApiClient connected");

                        if (ActivityCompat.checkSelfPermission(c,
                                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED) {
                            lastLocation = LocationServices.FusedLocationApi
                                    .getLastLocation(googleApiClient);
                            LocationRequest locationRequest = LocationRequest.create()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(GPS_INTERVAL)
                                    .setSmallestDisplacement(GPS_SET_SMALLEST_DISPLACEMENT);
                            LocationServices.FusedLocationApi.requestLocationUpdates(
                                    googleApiClient, locationRequest, locationListener);
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                };

        private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener =
                new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult result) {
                        if (!result.hasResolution()) {
                            GoogleApiAvailability.getInstance().getErrorDialog(
                                    (Activity) c,
                                    result.getErrorCode(),
                                    0
                            ).show();
                            return;
                        }
                        try {
                            result.startResolutionForResult(
                                    (Activity) c,
                                    REQUEST_CODE_RESOLUTION);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Exception while starting resolution activity");
                        }
                    }
                };

    }
}
