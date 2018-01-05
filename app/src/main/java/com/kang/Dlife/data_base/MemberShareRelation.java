package com.kang.Dlife.data_base;


public class MemberShareRelation {

    private int sk;
    private int from_member_sk;
    private int to_member_sk;
    private int category_sk;
    private String is_shareable;
    private String post_day;
    private String post_date;

    public MemberShareRelation() {
        super();
    }

    public MemberShareRelation(int sk, int from_member_sk, int to_member_sk, int category_sk, String is_shareable,
                               String post_day, String post_date) {
        super();
        this.sk = sk;
        this.from_member_sk = from_member_sk;
        this.to_member_sk = to_member_sk;
        this.category_sk = category_sk;
        this.is_shareable = is_shareable;
        this.post_day = post_day;
        this.post_date = post_date;
    }


    public int getSk() {
        return sk;
    }

    public void setSk(int sk) {
        this.sk = sk;
    }

    public int getFrom_member_sk() {
        return from_member_sk;
    }

    public void setFrom_member_sk(int from_member_sk) {
        this.from_member_sk = from_member_sk;
    }

    public int getTo_member_sk() {
        return to_member_sk;
    }

    public void setTo_member_sk(int to_member_sk) {
        this.to_member_sk = to_member_sk;
    }

    public int getCategory_sk() {
        return category_sk;
    }

    public void setCategory_sk(int category_sk) {
        this.category_sk = category_sk;
    }

    public String getIs_shareable() {
        return is_shareable;
    }

    public void setIs_shareable(String is_shareable) {
        this.is_shareable = is_shareable;
    }

    public String getPost_day() {
        return post_day;
    }

    public void setPost_day(String post_day) {
        this.post_day = post_day;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

}
