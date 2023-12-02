package mapify.mapify;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    @FXML
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/getStarted.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/mapify/mapify/assets/css/getStarted.css")).toExternalForm());
        stage.setTitle("Mapify");
        stage.setScene(scene);
        Image appIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/mapify/mapify/assets/images/Mapify.png")));
        stage.getIcons().add(appIcon);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}