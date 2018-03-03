package system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GoogleMapPlace {
	
	//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=座標&radius=搜尋範圍&types=種類&sensor=true&key=server api key
	public final static int MAXITEM = 8;
	public final static String googlePlaceURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	public final static int radius = 50;
	public final static String googleMapAPIKey = "AIzaSyD5YupqW-wsHEO5gxbRISA_zNBZNW8utxI";
	public String googlePlaceLocation = "";
	public String googlePlace = "";
	public ArrayList<GoogleNearbyItem> nearbyList = new ArrayList<GoogleNearbyItem>();
	private String latitude = "";
	private String longitude = "";
	private Map<String, String> sendData = new HashMap<>();
	private String googleResponeJson;
	private String googleNearbyItemsJson;
	
	
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
        googleResponeJson = httpRequest.doRequest().getOutput();
        return googleResponeJson;
    }
    
    public void parserJson() {
        JSONObject jObject = new JSONObject(googleResponeJson);
        for(int i = 0;i<jObject.getJSONArray("results").length();i++){
            System.out.println("results name" + jObject.getJSONArray("results").getJSONObject(i).getString("name"));
        }
    }
    
    public ArrayList<GoogleNearbyItem> getNearbyItemList() {
        JSONObject jObject = new JSONObject(googleResponeJson);
        for(int i = 0;i<jObject.getJSONArray("results").length() && i < MAXITEM ;i++){
        		GoogleNearbyItem nearbyItem = new GoogleNearbyItem(jObject.getJSONArray("results").getJSONObject(i).getString("name"), 
            									 jObject.getJSONArray("results").getJSONObject(i).getString("id"), 
            									 jObject.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
            									 jObject.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
        		nearbyList.add(nearbyItem);

        }   		
    		return nearbyList;
    }
    
    public String getNearbyItemJson() {
    		JsonObject outJsonObject = new JsonObject();
    		outJsonObject.addProperty("nearbyItems", new Gson().toJson(nearbyList));
    		googleNearbyItemsJson = outJsonObject.toString();
    		
    		googleNearbyItemsJson = new Gson().toJson(nearbyList);
    		return googleNearbyItemsJson;
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

	public String getGoogleNearbyItemsJson() {
		return googleNearbyItemsJson;
	}

	public void setGoogleNearbyItemsJson(String googleNearbyItemsJson) {
		this.googleNearbyItemsJson = googleNearbyItemsJson;
	}
}
