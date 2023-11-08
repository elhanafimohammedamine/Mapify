module mapify.mapify {
    requires javafx.controls;
    requires javafx.fxml;


    opens mapify.mapify to javafx.fxml;
    exports mapify.mapify;
}