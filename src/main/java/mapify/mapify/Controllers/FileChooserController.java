package mapify.mapify.Controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class FileChooserController implements Initializable {

    @FXML
    private Label fileNameLabel;
    @FXML
    private Label fileUploadDateLabel;
    @FXML
    private HBox fileContainer;
    @FXML
    private VBox csvErrorContainer;
    private File mainFile = null;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileContainer.setVisible(false);
        csvErrorContainer.setVisible(false);
    }
    public void chooseFileAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("csv files","*.csv"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            mainFile = selectedFile;
            Date uploadDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String formattedDate = dateFormat.format(uploadDate);
            fileUploadDateLabel.setText(formattedDate);
            csvErrorContainer.setVisible(false);
            fileContainer.setVisible(true);
            fileNameLabel.setText(selectedFile.getName());
        }
        else {
            System.out.println("error !");
        }
    }
    public void showFileErrorComponent() {
        csvErrorContainer.setVisible(true);
    }
    public File getMainFile() {
        return mainFile;
    }

}
