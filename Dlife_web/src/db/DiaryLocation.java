package db;

public class DiaryLocation {
	private int sk;
	private int member_sk;
	private int diary_detail_sk;
	private String google_id;
	private String google_name;
	private double longitude;
	private double latitude;
	private String diary_day;
	private String post_date;
	
	public DiaryLocation() {
		super();
	}
	public DiaryLocation(int sk, int member_sk, int diary_detail_sk, String google_id, String google_name,
			double longitude, double latitude, String diary_day, String post_date) {
		super();
		this.sk = sk;
		this.member_sk = member_sk;
		this.diary_detail_sk = diary_detail_sk;
		this.google_id = google_id;
		this.google_name = google_name;
		this.longitude = longitude;
		this.latitude = latitude;
		this.diary_day = diary_day;
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
	public int getDiary_detail_sk() {
		return diary_detail_sk;
	}
	public void setDiary_detail_sk(int diary_detail_sk) {
		this.diary_detail_sk = diary_detail_sk;
	}
	public String getGoogle_id() {
		return google_id;
	}
	public void setGoogle_id(String google_id) {
		this.google_id = google_id;
	}
	public String getGoogle_name() {
		return google_name;
	}
	public void setGoogle_name(String google_name) {
		this.google_name = google_name;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getDiary_day() {
		return diary_day;
	}
	public void setDiary_day(String diary_day) {
		this.diary_day = diary_day;
	}
	public String getPost_date() {
		return post_date;
	}
	public void setPost_date(String post_date) {
		this.post_date = post_date;
	}
	
	
}
