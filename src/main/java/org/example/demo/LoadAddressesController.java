package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.IOException;

public class LoadAddressesController {
    public AnchorPane anchorPane;
    @FXML
    TextArea addressField;
    @FXML
    ComboBox selectType;

    @FXML
    WebView webView;
    WebEngine webEngine;

    private MapController controller = new MapController(this);

    @FXML
    public void initialize() {
        ObservableList<String> options = FXCollections.observableArrayList("Select from map", "Select from input");

        selectType.setItems(options);
        selectType.setValue("Select from map");
        selectType.valueProperty().addListener((observable, oldValue, newValue) -> {
            addressField.clear();
            addressField.setEditable(!"Select from map".equals(newValue));
        });
        webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("index.html").toExternalForm());

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaApp", controller);
            }
        });
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logZoom(ActionEvent event) throws IOException {
        System.out.println("zoom");
    }

    public void setTextAreaValue(String text) {
        addressField.setWrapText(true);
        addressField.setText(text);
    }
}
