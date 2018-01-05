package com.kang.Dlife.data_base;


public class Chatroom {

    private int sk;
    private String room_title;
    private String is_useful;
    private String post_date;

    public Chatroom() {
        super();
    }

    public Chatroom(int sk, String room_title, String is_useful, String post_date) {
        super();
        this.sk = sk;
        this.room_title = room_title;
        this.is_useful = is_useful;
        this.post_date = post_date;
    }

    public int getSk() {
        return sk;
    }

    public void setSk(int sk) {
        this.sk = sk;
    }

    public String getRoom_title() {
        return room_title;
    }

    public void setRoom_title(String room_title) {
        this.room_title = room_title;
    }

    public String getIs_useful() {
        return is_useful;
    }

    public void setIs_useful(String is_useful) {
        this.is_useful = is_useful;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }
}
