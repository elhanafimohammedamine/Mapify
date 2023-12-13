package mapify.mapify.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchItemController implements Initializable {
    @FXML
    private Label searchResultLabel;
    @FXML
    private ImageView resultIcon;

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
