package com.kang.Dlife;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kang.Dlife.tb_page4.FacebookMember;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FacebookLogin {

    Context context;
    private String userId;
    private String userName;
    private String email;
    private List<FacebookMember> friendIdList;

    public LoginManager loginManager;
    public CallbackManager callbackManager;

    public FacebookLogin(Context context) {
        this.context = context;
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<FacebookMember> getFriendIdList() {
        return friendIdList;
    }

    public void setFriendIdList(List<FacebookMember> friendIdList) {
        this.friendIdList = friendIdList;
    }

    public void loginFB() {
        // 設定FB login的顯示方式 ; 預設是：NATIVE_WITH_FALLBACK
        // 設定要跟用戶取得的權限，以下3個是基本可以取得，不需要經過FB的審核
        loginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_friends");

//        // 設定要讀取的權限
        loginManager.logInWithReadPermissions((Activity) context, permissions);
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        new GraphRequest.GraphJSONObjectCallback() {
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
                                            String user_friends_name = obj.get("name").getAsString();
                                            String user_friends_id = obj.get("id").getAsString();
                                            friendIdList.add(new FacebookMember(user_friends_name, user_friends_id));
                                        }

                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,friends");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

}
