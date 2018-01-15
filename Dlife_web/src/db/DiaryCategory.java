package db;

public class DiaryCategory {
	
    private int sk;
    private int member_sk;
    private int diary_sk;
    private int category_sk;
    private int top_category_sk;
    private String category_type;
    private String post_date;
    
    public DiaryCategory() {
    		super();
    }

	public DiaryCategory(int sk, int member_sk, int diary_sk, int category_sk, int top_category_sk,
			String category_type, String post_date) {
		super();
		this.sk = sk;
		this.member_sk = member_sk;
		this.diary_sk = diary_sk;
		this.category_sk = category_sk;
		this.top_category_sk = top_category_sk;
		this.category_type = category_type;
		this.post_date = post_date;
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

	public int getDiary_sk() {
		return diary_sk;
	}

	public void setDiary_sk(int diary_sk) {
		this.diary_sk = diary_sk;
	}

	public int getCategory_sk() {
		return category_sk;
	}

	public void setCategory_sk(int category_sk) {
		this.category_sk = category_sk;
	}

	public int getTop_category_sk() {
		return top_category_sk;
	}

	public void setTop_category_sk(int top_category_sk) {
		this.top_category_sk = top_category_sk;
	}

	public String getCategory_type() {
		return category_type;
	}

	public void setCategory_type(String category_type) {
		this.category_type = category_type;
	}

	public String getPost_date() {
		return post_date;
	}

	public void setPost_date(String post_date) {
		this.post_date = post_date;
	}

}
