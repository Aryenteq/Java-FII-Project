package Controllers;

import Stages.StageResources;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ModeSelectorController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void switchToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(StageResources.getResource("HomeScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSelect(ActionEvent event) throws IOException {
        root = FXMLLoader.load(StageResources.getResource("SelectAddresses.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToClassical(ActionEvent event) throws IOException {
        root = FXMLLoader.load(StageResources.getResource("ClassicVisualizationStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToParameters(ActionEvent event) throws IOException {

        root = FXMLLoader.load(StageResources.getResource("ParametersStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
