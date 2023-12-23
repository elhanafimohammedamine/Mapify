package mapify.mapify.Controllers;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Arc;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import mapify.mapify.Models.LocationResult;
import mapify.mapify.APIs.MapGeocode;
import mapify.mapify.Models.User;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;


public class Controller implements Initializable {
    private List<User> userList = new ArrayList<>();
    private static final MapGeocode geocodeInstance = new MapGeocode();
    private Location deviceLocation = new Location(null,null);
    private int circleRadius = 0;
    private boolean isRadiusChanged = false;
    private String oldSearchText = null;
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
    @FXML
    TextField searchBarLabel;
    @FXML
    VBox searchResultsBox;
    @FXML
    VBox searchResultList;
    @FXML
    Label searchResultLabel;
    @FXML
    private Arc searchLoader;
    @FXML
    private AnchorPane searchLoaderContainer;
    private FileChooserController fileChooserController = null;
    private UsersListController usersListController = null;
    private LoaderController loaderController = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engine = mapView.getEngine();
        engine.load(Objects.requireNonNull(getClass().getResource("/scripts/mapView.html")).toExternalForm());
        MapLayersMenu.setVisible(false);
        searchResultsBox.setVisible(false);
        searchLoaderContainer.setVisible(false);
        loadSideBarComponent();
        handleRadiusChange();
        trackSearchLabel();
    }
    private void loadSideBarComponent() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/mapify/mapify/components/fileChooser.fxml")));
            Node node = loader.load();
            fileChooserController = loader.getController();
            sideBarContent.getChildren().clear();
            sideBarContent.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLoaderComponent() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/mapify/mapify/components/loader.fxml")));
            Node node = loader.load();
            loaderController = loader.getController();
            sideBarContent.getChildren().clear();
            sideBarContent.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void checkCsvFileFormatAndGetUsersAddresses() throws Exception {
        CsvParserController csvController = new CsvParserController();
        if (csvController.checkForFileHeadersAndFormat(fileChooserController.getMainFile())) {
            userList = csvController.getCSVData(fileChooserController.getMainFile());
            if (userList.size() != 0) {
                loadLoaderComponent();
                CompletableFuture<Void> searchTask = CompletableFuture.runAsync(() -> {
                    for (User user : userList) {
                        searchForUserLocation(user);
                    }
                });
                searchTask.thenRun(() -> {
                    Platform.runLater(() -> {
                        try {
                            loadUsersListComponent(userList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
        } else {
            fileChooserController.showFileErrorComponent();
        }
    }


    private void searchForUserLocation(User user) {
        Location userLocation = geocodeInstance.getLocation(user.getAddress());
        user.setAddressLocation(userLocation);
    }
    private void loadUsersListComponent(List<User> userList) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/mapify/mapify/components/usersList.fxml")));
        Node node = loader.load();
        usersListController = loader.getController();
        sideBarContent.getChildren().clear();
        for (User user : userList) {
            loadUserItem(user);
        }
        sideBarContent.getChildren().add(node);
        addUsersMarkersToMap(userList);
    }
    private void loadUserItem(User user) throws IOException {
        FXMLLoader userLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/mapify/mapify/components/userItem.fxml")));
        Button userItem = userLoader.load();
        if (userItem != null) {
            UserItemController userItemController = userLoader.getController();
            userItemController.setUserComponentData(user);
            userItem.setOnAction(event -> showUserPopup(user));
            usersListController.addUserComponentToList(userItem);
        }
    }
    private void addUsersMarkersToMap(List<User> users) {
        JSONArray jsonArray = new JSONArray();
        for (User user : users) {
            if(user.getAddressLocation() != null) {
                JSONObject json = new JSONObject();
                json.put("lat", Double.parseDouble(user.getAddressLocation().latitude));
                json.put("lng", Double.parseDouble(user.getAddressLocation().longitude));
                jsonArray.put(json);
            }
        }
        String jsonString = jsonArray.toString();
        engine.executeScript("setUsersMarker(" + jsonString + ")");
    }
    private void showUserPopup(User user) {
        JSONObject json = new JSONObject();
        json.put("lat", Double.parseDouble(user.getAddressLocation().latitude));
        json.put("lng", Double.parseDouble(user.getAddressLocation().longitude));
        json.put("fullName", user.getLastName() + " " + user.getFirstName());
        json.put("address", user.getAddress());
        json.put("phoneNumber", user.getPhoneNumber());
        String userJson = json.toString();
        engine.executeScript("displayPopup('" + userJson + "')");
    }
    private boolean checkUserAddressLocation(User user) {
        if (user.getAddressLocation() != null) {
            return true;
        }
        
        return false;
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

    public void mapZoom(ActionEvent event) throws Exception {
        Button zoomBtn = (Button)event.getSource();
        String clickedZoomBtn = zoomBtn.getId();
        engine.executeScript("mapZoom('" + clickedZoomBtn + "')");
        checkCsvFileFormatAndGetUsersAddresses();
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
            deviceLocation = geocodeInstance.getDeviceLocation();
            engine.executeScript("goToLocation(" + deviceLocation.latitude() + "," + deviceLocation.longitude() + ", userPositionIcon)");
        }
    }

    @FXML
    private void performSearchOnMap() throws IOException {
        AtomicReference<ArrayList<LocationResult>> autoCompleteResults = new AtomicReference<>(new ArrayList<>());
        String searchAddress = searchBarLabel.getText();
        if (!Objects.equals(searchAddress, " ") && !Objects.equals(searchAddress, oldSearchText)) {
            oldSearchText = searchAddress;
            animateSearchLoader(true);
            CompletableFuture<Void> searchTask = CompletableFuture.runAsync(() -> {
                autoCompleteResults.set(geocodeInstance.autoComplete(searchAddress));
            });
            searchTask.thenRun(() -> {
                Platform.runLater(() -> {
                    if (autoCompleteResults.get().size() > 0) {
                        searchResultsBox.setVisible(true);
                        for (LocationResult result : autoCompleteResults.get()) {
                            loadSearchResult(result);
                        }
                    }
                    else {
                        searchBarLabel.setText("No places Found");
                    }
                    animateSearchLoader(false);
                });
            });

        }

    }
    private void loadSearchResult(LocationResult result){
        // loading the search result item component
        FXMLLoader resultLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/mapify/mapify/components/searchResultItem.fxml")));
        Button resultButton = null;
        try {
            resultButton = resultLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (resultButton != null) {
            // get the search result controller and set the data to the component
            SearchItemController searchResultItemController = resultLoader.getController();
            searchResultItemController.setData(result.getPlaceName());
            resultButton.setOnAction(event -> setMapViewToResultLocation(result.getPlaceLat(), result.getPlaceLng(), result.getPlaceName()));
            searchResultsBox.getChildren().add(resultButton);
        }
    }
    private void setMapViewToResultLocation(double lat, double lng, String placeName) {
        engine.executeScript("goToLocation(" + lat + "," + lng + ", normalPositionIcon)");
        searchBarLabel.setText(placeName);
        searchResultsBox.setVisible(false);
    }

    // track the value of search label to hide the previous results
    private void trackSearchLabel() {
        searchBarLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                searchResultsBox.getChildren().clear();
                searchResultsBox.setVisible(false);
            }
        });
    }

    public void animateSearchLoader(boolean state){
        searchLoaderContainer.setVisible(state);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(searchLoader.lengthProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(searchLoader.lengthProperty(), 360))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        if (state) {
            timeline.play();
        }
        else {
            timeline.stop();
        }

    }

    // record to hold the device location
    public record Location(String latitude, String longitude) {
    }

}
