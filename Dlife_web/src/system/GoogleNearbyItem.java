package system;

public class GoogleNearbyItem {
	public String name;
	public String placeID ;
	public double latitude;
	public double longitude;
	public GoogleNearbyItem(String name, String placeID, double latitude, double longitude) {
		super();
		this.name = name;
		this.placeID = placeID;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
