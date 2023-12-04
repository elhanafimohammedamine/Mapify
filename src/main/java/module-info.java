module mapify.mapify {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.graalvm.sdk;
    requires java.scripting;


    opens mapify.mapify to javafx.fxml;
    exports mapify.mapify;
}