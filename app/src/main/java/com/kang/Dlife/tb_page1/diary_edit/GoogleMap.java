package com.kang.Dlife.tb_page1.diary_edit;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.LocationToDiary;

public class GoogleMap extends AppCompatActivity
        implements OnMapReadyCallback {
    private com.google.android.gms.maps.GoogleMap map;
    private LatLng MarkPlace;
    public LocationToDiary bundleP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page1_google_map);
        Bundle bundle = getIntent().getExtras();
        bundleP = (LocationToDiary) bundle.getSerializable("place");
        initPoints();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fmMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(com.google.android.gms.maps.GoogleMap map) {
        this.map = map;
        setUpMap();
    }

    //宣告地點的經緯度(緯度, 經度)
    private void initPoints() {
        MarkPlace = new LatLng(bundleP.getLatitude(), bundleP.getLongitude());
    }

    private void setUpMap() {
        //如果使用者同意取得現在位置, 就會顯示顯示自己位置的按鈕, 反之相反
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
        //map.getUiSettings()取得google map的ui元件
        //setZoomControlsEnabled 地圖上的縮放按鈕
        map.getUiSettings().setZoomControlsEnabled(true);
        //把鏡頭移到標記的位置
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(MarkPlace) //聚焦的點
                .zoom(18)        //縮放程度
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newCameraPosition(cameraPosition);
        //以動畫方式移動地圖
        map.animateCamera(cameraUpdate);
        map.addMarker(new MarkerOptions()
                .position(MarkPlace));


    }

    public void onBackClick(View view) {
        finish();
    }

}

