package com.kang.Dlife.tb_page4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kang.Dlife.Common;
import com.kang.Dlife.R;

public class PersonData extends AppCompatActivity {

    private static final String TAG = "PersonData";

    private TextView tvAccount, tvNickname, tvBirthday, tvGender, tvLogout;
    private ImageButton ibChangePassword, ibNickname, ibBirthday, ibGender, ibLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4_person_data);
        findView();


    }

    private void findView() {

        tvAccount = (TextView)findViewById(R.id.tvAccount);
        tvNickname = (TextView)findViewById(R.id.tvNickname);
        tvBirthday = (TextView)findViewById(R.id.tvBirthday);
        tvGender = (TextView)findViewById(R.id.tvGender);
        tvLogout = (TextView)findViewById(R.id.tvLogout);

        ibChangePassword = (ImageButton)findViewById(R.id.ibChangePassword);
        ibNickname = (ImageButton)findViewById(R.id.ibNickname);
        ibBirthday = (ImageButton)findViewById(R.id.ibBirthday);
        ibGender = (ImageButton)findViewById(R.id.ibGender);
        ibLogout = (ImageButton)findViewById(R.id.ibLogout);

        tvAccount.setText(Common.getAccount(PersonData.this));
        tvAccount.setText(Common.getAccount(PersonData.this));


    }

    public void onBackClick(View view) {
        finish();
    }
}
