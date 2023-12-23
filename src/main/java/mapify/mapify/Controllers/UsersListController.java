package mapify.mapify.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class UsersListController implements Initializable {

    @FXML
    private VBox usersList;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void addUserComponentToList(Button userItem) {
        usersList.getChildren().add(userItem);
    }
}
