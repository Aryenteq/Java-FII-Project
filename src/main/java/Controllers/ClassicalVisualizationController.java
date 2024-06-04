package Controllers;

import Stages.StageResources;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.ClassicalListener;
import utils.GAThread;
import utils.ProblemDisplay;

import java.io.IOException;

public class ClassicalVisualizationController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void runClassic(ActionEvent event) {
        GAThread ga = new GAThread();
        Thread gameThread = new Thread(ga);
        gameThread.start();
        ClassicalListener listener = new ClassicalListener(root, stage, scene);
        Thread lsnrThread = new Thread(listener);
        lsnrThread.start();
    }

    public void loadInstance(ActionEvent event) throws IOException {
        root = FXMLLoader.load(StageResources.getResource("ClassicVisualizationStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ProblemDisplay.drawInstance(root, stage);
    }

    public void switchToModeSelector(ActionEvent event) throws IOException {
        root = FXMLLoader.load(StageResources.getResource("ModeSelectorStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
