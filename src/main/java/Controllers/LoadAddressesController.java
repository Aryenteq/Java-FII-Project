package Controllers;

import Data.CoordsPair;
import Database.LocationDAO;
import Stages.StageResources;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.sql.SQLException;

public class LoadAddressesController {
    public AnchorPane anchorPane;
    @FXML
    TextArea addressField;
    @FXML
    ComboBox selectType;

    @FXML
    WebView webView;
    WebEngine webEngine;
    String address;
    double lat;
    double lng;
    private final MapController controller = new MapController(this);
    private Stage stage;
    private Scene scene;
    private Parent root;

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
        webEngine.load(StageResources.getResource("Maps/index.html").toExternalForm());

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaApp", controller);
            }
        });
    }

    public void switchToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(StageResources.getResource("HomeScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setTextAreaValue(String text, double lat, double lng) {
        this.address = text;
        this.lat = lat;
        this.lng = lng;
        addressField.setWrapText(true);
        addressField.setText(address);
    }

    public void addAddress(ActionEvent event) throws SQLException {
        LocationDAO locationDB = new LocationDAO();
        address = address.replaceAll("\n", " ");
        if (address != null && !address.isEmpty()) locationDB.addLocation(address, lat, lng, false);
    }

    public void tryGetCoords(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            CoordsPair coords = null;
            try {
                coords = controller.getAddress(addressField.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
            webEngine.executeScript("moveTo(" + coords.getLat() + "," + coords.getLng() + ")");
            address = addressField.getText().substring(0, addressField.getText().length() - 1);
            lat = coords.getLat();
            lng = coords.getLng();
        }
    }
}
