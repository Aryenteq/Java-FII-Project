module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires graph4j;
    requires annotations;
    requires java.sql;
    requires jdk.jsobject;
    requires java.net.http;
    requires org.json;


    exports Main;
    opens Main to javafx.fxml;
    exports Controllers;
    opens Controllers to javafx.fxml;
    exports Data;
    opens Data to javafx.fxml;
    exports utils;
    opens utils to javafx.fxml;


}