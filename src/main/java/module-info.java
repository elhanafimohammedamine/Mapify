module mapify.mapify {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.graalvm.sdk;
    requires java.scripting;
    requires jdk.jsobject;
    requires org.json;


    opens mapify.mapify to javafx.fxml;
    exports mapify.mapify;
}