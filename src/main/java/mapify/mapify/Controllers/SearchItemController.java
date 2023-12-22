package mapify.mapify.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import mapify.mapify.Models.LocationResult;
import mapify.mapify.Models.User;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class SearchItemController implements Initializable {
    @FXML
    private Label searchResultLabel;
    @FXML
    private ImageView resultIcon;
    @FXML
    private Button searchItemBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void setData(String result) {
        searchResultLabel.setText(result);
    }

    public void hideItemIcon() {
        resultIcon.setVisible(false);
    }
}
