package mapify.mapify.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import mapify.mapify.Models.User;

import java.net.URL;
import java.util.ResourceBundle;

public class UserItemController implements Initializable {

    @FXML
    private Label userName;
    @FXML
    private Label userAddress;
    @FXML
    private Button trackUserBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void setUserComponentData(User user) {
        userName.setText(user.getLastName() + " " + user.getFirstName());
        userAddress.setText(user.getAddress());
    }
    public Button getTrackButton() {
        return trackUserBtn;
    }
}
