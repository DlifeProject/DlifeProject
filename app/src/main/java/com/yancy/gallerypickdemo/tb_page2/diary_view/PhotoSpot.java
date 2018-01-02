package com.yancy.gallerypickdemo.tb_page2.diary_view;

import java.io.Serializable;

/**
 * Created by weisunquan on 2017/12/6.
 */

public class PhotoSpot implements Serializable{

    private    int image;

    public PhotoSpot(int image) {
        super();
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
