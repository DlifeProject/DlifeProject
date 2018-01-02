package com.yancy.gallerypickdemo.tb_page4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yancy.gallerypickdemo.R;

public class GpsUpload extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4_gps_upload);
    }
    public void onBackClick(View view) {
        finish();
    }
}
