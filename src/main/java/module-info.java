module mapify.mapify {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens mapify.mapify to javafx.fxml;
    exports mapify.mapify;
}