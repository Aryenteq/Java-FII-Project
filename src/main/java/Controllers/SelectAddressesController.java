package Controllers;

import Data.LocationListCell;
import Data.MapRoutes;
import Database.LocationDAO;
import Database.LocationStructure;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectAddressesController {
    @FXML
    ListView addressList;
    @FXML
    WebView webView;
    WebEngine webEngine;
    LocationDAO locationDAO;
    List<LocationStructure> locations;
    List<LocationStructure> selectedLocations = new ArrayList<>();
    ObservableList<LocationListCell> locationsObservableList = FXCollections.observableArrayList();
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void initialize() throws SQLException {
        locationDAO = new LocationDAO();
        webEngine = webView.getEngine();
        webEngine.load(StageResources.getResource("Maps/Select.html").toExternalForm());

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaApp", this);
            }
        });

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                try {
                    loadMarkers();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void switchToModeSelector(ActionEvent event) throws IOException {
        root = FXMLLoader.load(StageResources.getResource("ModeSelectorStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void loadMarkers() throws SQLException {
        locations = locationDAO.getUnsolvedLocationsList();
        for (LocationStructure location : locations) {
            String script = STR."addMarkerToMap(\{location.getLatitude()}, \{location.getLongitude()}, '\{location.getName()}', \{location.getId()})";
            webEngine.executeScript(script);
        }
    }

    public void addMarkerToList(int id) {
        LocationStructure tempLocation = null;
        for (LocationStructure location : locations) {
            if (location.getId() == id) {
                tempLocation = location;
                break;
            }
        }
        locationsObservableList.add(new LocationListCell(tempLocation));
        selectedLocations.add(tempLocation);
        addressList.setCellFactory(param -> new ListCell<LocationListCell>() {
            private final Button removeButton = new Button("X");

            @Override
            protected void updateItem(LocationListCell item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    removeButton.getStyleClass().add("removeButton");
                    setText(item.toString());
                    setGraphic(removeButton);

                    removeButton.setOnAction(event -> {
                        try {
                            removeMarkerFromList(item.cellData.getId());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        getListView().getItems().remove(item);
                    });
                }
            }
        });

        addressList.setItems(locationsObservableList);
    }

    private void removeMarkerFromList(int id) throws SQLException {
        locations = locationDAO.getUnsolvedLocationsList();
        for (LocationStructure location : locations) {
            if (location.getId() == id) {
                webEngine.executeScript(STR."addMarkerToMap(\{location.getLatitude()}, \{location.getLongitude()}, '\{location.getName()}', \{location.getId()})");
                selectedLocations.add(location);
                break;
            }
        }
    }

    public void switchToLoadingScreen(ActionEvent event) throws IOException, InterruptedException {
        if (selectedLocations.size() > 2) {
            MapRoutes.loadLocations(selectedLocations);
            root = FXMLLoader.load(StageResources.getResource("LoadingRoutes.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}
