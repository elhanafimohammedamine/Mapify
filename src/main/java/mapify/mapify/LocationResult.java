package mapify.mapify;

public class LocationResult {
    private String placeName;
    private double placeLat;
    private double placeLng;
    private String placeId;

    public LocationResult() {

    }
    public LocationResult(String placeName, double placeLat, double placeLng, String placeId) {
        this.placeName = placeName;
        this.placeLat = placeLat;
        this.placeLng = placeLng;
        this.placeId = placeId;
    }
    public String getPlaceName() {
        return placeName;
    }

    @Override
    public String toString() {
        return "LocationResult{" +
                "placeName='" + placeName + '\'' +
                ", placeLat=" + placeLat +
                ", placeLng=" + placeLng +
                ", placeId='" + placeId + '\'' +
                '}';
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getPlaceLat() {
        return placeLat;
    }

    public void setPlaceLat(double placeLat) {
        this.placeLat = placeLat;
    }

    public double getPlaceLng() {
        return placeLng;
    }

    public void setPlaceLng(double placeLng) {
        this.placeLng = placeLng;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
