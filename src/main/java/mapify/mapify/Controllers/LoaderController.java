package mapify.mapify.Controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Arc;

import java.net.URL;
import java.util.ResourceBundle;

public class LoaderController implements Initializable {

    @FXML
    public Arc loader;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        animateLoader();
    }
    public void animateLoader(){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(loader.lengthProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(loader.lengthProperty(), 360))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

}
