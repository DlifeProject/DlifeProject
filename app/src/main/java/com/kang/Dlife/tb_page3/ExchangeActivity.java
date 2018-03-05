package com.kang.Dlife.tb_page3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kang.Dlife.R;



public class ExchangeActivity extends AppCompatActivity {

    private ImageButton ibBack;
    private TextView tvHeader, tvFriendShareCategory, tvMyShareCategory, tvSend;
    private ImageView ivFriendPhoto, ivMyPhoto, iv_previous, iv_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3_friend_exchange);
        findView(this);

    }

    private void findView(ExchangeActivity exchangeActivity) {

        ibBack = (ImageButton) findViewById(R.id.ibBack);
        tvHeader = (TextView) findViewById(R.id.tvHeader);
        tvFriendShareCategory = (TextView) findViewById(R.id.tvFriendShareCategory);
        tvMyShareCategory = (TextView) findViewById(R.id.tvMyShareCategory);
        tvSend = (TextView) findViewById(R.id.tvSend);
        ivFriendPhoto = (ImageView) findViewById(R.id.ivFriendPhoto);
        ivMyPhoto = (ImageView) findViewById(R.id.ivMyPhoto);
        iv_previous = (ImageView) findViewById(R.id.iv_previous);
        iv_next = (ImageView) findViewById(R.id.iv_next);
    }


}

