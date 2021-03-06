package db;

public class MemberLocation {
	private int sk;
	private int member_sk;
	private String google_title;
	private String user_title;
	private double latitude;
	private double longitude;
	private String latest_date;
	private String post_date;
	
	public MemberLocation() {
		super();
	}
	
	public MemberLocation(int sk, int member_sk, String google_title, String user_title, String latest_date,
			String post_date, double latitude, double longitude) {
		super();
		this.sk = sk;
		this.member_sk = member_sk;
		this.google_title = google_title;
		this.user_title = user_title;
		this.latest_date = latest_date;
		this.post_date = post_date;
		this.latitude = latitude;
		this.longitude = longitude;
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


	public String getGoogle_title() {
		return google_title;
	}


	public void setGoogle_title(String google_title) {
		this.google_title = google_title;
	}


	public String getuser_title() {
		return user_title;
	}


	public void setuser_title(String user_title) {
		this.user_title = user_title;
	}


	public String getLatest_date() {
		return latest_date;
	}


	public void setLatest_date(String latest_date) {
		this.latest_date = latest_date;
	}


	public String getPost_date() {
		return post_date;
	}


	public void setPost_date(String post_date) {
		this.post_date = post_date;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


}
