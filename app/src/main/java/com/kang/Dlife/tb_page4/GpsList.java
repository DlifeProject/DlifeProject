package com.kang.Dlife.tb_page4;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.LocationDao;
import com.kang.Dlife.data_base.LocationTrace;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GpsList extends AppCompatActivity {

    private ScrollView gpsMsg;
    private LinearLayout linearLayout;
    private Button btGPS,insertGPS,queryNew;

    private final static int REQUEST_CODE_RESOLUTION = 1;
    private final static String TAG = "DiaryTab";
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private LocationTrace locationTrace = new LocationTrace();
    private LocationTrace queryLocationTrace = new LocationTrace();
    private LocationDao locationDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.page4_gps_list);

        gpsMsg = (ScrollView) findViewById(R.id.gpsMsg);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        btGPS = (Button) findViewById(R.id.btGPS);
        insertGPS = (Button) findViewById(R.id.insertGPS);
        queryNew = (Button) findViewById(R.id.queryNew);


        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(connectionCallbacks)
                    .addOnConnectionFailedListener(onConnectionFailedListener)
                    .build();
        }
        googleApiClient.connect();
        askPermissions();

        if (locationDao == null) {
            locationDao = new LocationDao(GpsList.this);
        }

        btGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = discribLocarionTrace(lastLocation);
                updateLocation(lastLocation);

                TextView textView = new TextView(getApplicationContext());
                textView.setText(msg);
                linearLayout.addView(textView);

                gpsMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        gpsMsg.fullScroll(View.FOCUS_DOWN);
                    }
                });

            }
        });

        insertGPS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(lastLocation.getLatitude() == 0.0){
                    updateLocation(lastLocation);
                }

                long rowId = locationDao.insert(locationTrace);
                String msg = "NO :" + rowId;

                TextView textView = new TextView(getApplicationContext());
                textView.setText(msg);
                linearLayout.addView(textView);

                gpsMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        gpsMsg.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });

        queryNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryLocationTrace = locationDao.queryLatest();
                String message = queryMessage(queryLocationTrace);

                TextView textView = new TextView(getApplicationContext());
                textView.setText(message);
                linearLayout.addView(textView);

                gpsMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        gpsMsg.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });

    }
    public void onBackClick(View view) {
        finish();
    }

    public void updateLastLocationInfo(String msg){

        TextView textView = new TextView(this);
        textView.setText(msg);
        linearLayout.addView(textView);

        gpsMsg.post(new Runnable() {
            @Override
            public void run() {
                gpsMsg.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            lastLocation = location;
            String msg = discribLocarionTrace(location);
            updateLocation(location);
            locationDao.insertNewLocation(location);

        }
    };

    private void updateLocation(Location lastLocation) {
        locationTrace.setLongitude(lastLocation.getLongitude());
        locationTrace.setLatitude(lastLocation.getLatitude());
        locationTrace.setAltitude(lastLocation.getAltitude());
        locationTrace.setAccuracy(lastLocation.getAccuracy());
        locationTrace.setSpeed(lastLocation.getSpeed());
    }

    private String discribLocarionTrace(Location lastLocation) {
        String message = "";
        message += "The Information of the Last Location \n";

        Date date = new Date(lastLocation.getTime());
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String time = dateFormat.format(date);
        //String time = Common.getNowDateString();
        message += "fix time: " + time + "\n";

        message += "latitude: " + lastLocation.getLatitude() + "\n";            //緯度
        message += "longitude: " + lastLocation.getLongitude() + "\n";          //經度
        message += "altitude (meters): " + lastLocation.getAltitude() + "\n";   //高度
        message += "accuracy (meters): " + lastLocation.getAccuracy() + "\n";   //準確性
        message += "bearing (horizontal direction- in degrees): "
                + lastLocation.getBearing() + "\n";
        message += "speed (meters/second): " + lastLocation.getSpeed() + "\n";  //速度

        return message;
    }
    private String queryMessage(LocationTrace queryLocationTrace) {
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

    private GoogleApiClient.ConnectionCallbacks connectionCallbacks =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    Log.i(TAG, "GoogleApiClient connected");

                    if (ActivityCompat.checkSelfPermission(GpsList.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                        lastLocation = LocationServices.FusedLocationApi
                                .getLastLocation(googleApiClient);
                        LocationRequest locationRequest = LocationRequest.create()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setSmallestDisplacement(1000);
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
                                GpsList.this,
                                result.getErrorCode(),
                                0
                        ).show();
                        return;
                    }
                    try {
                        result.startResolutionForResult(
                                GpsList.this,
                                REQUEST_CODE_RESOLUTION);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e(TAG, "Exception while starting resolution activity");
                    }
                }
            };

    private static final int REQ_PERMISSIONS = 0;

    // New Permission see Appendix A
    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(GpsList.this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(GpsList.this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    REQ_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSIONS:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        String text = "onRequestPermissionsResult";
                        Toast.makeText(GpsList.this, text, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                break;
        }
    }

}
