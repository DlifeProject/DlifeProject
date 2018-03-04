package com.kang.Dlife.tb_page3;

import java.io.Serializable;

/**
 * Created by regan on 2018/1/30.
 */

public class MatchFriendItem implements Serializable {

    private String myCategory;
    private int myCategorySK;
    private String myFriendCategory;
    private int myFriendCategorySK;
    private String myFriendName;
    private int myFriendSK;
    private int isShareable = 1;
    private String postDay = "0000-00-00";

    public MatchFriendItem(String myCategory, String myFriendCategory, String myFriendName) {
        super();
        this.myCategory = myCategory;
        this.myFriendCategory = myFriendCategory;
        this.myFriendName = myFriendName;
    }

    public MatchFriendItem(String myCategory, int myCategorySK, String myFriendCategory, int myFriendCategorySK,
                           String myFriendName, int myFriendSK, int isShareable, String postDay) {
        super();
        this.myCategory = myCategory;
        this.myCategorySK = myCategorySK;
        this.myFriendCategory = myFriendCategory;
        this.myFriendCategorySK = myFriendCategorySK;
        this.myFriendName = myFriendName;
        this.myFriendSK = myFriendSK;
        this.isShareable = isShareable;
        this.postDay = postDay;
    }

    public String getMyCategory() {
        return myCategory;
    }
    public void setMyCategory(String myCategory) {
        this.myCategory = myCategory;
    }
    public int getMyCategorySK() {
        return myCategorySK;
    }
    public void setMyCategorySK(int myCategorySK) {
        this.myCategorySK = myCategorySK;
    }
    public String getMyFriendCategory() {
        return myFriendCategory;
    }
    public void setMyFriendCategory(String myFriendCategory) {
        this.myFriendCategory = myFriendCategory;
    }
    public int getMyFriendCategorySK() {
        return myFriendCategorySK;
    }
    public void setMyFriendCategorySK(int myFriendCategorySK) {
        this.myFriendCategorySK = myFriendCategorySK;
    }
    public String getMyFriendName() {
        return myFriendName;
    }
    public void setMyFriendName(String myFriendName) {
        this.myFriendName = myFriendName;
    }
    public int getMyFriendSK() {
        return myFriendSK;
    }
    public void setMyFriendSK(int myFriendSK) {
        this.myFriendSK = myFriendSK;
    }
    public int getIsShareable() {
        return isShareable;
    }
    public void setIsShareable(int isShareable) {
        this.isShareable = isShareable;
    }
    public String getPostDay() {
        return postDay;
    }
    public void setPostDay(String postDay) {
        this.postDay = postDay;
    }

}
