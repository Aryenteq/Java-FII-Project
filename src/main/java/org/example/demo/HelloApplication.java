package org.example.demo;

import Database.LocationDAO;
import GA.Parameters;
import GA.VehicleRouting;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
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