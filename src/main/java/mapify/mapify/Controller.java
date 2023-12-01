package mapify.mapify;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    public VBox sideBarContent;

    @FXML
    private WebView mapView;
    private WebEngine engine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engine = mapView.getEngine();
        engine.load(Objects.requireNonNull(getClass().getResource("/scripts/mapView1.html")).toExternalForm());
        loadSideBarComponent();
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

}
