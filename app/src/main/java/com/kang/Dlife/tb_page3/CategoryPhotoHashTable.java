package com.kang.Dlife.tb_page3;

import com.kang.Dlife.Common;

import java.util.Hashtable;

/**
 * Created by regan on 2018/3/5.
 */

class CategoryPhotoHashTable {

    Hashtable<String,Integer> CategoryPotoTable = new Hashtable<String,Integer>();

    CategoryPhotoHashTable(){
        String[] categoryArray = Common.DEFAULTCATE;

        for (int i=0;i<categoryArray.length;i++){
            if(!categoryArray[i].equals(Common.NONSHARECATE[0])) {
                CategoryPotoTable.put(categoryArray[i], 0);
            }
        }
    }

    public Hashtable<String,Integer> getinit() {
        return CategoryPotoTable;
    }
}
