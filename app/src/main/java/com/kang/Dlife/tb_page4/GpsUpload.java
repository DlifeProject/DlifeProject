package com.kang.Dlife.tb_page4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kang.Dlife.R;
import com.kang.Dlife.data_base.DiaryDetail;
import com.kang.Dlife.sever.LocationDao;
import com.kang.Dlife.data_base.LocationTrace;
import com.kang.Dlife.sever.LocationToDiary;

import java.util.List;

public class GpsUpload extends AppCompatActivity {

    private ScrollView gpsMsg;
    private LinearLayout linearLayout;
    private Button btGPS,btDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4_gps_upload);
        gpsMsg = (ScrollView) findViewById(R.id.gpsMsg);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        btGPS = (Button) findViewById(R.id.btGPS);
        btDiary = (Button) findViewById(R.id.btDiary);
        btGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationDao locationDao = new LocationDao(getApplicationContext());
                List<LocationTrace> tList = locationDao.getNunUpdate();
                String allMsg = "";
                for(LocationTrace l:tList){
                    allMsg += locationDao.queryMessage(l);
                }
                updateLastLocationInfo(allMsg);
            }
        });
        btDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationDao locationDao = new LocationDao(getApplicationContext());
                List<LocationToDiary> ltDiary = locationDao.autoDiary(getApplicationContext());
                String msg = "";
                for(LocationToDiary d:ltDiary) {
                    //msg += d.describe();
                }

                updateLastLocationInfo(msg);
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
}
