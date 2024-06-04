package Controllers;

import GA.Parameters;
import Stages.StageResources;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.GAThread;
import utils.ProblemDisplay;

import java.io.IOException;

public class ClassicalVisualizationController {
    public Label generationCounter;
    public Label bestPath;
    public Label currentbestPath;
    private Stage stage;
    private Scene scene;
    private Parent root;

    Thread gameThread;

    @FXML
    public void initialize() {
        generationCounter.setWrapText(true);
        bestPath.setWrapText(true);
        currentbestPath.setWrapText(true);
    }

    public void runClassic(ActionEvent event) {
        if (ProblemDisplay.instanceSelected) {
            Parameters.setUseDatabase(false);
            Parameters.reset();
            GAThread ga = new GAThread();
            Thread gameThread = new Thread(ga);
            gameThread.start();
            new Thread(() -> {
                while (!Parameters.isDone()) {
                    if(Parameters.isBestPathChanged())
                    {
                        Platform.runLater(() -> ProblemDisplay.drawSolution(Parameters.getBestPath()));
                        Parameters.setBestPathChanged(false);
                    }
                    String generations = String.valueOf(Parameters.getCurrentGeneration() + 1);
                    String best = String.valueOf(Parameters.getBestPathLength()).substring(0, Math.min(12, String.valueOf(Parameters.getBestPathLength()).length()));
                    String current = String.valueOf(Parameters.getAvgPathLength()).substring(0, Math.min(12, String.valueOf(Parameters.getAvgPathLength()).length()));
                    Platform.runLater(() -> generationCounter.setText(generations));
                    Platform.runLater(() -> bestPath.setText(best));
                    Platform.runLater(() -> currentbestPath.setText(current));
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Platform.runLater(() -> ProblemDisplay.drawSolution(Parameters.getBestPath()));
            }).start();
        }

    }

    public void loadInstance(ActionEvent event) throws IOException {
        if(gameThread != null)
            gameThread.interrupt();
        ProblemDisplay.instanceSelected = false;
        root = FXMLLoader.load(StageResources.getResource("ClassicVisualizationStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ProblemDisplay.drawInstance(root, stage);
    }

    public void switchToModeSelector(ActionEvent event) throws IOException {
        if(gameThread != null)
            gameThread.interrupt();
        root = FXMLLoader.load(StageResources.getResource("ModeSelectorStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
