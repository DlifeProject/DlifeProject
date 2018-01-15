package db;

public class LocationTrace {

    private int sk;
    private int mobile_sk;
    private int member_sk;
    private double longitude;
    private double latitude;
    private double altitude;
    private double speed;
    private double accuracy;
    private int forward_sk;
    private double distance;
    private int is_update;
    private String update_date;
    private String post_stamp;
    private String post_date;
    
    public LocationTrace() {
    		super();
    }

    public LocationTrace(LocationTrace l) {
    	
		super();
		this.sk = l.getSk();
		this.mobile_sk = l.getMobile_sk();
		this.member_sk = l.getMember_sk();
		this.longitude = l.getLongitude();
		this.latitude = l.getLatitude();
		this.altitude = l.getAltitude();
		this.speed = l.getSpeed();
		this.accuracy = l.getAccuracy();
		this.forward_sk = l.getForward_sk();
		this.distance = l.getDistance();
		this.is_update = l.getIs_update();
		this.update_date = l.getUpdate_date();
		this.post_stamp = l.getPost_stamp();
		this.post_date = l.getPost_date();
    }   

    public LocationTrace(int sk, int mobile_sk, int member_sk, double longitude, double latitude, double altitude,
			double speed, double accuracy, int forward_sk, double distance, int is_update, String update_date,
			String post_stamp, String post_date) {
		super();
		this.sk = sk;
		this.mobile_sk = mobile_sk;
		this.member_sk = member_sk;
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
		this.speed = speed;
		this.accuracy = accuracy;
		this.forward_sk = forward_sk;
		this.distance = distance;
		this.is_update = is_update;
		this.update_date = update_date;
		this.post_stamp = post_stamp;
		this.post_date = post_date;
	}

	public int getSk() {
        return sk;
    }

    public void setSk(int sk) {
        this.sk = sk;
    }
    
	public int getMobile_sk() {
		return mobile_sk;
	}

	public void setMobile_sk(int mobile_sk) {
		this.mobile_sk = mobile_sk;
	}	   

    public int getMember_sk() {
        return member_sk;
    }

    public void setMember_sk(int member_sk) {
        this.member_sk = member_sk;
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

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getForward_sk() {
        return forward_sk;
    }

    public void setForward_sk(int forward_sk) {
        this.forward_sk = forward_sk;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getIs_update() {
        return is_update;
    }

    public void setIs_update(int is_update) {
        this.is_update = is_update;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getPost_stamp() {
        return post_stamp;
    }

    public void setPost_stamp(String post_stamp) {
        this.post_stamp = post_stamp;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

}
