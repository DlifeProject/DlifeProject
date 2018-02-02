package db;

public class CategoryMatch {
	
	private int sk;
	private int member_sk;
	private int top_category_1_sk;
	private int diary_count_1;
	private int top_category_2_sk;
	private int diary_count_2;
	private int top_category_3_sk;
	private int diary_count_3;
	private int start_diary_detail_sk;
	private int end_diary_detail_sk;
	private String row_data;
	private int shareable_diary_count;
	private String update_day;
	private String post_date;
	
	
	public CategoryMatch() {
		super();
	}
	
	public CategoryMatch(int member_sk) {
		super();
		this.member_sk = member_sk;
	}

	public CategoryMatch(int sk, int member_sk, int top_category_1_sk, int diary_count_1, int top_category_2_sk,
			int diary_count_2, int top_category_3_sk, int diary_count_3, int start_diary_detail_sk,
			int end_diary_detail_sk, String row_data, int shareable_diary_count, String update_day, String post_date) {
		super();
		this.sk = sk;
		this.member_sk = member_sk;
		this.top_category_1_sk = top_category_1_sk;
		this.diary_count_1 = diary_count_1;
		this.top_category_2_sk = top_category_2_sk;
		this.diary_count_2 = diary_count_2;
		this.top_category_3_sk = top_category_3_sk;
		this.diary_count_3 = diary_count_3;
		this.start_diary_detail_sk = start_diary_detail_sk;
		this.end_diary_detail_sk = end_diary_detail_sk;
		this.row_data = row_data;
		this.shareable_diary_count = shareable_diary_count;
		this.update_day = update_day;
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

	public int getTop_category_1_sk() {
		return top_category_1_sk;
	}

	public void setTop_category_1_sk(int top_category_1_sk) {
		this.top_category_1_sk = top_category_1_sk;
	}

	public int getDiary_count_1() {
		return diary_count_1;
	}

	public void setDiary_count_1(int diary_count_1) {
		this.diary_count_1 = diary_count_1;
	}

	public int getTop_category_2_sk() {
		return top_category_2_sk;
	}

	public void setTop_category_2_sk(int top_category_2_sk) {
		this.top_category_2_sk = top_category_2_sk;
	}

	public int getDiary_count_2() {
		return diary_count_2;
	}

	public void setDiary_count_2(int diary_count_2) {
		this.diary_count_2 = diary_count_2;
	}

	public int getTop_category_3_sk() {
		return top_category_3_sk;
	}

	public void setTop_category_3_sk(int top_category_3_sk) {
		this.top_category_3_sk = top_category_3_sk;
	}

	public int getDiary_count_3() {
		return diary_count_3;
	}

	public void setDiary_count_3(int diary_count_3) {
		this.diary_count_3 = diary_count_3;
	}

	public int getStart_diary_detail_sk() {
		return start_diary_detail_sk;
	}

	public void setStart_diary_detail_sk(int start_diary_detail_sk) {
		this.start_diary_detail_sk = start_diary_detail_sk;
	}

	public int getEnd_diary_detail_sk() {
		return end_diary_detail_sk;
	}

	public void setEnd_diary_detail_sk(int end_diary_detail_sk) {
		this.end_diary_detail_sk = end_diary_detail_sk;
	}

	public String getRow_data() {
		return row_data;
	}

	public void setRow_data(String row_data) {
		this.row_data = row_data;
	}

	public int getShareable_diary_count() {
		return shareable_diary_count;
	}

	public void setShareable_diary_count(int shareable_diary_count) {
		this.shareable_diary_count = shareable_diary_count;
	}

	public String getUpdate_day() {
		return update_day;
	}

	public void setUpdate_day(String update_day) {
		this.update_day = update_day;
	}

	public String getPost_date() {
		return post_date;
	}

	public void setPost_date(String post_date) {
		this.post_date = post_date;
	}


	

}
