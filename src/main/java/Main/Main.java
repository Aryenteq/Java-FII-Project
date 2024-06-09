package Main;

import Stages.StageResources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch();

        // Comment the line above and uncomment these two lines to run without GUI
//        VehicleRouting solver = new VehicleRouting();
//        solver.run();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(StageResources.getResource("HomeScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}