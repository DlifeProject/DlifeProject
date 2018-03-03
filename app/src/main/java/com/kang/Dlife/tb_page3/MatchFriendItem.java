package com.kang.Dlife.tb_page3;

/**
 * Created by regan on 2018/1/30.
 */

class MatchFriendItem {

    private String myCategory;
    private String myFriendCategory;
    private String myFriendName;
    private boolean isShareable = true;
    private String postDay = "0000-00-00";

    public MatchFriendItem() {
        super();
        this.myCategory = "-";
        this.myFriendCategory = "-";
        this.myFriendName = "Unknown";
    }

    public MatchFriendItem(String myCategory, String myFriendCategory, String myFriendName) {
        super();
        this.myCategory = myCategory;
        this.myFriendCategory = myFriendCategory;
        this.myFriendName = myFriendName;
    }

    public MatchFriendItem(String myCategory, String myFriendCategory, String myFriendName, boolean isShareable, String postDay) {
        super();
        this.myCategory = myCategory;
        this.myFriendCategory = myFriendCategory;
        this.myFriendName = myFriendName;
        this.isShareable = isShareable;
        this.postDay = postDay;
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

    public boolean isShareable() {
        return isShareable;
    }

    public void setShareable(boolean shareable) {
        isShareable = shareable;
    }

    public String getPostDay() {
        return postDay;
    }

    public void setPostDay(String postDay) {
        this.postDay = postDay;
    }
}
