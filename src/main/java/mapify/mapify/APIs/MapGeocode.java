package mapify.mapify.APIs;
import mapify.mapify.Controllers.Controller;
import mapify.mapify.Models.LocationResult;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MapGeocode {
    private final String googleMapKey = System.getenv("GOOGLE_API_KEY");
    private static final String IP_API_URL = "https://ipinfo.io/json";
    private ArrayList<LocationResult> predictedLocations = new ArrayList<>();
    public MapGeocode() {

    }
    private JSONObject performFetchRequest(String link) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(link);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode >= 200 && statusCode < 300) {
                    HttpEntity entity = response.getEntity();
                    String responseBody = EntityUtils.toString(entity);
                    return new JSONObject(responseBody);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Distance getDistanceBetweenTwoPoints(Controller.Location origin, Controller.Location destination, String mode){
        double originLat = origin.latitude();
        double originLng = origin.longitude();
        double destinationLat = destination.latitude();
        double destinationLng = destination.longitude();
        String getLocationLink = "https://maps.googleapis.com/maps/api/distancematrix/json?destinations="
                + destinationLat + "%20" +
                + destinationLng + "&origins="
                + originLat + "%20"
                + originLng +
                "&mode=" + mode + "&key=" + googleMapKey;
        String formattedLink = formatAddress(getLocationLink);
        JSONObject response = performFetchRequest(formattedLink);
        if (response != null) {
            System.out.println(response);
            JSONArray results = response.getJSONArray("rows");
            if (results.length() > 0) {
                JSONObject resultObject = results.getJSONObject(0);
                JSONArray elements = resultObject.getJSONArray("elements");
                JSONObject elementsObject = elements.getJSONObject(0);
                JSONObject distance = elementsObject.getJSONObject("distance");
                int distanceInKm = distance.getInt("value");
                JSONObject duration = elementsObject.getJSONObject("duration");
                int durationValue = duration.getInt("value");
                return new Distance(distanceInKm, durationValue);
            }
        }
        return null;
    }
    public Controller.Location getLocation(String address) {
        String getLocationLink = "https://maps.googleapis.com/maps/api/place/textsearch/json?fields=formatted_address%2Cgeometry&query=" + address + "&key=" + googleMapKey;
        String formattedLink = formatAddress(getLocationLink);
        JSONObject response = performFetchRequest(formattedLink);
        if (response != null) {
            JSONArray results = response.getJSONArray("results");
            if (results.length() > 0) {
                JSONObject resultObject = results.getJSONObject(0);
                JSONObject geometry = resultObject.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                double lat =location.getDouble("lat");
                double lng = location.getDouble("lng");
                return new Controller.Location(lat,lng);
            }
        }

        return null;
    }
    private LocationResult getLocationDetails(String placeId) {
        String locationDetailLink = "https://maps.googleapis.com/maps/api/place/details/json?fields=formatted_address%2Cgeometry&place_id=" + placeId + "&key=" + googleMapKey;
        JSONObject response = performFetchRequest(locationDetailLink);
        if (response != null && checkResponseStatus(response)) {
            JSONObject result = response.getJSONObject("result");
            String formattedAddress = result.getString("formatted_address");
            JSONObject geometry = result.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");
            return new LocationResult(formattedAddress, lat, lng, placeId);

        }
        return null;
    }
    public ArrayList<LocationResult> autoComplete(String address) {
        predictedLocations.clear();
        Controller.Location addressLocation = getLocation(address);
        if (addressLocation != null) {
            String autoCompleteLink = "https://maps.googleapis.com/maps/api/place/autocomplete/json?fields=place_id&input="+ address + "&location=" + addressLocation.latitude() + "%2C" + addressLocation.longitude() + "&radius=1000" + "&types=geocode&key=" + googleMapKey;
            String formattedLink = formatAddress(autoCompleteLink);
            JSONObject response = performFetchRequest(formattedLink);
            if (response != null && checkResponseStatus(response)) {
                JSONArray predictions = response.getJSONArray("predictions");
                for (int i = 0; i < predictions.length(); i++) {
                    JSONObject prediction = predictions.getJSONObject(i);
                    String placeId = prediction.getString("place_id");
                    LocationResult result = getLocationDetails(placeId);
                    predictedLocations.add(result);
                }
            }
        }
        return predictedLocations;
    }
    public Controller.Location getDeviceLocation() throws IOException {
        JSONObject response = performFetchRequest(IP_API_URL);
        if (response != null) {
            String loc = response.getString("loc");
            String[] coordinates = loc.split(",");
            double latitude = Double.parseDouble(coordinates[0]);
            double longitude = Double.parseDouble(coordinates[1]);
            return new Controller.Location(latitude, longitude);
        }
        return null;
    }
    private boolean checkResponseStatus(JSONObject response) {
        String status = response.getString("status");
        return Objects.equals(status,"OK");
    }
    private String formatAddress(String address) {
        return address.replaceAll(",", "").replaceAll("\"", "").replaceAll(" ", "%2C");
    }
    public record Distance(int distanceValue, int durationValue) {

    }
}
