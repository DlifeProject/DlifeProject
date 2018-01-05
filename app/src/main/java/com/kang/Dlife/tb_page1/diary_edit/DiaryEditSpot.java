package com.kang.Dlife.tb_page1.diary_edit;

public class DiaryEditSpot {

    private String diary;
    private String category;

    public DiaryEditSpot(String diary, String category) {
        this.diary = diary;
        this.category = category;
    }

    public String getName() {
        return diary;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String diary) {
        this.diary = diary;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
