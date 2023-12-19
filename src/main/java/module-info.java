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
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;


    opens mapify.mapify to javafx.fxml;
    exports mapify.mapify;
    exports mapify.mapify.Controllers;
    opens mapify.mapify.Controllers to javafx.fxml;
    exports mapify.mapify.Models;
    opens mapify.mapify.Models to javafx.fxml;
    exports mapify.mapify.APIs;
    opens mapify.mapify.APIs to javafx.fxml;
}