package api;

public class DiarySpot {
    private String diary;
    private String category;


    public DiarySpot(String diary, String category) {
        this.diary = diary;
        this.category = category;

    }

    public String getDiary() {
        return diary;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setDiary(String diary) {
        this.diary = diary;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
