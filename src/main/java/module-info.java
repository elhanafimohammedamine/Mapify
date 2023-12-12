module mapify.mapify {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.scripting;
    requires jdk.jsobject;
    requires org.json;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires json.simple;


    opens mapify.mapify to javafx.fxml;
    exports mapify.mapify;
}