package com.kang.Dlife.data_base;


public class ChatroomMember {
    private int sk;
    private int chatroom_sk;
    private int member_sk;
    private String is_useful;
    private String join_stamp;
    private String post_date;

    public ChatroomMember() {
        super();
    }

    public ChatroomMember(int sk, int chatroom_sk, int member_sk, String is_useful, String join_stamp,
                          String post_date) {
        super();
        this.sk = sk;
        this.chatroom_sk = chatroom_sk;
        this.member_sk = member_sk;
        this.is_useful = is_useful;
        this.join_stamp = join_stamp;
        this.post_date = post_date;
    }

    public int getSk() {
        return sk;
    }

    public void setSk(int sk) {
        this.sk = sk;
    }

    public int getChatroom_sk() {
        return chatroom_sk;
    }

    public void setChatroom_sk(int chatroom_sk) {
        this.chatroom_sk = chatroom_sk;
    }

    public int getMember_sk() {
        return member_sk;
    }

    public void setMember_sk(int member_sk) {
        this.member_sk = member_sk;
    }

    public String getIs_useful() {
        return is_useful;
    }

    public void setIs_useful(String is_useful) {
        this.is_useful = is_useful;
    }

    public String getJoin_stamp() {
        return join_stamp;
    }

    public void setJoin_stamp(String join_stamp) {
        this.join_stamp = join_stamp;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }
}
