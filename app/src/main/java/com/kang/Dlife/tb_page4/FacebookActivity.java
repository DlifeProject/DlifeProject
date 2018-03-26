package com.kang.Dlife.tb_page4;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kang.Dlife.Common;
import com.kang.Dlife.R;
import com.kang.Dlife.sever.MyTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacebookActivity extends AppCompatActivity {
    private static final String TAG = FacebookActivity.class.getSimpleName();
    private ImageView mImgPhoto;
    private TextView mTextDescription;
    private String userId;
    private String userName;
    private TextView tvTitle;
    private ImageButton loginButton;
    private ImageButton logoutButton;
    private String email;
    private String user_friends_id;
    private String user_friends_name;
    private List<FacebookMember> friendIdList;
    private ListView lvMember;
    private LinearLayout linearLayoutFriend;
    // FB
    private LoginManager loginManager;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4_facebook);

        // init LoginManager & CallbackManager
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        findViewByID();

        // method_1.判斷用戶是否登入過
        if (Profile.getCurrentProfile() != null) {
            Profile profile = Profile.getCurrentProfile();
            // 取得用戶大頭照
            Uri userPhoto = profile.getProfilePictureUri(300, 300);
            String id = profile.getId();
            String name = profile.getName();

            Glide.with(FacebookActivity.this)
                    .load(userPhoto.toString())
                    .crossFade()
                    .into(mImgPhoto);

            mTextDescription.setText(name);
            loginButton.setVisibility(View.GONE);


            AccessToken token = AccessToken.getCurrentAccessToken();
            GraphRequest graphRequest = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                if (response.getConnection().getResponseCode() == 200) {

                                    userId = object.getString("id");
                                    userName = object.getString("name");
                                    email = object.getString("email");
                                    String friendsJString = object.getString("friends");
                                    Gson gson = new Gson();
                                    JsonObject friendsJsonObject = gson.fromJson(object.getString("friends"), JsonObject.class);
                                    JsonArray friendsArray = friendsJsonObject.get("data").getAsJsonArray();
                                    friendIdList = new ArrayList<>();
                                    for (int i = 0; i < friendsArray.size(); i++) {
                                        JsonObject obj = friendsArray.get(i).getAsJsonObject();
                                        user_friends_name = obj.get("name").getAsString();
                                        user_friends_id = obj.get("id").getAsString();
                                        friendIdList.add(new FacebookMember(user_friends_name, user_friends_id));

                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                Thread.sleep(3000);//括号里面的5000代表5000毫秒，也就是5秒，可以该成你需要的时间
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (user_friends_name != null) {
                                updateFacebookFriend(FacebookActivity.this, friendIdList, userId);
                                lvMember.setAdapter(new MemberAdapter(FacebookActivity.this, friendIdList));
                            }

                        }

                        private void updateFacebookFriend(Context context, List<FacebookMember> friendIdList, String userId) {

                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("action", "updateFBList");
                            jsonObject.addProperty("account", Common.getAccount(context));
                            jsonObject.addProperty("password", Common.getPWD(context));
                            jsonObject.addProperty("FBid", userId);
                            jsonObject.addProperty("FBList", new Gson().toJson(friendIdList));
                            String url = Common.URL + Common.FRIEND;

                            MyTask login = new MyTask(url, jsonObject.toString());
                            String inStr = null;
                            try {
                                inStr = login.execute().get().trim();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
            // 如果要取得email，需透過添加參數的方式來獲取(如下)
            // 不添加只能取得id & name
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,friends");
            graphRequest.setParameters(parameters);
            graphRequest.executeAsync();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void findViewByID() {
        mImgPhoto = (ImageView) findViewById(R.id.mImgPhoto);
        mTextDescription = (TextView) findViewById(R.id.mTextDescription);
        lvMember = (ListView) findViewById(R.id.lvMember);
        tvTitle = findViewById(R.id.tvTitle);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login
                loginFB();


            }
        });

        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Profile.getCurrentProfile() != null) {
                    mTextDescription.setText("您已經成功登出");
                    lvMember.setVisibility(View.GONE);
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
            public void onSuccess(LoginResult loginResult) {
                // 登入成功
        /* 可以取得相關資訊
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


                                        userId = object.getString("id");
                                        userName = object.getString("name");
                                        email = object.getString("email");
                                        String friendsJString = object.getString("friends");
                                        Gson gson = new Gson();
                                        JsonObject friendsJsonObject = gson.fromJson(object.getString("friends"), JsonObject.class);
                                        JsonArray friendsArray = friendsJsonObject.get("data").getAsJsonArray();
                                        friendIdList = new ArrayList<>();
//                                    friendsArray.get(0).getAsJsonObject().get("name").getAsString();
                                        for (int i = 0; i < friendsArray.size(); i++) {
                                            JsonObject obj = friendsArray.get(i).getAsJsonObject();
                                            user_friends_name = obj.get("name").getAsString();
                                            user_friends_id = obj.get("id").getAsString();
                                            friendIdList.add(new FacebookMember(user_friends_name, user_friends_id));

                                        }


                                        Log.d(TAG, "Facebook id:" + userId);
                                        Log.d(TAG, "Facebook name:" + userName);
                                        Log.d(TAG, "Facebook email:" + email);


                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                try {
                                    Thread.sleep(500);//括号里面的5000代表5000毫秒，也就是5秒，可以该成你需要的时间
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent();
                                intent.setClass(FacebookActivity.this, FacebookActivity.class);
                                startActivity(intent);
                                finish();


                            }
                        }
                );
                // 如果要取得email，需透過添加參數的方式來獲取(如下)
                // 不添加只能取得id & name
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,friends");
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


    private class MemberAdapter extends BaseAdapter {
        Context context;
        List<FacebookMember> friendIdList;

        MemberAdapter(Context context, List<FacebookMember> friendIdList) {
            this.context = context;
            this.friendIdList = friendIdList;
        }

        @Override
        public int getCount() {
            return friendIdList.size();
        }

        @Override
        public View getView(int position, View itemView, ViewGroup parent) {
            if (itemView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                itemView = layoutInflater.inflate(R.layout.page4_facebook_item_view, parent, false);
            }

            FacebookMember facebookMember = friendIdList.get(position);
            ImageView ivImage = (ImageView) itemView
                    .findViewById(R.id.ivImage);
            ivImage.setImageResource(R.drawable.ic_face);

            TextView tvId = (TextView) itemView
                    .findViewById(R.id.tvId);
            tvId.setText(facebookMember.getId());

            TextView tvName = (TextView) itemView
                    .findViewById(R.id.tvName);
            tvName.setText(facebookMember.getName());
            return itemView;
        }

        @Override
        public Object getItem(int position) {
            return friendIdList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return friendIdList.size();
        }
    }
}



