package Controllers;

import Data.IntPair;
import Data.MapRoutes;
import Database.LocationDAO;
import Database.LocationStructure;
import GA.Parameters;
import Stages.StageResources;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.GAThread;
import utils.ProblemDisplay;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapVisualizationController {
    public WebView webView;
    public static List<LocationStructure> locations;
    public Label currentGenerations;
    public Label bestPath;
    public Label currentPath;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private WebEngine webEngine;

    public void switchToModeSelector(ActionEvent event) throws IOException {
        Parameters.reset();
        Parameters.setUseDatabase(false);
        root = FXMLLoader.load(StageResources.getResource("ModeSelectorStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void run(ActionEvent event) throws IOException {
        Parameters.reset();
        Parameters.setUseDatabase(true);
        GAThread ga = new GAThread();
        Thread gameThread = new Thread(ga);
        gameThread.start();
        new Thread(() -> {
            while (!Parameters.isDone()) {
                if(Parameters.isBestPathChanged())
                {
                    Platform.runLater(() -> {
                        try {
                            loadRoutes(Parameters.getBestPath());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    Parameters.setBestPathChanged(false);

                }
                String generations = String.valueOf(Parameters.getCurrentGeneration() + 1);
                String best = String.valueOf(Parameters.getBestPathLength()).substring(0, Math.min(12, String.valueOf(Parameters.getBestPathLength()).length()));
                String current = String.valueOf(Parameters.getAvgPathLength()).substring(0, Math.min(12, String.valueOf(Parameters.getAvgPathLength()).length()));
                Platform.runLater(() -> currentGenerations.setText(generations));
                Platform.runLater(() -> bestPath.setText(best));
                Platform.runLater(() -> currentPath.setText(current));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> {
                try {
                    loadRoutes(Parameters.getBestPath());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }).start();
    }

    @FXML
    public void initialize() throws SQLException {
        webEngine = webView.getEngine();
        webEngine.load(StageResources.getResource("Maps/Solver.html").toExternalForm());

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
                    System.out.println("Unreachable location by road");
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
        webEngine.executeScript("removeRoutes()");
        Map<String, String> routes = MapRoutes.getRoutes();
        JSONArray routesArray = new JSONArray();
        for (int i = 0; i < routeIds.size() - 1; i++) {
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
