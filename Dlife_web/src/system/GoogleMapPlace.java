package system;

public class GoogleMapPlace {
	
	//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=座標&radius=搜尋範圍&types=種類&sensor=true&key=server api key
	
	public final static String googlePlaceURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
	public final static int radius = 100;
	public final static String googleMapAPIKey = "AIzaSyDGkE4FG31EUpY8-EA8lO5HH4SN8bDneuE";
	public String googlePlaceLocation = "";
	public String googlePlace = "";
	private String latitude = "";
	private String longitude = "";
	private String fullURL = "";
	
	public GoogleMapPlace(Long latitude, Long longitude) {
		super();
		this.latitude = latitude.toString();
		this.longitude = longitude.toString();
		setFullURL();
	}
	
	public GoogleMapPlace(String latitude, String longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		setFullURL();
	}
	
    public String getLocationJson(){
        String json = "";
  
        return json;
    }

	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getFullURL() {
		fullURL = googlePlaceURL + "?" 
				+ "location=" + this.latitude + "," + this.longitude
				+ "&radius=" + radius
				+ "&sensor=true"
				+ "&key=" + googleMapAPIKey;
		return fullURL;
	}

	public void setFullURL() {
		this.fullURL = getFullURL();
	}
	
	
	
	
	
	
	

}
