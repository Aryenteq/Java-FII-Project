package Controllers;

import Data.MapRoutes;
import Stages.StageResources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadingRoutesController {
    public ProgressBar progressBar;
    public Button doneButton;
    MapRoutes routes = new MapRoutes();
    private double total;
    private double currentProgress = 0;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void updateProgress() {
        currentProgress += 1;
        progressBar.setProgress(currentProgress / total);
    }

    @FXML
    public void initialize() {
        total = MapRoutes.getMaxConnections();
        progressBar.progressProperty().bind(routes.progressProperty());
        new Thread(routes).start();
        progressBar.progressProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() >= 1.0) {

                doneButton.setDisable(false);
                doneButton.setOpacity(1.0);
            }
        });

    }

    public void switchToMapVisualizer(ActionEvent event) throws IOException {
        root = FXMLLoader.load(StageResources.getResource("MapVisualizationStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
