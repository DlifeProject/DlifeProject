package com.kang.Dlife.tb_page4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kang.Dlife.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacebookActivity extends AppCompatActivity {
    private static final String TAG = FacebookActivity.class.getSimpleName();
    private ImageView mImgPhoto;
    private TextView mTextDescription;
    private long userID;
    private String userName;
    private  Button loginButton;
    private String email;
    // FB
    private LoginManager loginManager;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4_facebook);

        // init facebook
//        FacebookSdk.sdkInitialize(this.getApplicationContext());
//        accessToken = AccessToken.getCurrentAccessToken();


        // init LoginManager & CallbackManager
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        findViewByID();

        // method_1.判斷用戶是否登入過
        if ( Profile.getCurrentProfile() != null) {
            Profile profile = Profile.getCurrentProfile();
            // 取得用戶大頭照
            Uri userPhoto = profile.getProfilePictureUri(300, 300);
            String id = profile.getId();
            String name = profile.getName();
            Log.d(TAG, "Facebook userPhoto: " + userPhoto);
            Log.d(TAG, "Facebook id: " + id);
            Log.d(TAG, "Facebook name: " + name);
            Log.d(TAG, "Facebook email:" + email);
            Glide.with(FacebookActivity.this)
                    .load(userPhoto.toString())
                    .crossFade()
                    .into(mImgPhoto);

            mTextDescription.setText("Name:"+name);
            loginButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void findViewByID() {
        mImgPhoto = (ImageView) findViewById(R.id.mImgPhoto);
        mTextDescription = (TextView) findViewById(R.id.mTextDescription);


        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login
                loginFB();



            }
        });

        Button logoutButton=findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if ( Profile.getCurrentProfile() != null) {
                    mTextDescription.setText("您已經成功登出");
                }
                //logout
                loginManager.logOut();
                Glide.with(FacebookActivity.this)
                        .load(R.drawable.ic_face)
                        .crossFade()
                        .into(mImgPhoto);
                loginButton.setVisibility(View.VISIBLE);

                }
        });

    }




    private void loginFB() {
        // 設定FB login的顯示方式 ; 預設是：NATIVE_WITH_FALLBACK
        /**
         * 1. NATIVE_WITH_FALLBACK
         * 2. NATIVE_ONLY
         * 3. KATANA_ONLY
         * 4. WEB_ONLY
         * 5. WEB_VIEW_ONLY
         * 6. DEVICE_AUTH
         */
        // 設定要跟用戶取得的權限，以下3個是基本可以取得，不需要經過FB的審核
        loginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_friends");
//        // 設定要讀取的權限
        loginManager.logInWithReadPermissions(this, permissions);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess( LoginResult loginResult) {
                // 登入成功
        /* 可以取得相關資訊，這裡就請各位自行打印出來
        Log.d(TAG, "Facebook getApplicationId: " + loginResult.getAccessToken().getApplicationId());
        Log.d(TAG, "Facebook getUserId: " + loginResult.getAccessToken().getUserId());
        Log.d(TAG, "Facebook getExpires: " + loginResult.getAccessToken().getExpires());
        Log.d(TAG, "Facebook getLastRefresh: " + loginResult.getAccessToken().getLastRefresh());
        Log.d(TAG, "Facebook getToken: " + loginResult.getAccessToken().getToken());
        Log.d(TAG, "Facebook getSource: " + loginResult.getAccessToken().getSource());
        Log.d(TAG, "Facebook getRecentlyGrantedPermissions: " + loginResult.getRecentlyGrantedPermissions());
        Log.d(TAG, "Facebook getRecentlyDeniedPermissions: " + loginResult.getRecentlyDeniedPermissions());*/
                // 透過GraphRequest來取得用戶的Facebook資訊
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            if (response.getConnection().getResponseCode() == 200) {
                                long userID = object.getLong("id");
                                userName = object.getString("name");
                                 email = object.getString("email");
                                Log.d(TAG, "Facebook id:" + userID);
                                Log.d(TAG, "Facebook name:" + userName);
                                Log.d(TAG, "Facebook email:" + email);


                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        try {
                            Thread.sleep(3500);//括号里面的5000代表5000毫秒，也就是5秒，可以该成你需要的时间
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent();
                        intent.setClass(FacebookActivity.this, FacebookActivity.class);
                        startActivity(intent);
                        finish();
//                        if ( Profile.getCurrentProfile() != null) {
//
//                            // 此時如果登入成功，就可以順便取得用戶大頭照
//                            Profile profile = Profile.getCurrentProfile();
//                            // 設定大頭照大小
//                            Uri userPhoto = profile.getProfilePictureUri(300, 300);
//                            String id = profile.getId();
//                            String name = profile.getName();
//                            Glide.with(FacebookActivity.this)
//                                    .load(userPhoto.toString())
//                                    .crossFade()
//                                    .into(mImgPhoto);
//                            mTextDescription.setText("Name:" + name);
//                            loginButton.setVisibility(View.GONE);
//
//                        }
                    }
                }
                );
                // https://developers.facebook.com/docs/android/graph?locale=zh_TW
                // 如果要取得email，需透過添加參數的方式來獲取(如下)
                // 不添加只能取得id & name
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

                mTextDescription.setText("登入中...");
                loginButton.setVisibility(View.GONE);



            }

            @Override
            public void onCancel() {
                // 用戶取消
                Log.d(TAG, "Facebook onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                // 登入失敗
                Log.d(TAG, "Facebook onError:" + error.toString());
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackClick(View view) {
        finish();
    }


}
