package org.example.demo;

import GA.Parameters;
import GA.VehicleRouting;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomeScene.fxml"));
        Stage tempStage = fxmlLoader.load();
        Scene scene = tempStage.getScene();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //launch();
        GA.Parameters.setGenerations(1000);
        VehicleRouting solver = new VehicleRouting();
        solver.run();
    }
}