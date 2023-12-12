package mapify.mapify;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;

public class MapGeocode {
    private final String googleMapKey = "AIzaSyBCAOYM3zI6M4n0e9LpTerxM0QnU9ZjNfE";
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
    public Controller.Location getLocation(String address) {
        String getLocationLink = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + googleMapKey;
        String formattedLink = getLocationLink.replaceAll(" ", "%2C");
        JSONObject response = performFetchRequest(formattedLink);
        if (response != null) {
            JSONArray results = response.getJSONArray("results");
            if (results.length() > 0) {
                JSONObject resultObject = results.getJSONObject(0);
                JSONObject geometry = resultObject.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                String lat = String.valueOf(location.getDouble("lat"));
                String lng = String.valueOf(location.getDouble("lng"));
                return new Controller.Location(lat,lng);
            }
        }

        return null;
    }
    private LocationResult getLocationDetails(String placeId) {
        String locationDetailLink = "https://maps.googleapis.com/maps/api/place/autocomplete/json&place_id=" + placeId + "&key=" + googleMapKey;
        JSONObject response = performFetchRequest(locationDetailLink);
        if (response != null) {
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
    public void autoComplete(String address) {
        Controller.Location addressLocation = getLocation(address);
        if (addressLocation != null) {
            String autoCompleteLink = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+ address + "&location=" + addressLocation.latitude() + "%2C" + addressLocation.longitude() + "&radius=1000" + "&types=geocode&key=" + googleMapKey;
            String formattedLink = autoCompleteLink.replaceAll(" ", "%2C");
            JSONObject response = performFetchRequest(formattedLink);
            if (response != null) {
                JSONArray predictions = response.getJSONArray("predictions");
                for (int i = 0; i < predictions.length(); i++) {
                    JSONObject prediction = predictions.getJSONObject(i);
                    String placeId = prediction.getString("place_id");
                    LocationResult result = getLocationDetails(placeId);
                    if (result != null) {
                        System.out.println(result.toString());
                    }
                }
            }
        }
    }
}
