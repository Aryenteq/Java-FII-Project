package Controllers;

import Stages.StageResources;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeStageController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToModeSelector(ActionEvent event) throws IOException {
        root = FXMLLoader.load(StageResources.getResource("ModeSelectorStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onClickExit() {
        System.exit(0);
    }

    public void switchToLoad(ActionEvent event) throws IOException {
        root = FXMLLoader.load(StageResources.getResource("LoadAddresses.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
