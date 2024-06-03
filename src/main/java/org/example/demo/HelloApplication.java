package org.example.demo;

import Database.LocationDAO;
import GA.Parameters;
import GA.VehicleRouting;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            LocationDAO locationDAO = new LocationDAO();

            //launch();
            GA.Parameters.setGenerations(1000);
            VehicleRouting solver = new VehicleRouting();
            solver.run();

            // Add/update solver/delete
            //locationDAO.addLocation("Sample Location", 37.7749, -122.4194, false);
            //locationDAO.updateSolvedStatus(2, true);
            //locationDAO.deleteLocation(4);
            //locationDAO.deleteAll();



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}