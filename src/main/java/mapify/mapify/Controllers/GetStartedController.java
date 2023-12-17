package mapify.mapify.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mapify.mapify.Application;

import java.io.IOException;
import java.util.Objects;

public class GetStartedController {
    public void switchToAppScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/app.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/mapify/mapify/assets/css/app.css")).toExternalForm());
        stage.setTitle("Mapify");
        stage.show();
    }
}
