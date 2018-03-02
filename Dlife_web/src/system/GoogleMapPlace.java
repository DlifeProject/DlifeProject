package system;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class GoogleMapPlace {
	
	//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=座標&radius=搜尋範圍&types=種類&sensor=true&key=server api key
	
	public final static String googlePlaceURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	public final static int radius = 50;
	public final static String googleMapAPIKey = "AIzaSyDGkE4FG31EUpY8-EA8lO5HH4SN8bDneuE";
	public String googlePlaceLocation = "";
	public String googlePlace = "";
	private String latitude = "";
	private String longitude = "";
	private Map<String, String> sendData = new HashMap<>();
	private String json;
	
	public GoogleMapPlace(Long latitude, Long longitude) {
		super();
		this.latitude = latitude.toString();
		this.longitude = longitude.toString();
		setSendData();
	}
	
	public GoogleMapPlace(String latitude, String longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		setSendData();
	}
	
    public String getLocationJson(){
        HttpRequest httpRequest = new HttpRequest("GET", googlePlaceURL, sendData);
        json = httpRequest.doRequest().getOutput();
        return json;
    }
    
    public void parserJson() {
        JSONObject jObject = new JSONObject(json);
        for(int i = 0;i<jObject.getJSONArray("results").length();i++){
            System.out.println("results name" + jObject.getJSONArray("results").getJSONObject(i).getString("name"));

        }
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
	
	public void setSendData() {
		String location = this.latitude + "," + this.longitude;
		sendData.put("location", location);
		sendData.put("radius", String.valueOf(radius));
		sendData.put("sensor", "true");
		sendData.put("key", googleMapAPIKey);
	}
	
	
	
	
	
	
	

}
