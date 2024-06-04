package org.example.demo;

import Database.LocationDAO;
import Database.LocationStructure;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapVisualizationController {
    public WebView webView;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private WebEngine webEngine;
    List<LocationStructure> locations;

    public void switchToModeSelector(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ModeSelectorStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToParameters(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ParametersStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() throws SQLException {
        webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("Solver.html").toExternalForm());

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaApp", this);
                try {
                    loadMarkers();
                    List<Integer> temp = new ArrayList<>();
                    for (int i = 0; i < locations.size(); i++) {
                        temp.add(i);
                    }
                    loadRoutes(temp);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void loadMarkers() throws SQLException {
        locations = MapRoutes.getLocations();
        for (LocationStructure location : locations) {
            webEngine.executeScript(STR."addMarkerToMap(\{location.getLatitude()}, \{location.getLongitude()}, '\{location.getName()}', \{location.getId()})");
        }
    }

    public void loadRoutes(List<Integer> routeIds) throws SQLException {
        Map<String, String> routes = MapRoutes.getRoutes();
        JSONArray routesArray = new JSONArray();
        for(int i = 0; i < routeIds.size() - 1; i++) {
            String key = new IntPair(routeIds.get(i), routeIds.get(i + 1)).toString();
            String geometry = routes.get(key);
            JSONObject routeJson = new JSONObject();
            routeJson.put("geometry", geometry);
            routesArray.put(routeJson);
        }
        webEngine.executeScript("loadRoutes(" + routesArray + ")");

    }

    public void markAsSolved(ActionEvent event) throws SQLException {
        LocationDAO locationDAO = new LocationDAO();

        for (LocationStructure location : locations) {
            locationDAO.markAsSolved(location.getId());
        }
    }
}
