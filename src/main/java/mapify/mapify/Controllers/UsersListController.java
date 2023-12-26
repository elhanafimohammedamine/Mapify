package mapify.mapify.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mapify.mapify.Models.User;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UsersListController implements Initializable {

    @FXML
    private VBox usersList;
    private File csvFile = null;
    @FXML
    private Button saveFileBtn;
    @FXML
    private Button backBtn;
    private List<User> users = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void addUserComponentToList(Button userItem) {
        usersList.getChildren().add(userItem);
    }
    public Button getSaveFileBtn() {
        return this.saveFileBtn;
    }
    public Button getBackBtn() {
        return this.backBtn;
    }
}
