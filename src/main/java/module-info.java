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

    opens org.example.demo to javafx.fxml;
    exports org.example.demo;



}