package com.kang.Dlife.tb_page3;

/**
 * Created by regan on 2018/1/30.
 */

class MatchFriendItem {

    private String myCategory;
    private String myFriendCategory;
    private String myFriendName;

    public MatchFriendItem() {
        this.myCategory = "-";
        this.myFriendCategory = "-";
        this.myFriendName = "Unknown";
    }

    public MatchFriendItem(String myCategory, String myFriendCategory, String myFriendName) {
        this.myCategory = myCategory;
        this.myFriendCategory = myFriendCategory;
        this.myFriendName = myFriendName;
    }

    public String getMyCategory() {
        return myCategory;
    }

    public void setMyCategory(String myCategory) {
        this.myCategory = myCategory;
    }

    public String getMyFriendCategory() {
        return myFriendCategory;
    }

    public void setMyFriendCategory(String myFriendCategory) {
        this.myFriendCategory = myFriendCategory;
    }

    public String getMyFriendName() {
        return myFriendName;
    }

    public void setMyFriendName(String myFriendName) {
        this.myFriendName = myFriendName;
    }
}
