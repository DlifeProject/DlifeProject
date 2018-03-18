package com.kang.Dlife.tb_page4;

/**
 * Created by weisunquan on 2018/3/5.
 */

public class FacebookMember {
    private String name;
    private String id;

    public FacebookMember(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}