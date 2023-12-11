package mapify.mapify;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import netscape.javascript.JSObject;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private static final String IP_API_URL = "https://ipinfo.io/json";
    private Location deviceLocation = new Location(null,null);
    private int circleRadius = 0;
    private boolean isRadiusChanged = false;
    private static WebEngine engine;
    @FXML
    private WebView mapView;
    @FXML
    private VBox sideBarContent;
    @FXML
    private HBox MapLayersMenu;
    @FXML
    private HBox distanceBarContainer;
    @FXML
    private Button LayersChangerBtn;
    @FXML
    private Button locationBtn;
    @FXML
    private Button zoomInBtn;
    @FXML
    private Button zoomOutBtn;
    @FXML
    Slider downBarSlider;
    @FXML
    Label sliderLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engine = mapView.getEngine();
        engine.load(Objects.requireNonNull(getClass().getResource("/scripts/mapView.html")).toExternalForm());
        MapLayersMenu.setVisible(false);
        loadSideBarComponent();
        handleRadiusChange();
    }
    private void loadSideBarComponent() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/mapify/mapify/components/fileChooser.fxml")));
            Node node = loader.load();
            sideBarContent.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMapLayerMenu() {
        boolean visibility = MapLayersMenu.isVisible();
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), MapLayersMenu);
        if (visibility) {
            fadeTransition.setToValue(0);
        } else {
            fadeTransition.setToValue(1);
        }
        fadeTransition.setOnFinished(ev -> MapLayersMenu.setVisible(!visibility));
        fadeTransition.play();
        // hide map layers menu when clicking outside the button
        Scene scene = MapLayersMenu.getScene();
        if (scene != null) {
            ((Scene) scene).setOnMouseClicked(e -> {
                if (!MapLayersMenu.getBoundsInParent().contains(e.getX(), e.getY())) {
                    fadeTransition.setToValue(0);
                    fadeTransition.setOnFinished(ev -> MapLayersMenu.setVisible(false));
                    fadeTransition.play();
                    scene.setOnMouseClicked(null);
                    String buttonStyle = "downBarBtn";
                    LayersChangerBtn.getStyleClass().clear();
                    LayersChangerBtn.getStyleClass().add(buttonStyle);
                }
            });
        }
        // changing button style when it is clicked
        String buttonStyle = visibility ? "downBarBtn" : "downBarBtnClicked" ;
        LayersChangerBtn.getStyleClass().clear();
        LayersChangerBtn.getStyleClass().add(buttonStyle);
    }
    @FXML
    private void changeMapLayer(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        engine.executeScript("mapLayerChooser('" + buttonId + "')");
        showMapLayerMenu();
    }

    public void mapZoom(ActionEvent event) {
        Button zoomBtn = (Button)event.getSource();
        String clickedZoomBtn = zoomBtn.getId();
        engine.executeScript("mapZoom('" + clickedZoomBtn + "')");

    }

    private void handleRadiusChange() {
        circleRadius = (int) downBarSlider.getValue();
        sliderLabel.setText(convertToMeterToKM(circleRadius));
        downBarSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                circleRadius = (int) downBarSlider.getValue();
                // invoke the function that changes the circle radius in javascript code
                invokeJSCodeOnRadiusChange();
                sliderLabel.setText(convertToMeterToKM(circleRadius));
            }
        });
    }
    private void invokeJSCodeOnRadiusChange() {
        // check if the circle radius has changed for the first time
        // to call getCurrentLocation
        if (circleRadius > 0 && !isRadiusChanged) {
            isRadiusChanged = true;
            try {
                getCurrentLocation();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            engine.executeScript("displayCircle('" + circleRadius + "')");
        }
        else {
            // if the map view is not set to the device location when trying to change the circle radius
            // we set the map view to the device location then change the radius
            if (!isDeviceLocationInMapView()) {
                String script = "map.setView([" + deviceLocation.latitude() + ", " + deviceLocation.longitude() + "], 13)";
                engine.executeScript(script);
            }
            engine.executeScript("circle.setRadius('" + circleRadius + "')");
        }
    }
    // check if the map view is set to the device location or not
    private boolean isDeviceLocationInMapView() {
        JSObject result = (JSObject) engine.executeScript("getMapView()");
        double latitude = (double) result.getMember("lat");
        double longitude = (double) result.getMember("lng");
        return longitude == Double.parseDouble(deviceLocation.longitude()) && latitude == Double.parseDouble(deviceLocation.latitude());
    }
    private String convertToMeterToKM(int distanceInMeter) {
        if (distanceInMeter > 999) {
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            return decimalFormat.format((double) distanceInMeter/1000) + " Km";
        }
        return Integer.toString(distanceInMeter) + " m";
    }
    public void getCurrentLocation() throws IOException {
        // check if the device is already located or not
        if (deviceLocation.latitude() == null && deviceLocation.longitude() == null) {
            deviceLocation = fetchLocationData();
            engine.executeScript("locateMapUser(" + deviceLocation.latitude() + "," + deviceLocation.longitude() + ")");
        }
    }
    // find the location of the ip address of the device
    private Location fetchLocationData() throws IOException {
        URL url = new URL(IP_API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        JSONObject json = new JSONObject(response.toString());
        String loc = json.getString("loc");
        String[] coordinates = loc.split(",");
        String latitude = coordinates[0];
        String longitude = coordinates[1];
        return new Location(latitude, longitude);
    }

    // record to hold the device location
    public record Location(String latitude, String longitude) {
    }

}
