package db;

public class MemberMatch {
	
	private int sk;
	private int request_member_sk;
	private int request_top_category_sk;
	private int accept_member_sk;
	private int accept_top_category_sk;
	private String post_day;
	private String post_date;
	
	public MemberMatch() {
		super();
	}
	public MemberMatch(int sk, int request_member_sk, int request_top_category_sk, int accept_member_sk,
			int accept_top_category_sk, String post_day, String post_date) {
		super();
		this.sk = sk;
		this.request_member_sk = request_member_sk;
		this.request_top_category_sk = request_top_category_sk;
		this.accept_member_sk = accept_member_sk;
		this.accept_top_category_sk = accept_top_category_sk;
		this.post_day = post_day;
		this.post_date = post_date;
	}
	public int getSk() {
		return sk;
	}
	public void setSk(int sk) {
		this.sk = sk;
	}
	public int getRequest_member_sk() {
		return request_member_sk;
	}
	public void setRequest_member_sk(int request_member_sk) {
		this.request_member_sk = request_member_sk;
	}
	public int getRequest_top_category_sk() {
		return request_top_category_sk;
	}
	public void setRequest_top_category_sk(int request_top_category_sk) {
		this.request_top_category_sk = request_top_category_sk;
	}
	public int getAccept_member_sk() {
		return accept_member_sk;
	}
	public void setAccept_member_sk(int accept_member_sk) {
		this.accept_member_sk = accept_member_sk;
	}
	public int getAccept_top_category_sk() {
		return accept_top_category_sk;
	}
	public void setAccept_top_category_sk(int accept_top_category_sk) {
		this.accept_top_category_sk = accept_top_category_sk;
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
