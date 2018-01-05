package com.kang.Dlife.data_base;


public class Category {

    private int sk;
    private int member_sk;
    private String tag_title;
    private String category_type;
    private String is_shareable;
    private String is_top_category;
    private String is_default;
    private String is_useful;
    private int belong_category_sk;

    public Category() {
        super();
    }

    public Category(int sk, int member_sk, String tag_title, String category_type, String is_shareable,
                    String is_top_category, String is_default, String is_useful, int belong_category_sk) {
        super();
        this.sk = sk;
        this.member_sk = member_sk;
        this.tag_title = tag_title;
        this.category_type = category_type;
        this.is_shareable = is_shareable;
        this.is_top_category = is_top_category;
        this.is_default = is_default;
        this.is_useful = is_useful;
        this.belong_category_sk = belong_category_sk;
    }


    public int getSk() {
        return sk;
    }


    public void setSk(int sk) {
        this.sk = sk;
    }


    public int getMember_sk() {
        return member_sk;
    }


    public void setMember_sk(int member_sk) {
        this.member_sk = member_sk;
    }


    public String getTag_title() {
        return tag_title;
    }


    public void setTag_title(String tag_title) {
        this.tag_title = tag_title;
    }


    public String getCategory_type() {
        return category_type;
    }


    public void setCategory_type(String category_type) {
        this.category_type = category_type;
    }


    public String getIs_shareable() {
        return is_shareable;
    }


    public void setIs_shareable(String is_shareable) {
        this.is_shareable = is_shareable;
    }


    public String getIs_top_category() {
        return is_top_category;
    }


    public void setIs_top_category(String is_top_category) {
        this.is_top_category = is_top_category;
    }


    public String getIs_default() {
        return is_default;
    }


    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }


    public String getIs_useful() {
        return is_useful;
    }


    public void setIs_useful(String is_useful) {
        this.is_useful = is_useful;
    }


    public int getBelong_category_sk() {
        return belong_category_sk;
    }


    public void setBelong_category_sk(int belong_category_sk) {
        this.belong_category_sk = belong_category_sk;
    }
}
