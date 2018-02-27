package com.kang.Dlife.tb_page3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kang.Dlife.Common;
import com.kang.Dlife.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weisunquan on 2018/2/1.
 */

public class ExchangeActivity extends AppCompatActivity {

    private ImageButton btBack;
    private int imageNo = 1;
    private ImageButton ibPre;
    private ImageButton ibNext;
    private Button btCenter;
    private Button exchange;
    private TextView tvYes;
    List<String> type = getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3_friend_exchange);
        initView();
        showView();


    }


    private void initView() {
        tvYes=findViewById(R.id.tvYes);
        tvYes.setVisibility(View.GONE);
        btBack = findViewById(R.id.imageButton);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ibPre = findViewById(R.id.ibPre);
        ibNext = findViewById(R.id.ibNext);
        btCenter = findViewById(R.id.btCenter);
        exchange = findViewById(R.id.exchange);
        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        btCenter.setBackgroundResource(R.drawable.shape_rectangle_gray);
                ibPre.setClickable(false);
                ibNext.setClickable(false);
            exchange.setVisibility(View.GONE);
            tvYes.setVisibility(View.VISIBLE);

            switch (btCenter.getText().toString()){
                case "Shopping":
                    Common.showToast(ExchangeActivity.this,"Shopping");
                    break;
                case "Hobby":
                    Common.showToast(ExchangeActivity.this,"Hobby");
                    break;
                case "Learning":
                    Common.showToast(ExchangeActivity.this,"Learning");
                    break;
                case "Travel":
                    Common.showToast(ExchangeActivity.this,"Travel");
                    break;
                case "Work":
                    Common.showToast(ExchangeActivity.this,"Work");
                    break;



            }


            }
        });

    }

    public List<String> getType() {
        type = new ArrayList<>();
        type.add("Shopping");
        type.add("Hobby");
        type.add("Learning");
        type.add("Travel");
        type.add("Work");
        return type;
    }

    public void clickNext(View view) {
        imageNo++;
        setPageText();
    }

    public void clickPre(View view) {
        imageNo--;
        setPageText();
    }

    private void setPageText() {
        if (imageNo > type.size()) {
            imageNo = 1;
        } else if (imageNo < 1) {
            imageNo = type.size();
        }
        showView();
    }

    private void showView() {

        btCenter.setText(type.get(imageNo - 1));
    }
}

