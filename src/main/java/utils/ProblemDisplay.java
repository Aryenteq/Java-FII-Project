package utils;

import Data.CoordsPair;
import GA.Parameters;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.Objects;

public class ProblemDisplay {
    static List<CoordsPair> coords;
    static AnchorPane drawingScene;

    static double maxX = Double.MIN_VALUE;
    static double maxY = Double.MIN_VALUE;
    static double minX = Double.MAX_VALUE;
    static double minY = Double.MAX_VALUE;

    static double drawingSceneWidth;
    static double drawingSceneHeight;

    static double circleRadius;

    static String fileSeparator = Objects.equals(FileSystems.getDefault().getSeparator(), "\\") ? "\\\\" : File.separator;
    static String resource;

    public static boolean instanceSelected = false;

    public static void drawInstance(Parent root, Stage stage) {
        maxX = Double.MIN_VALUE;
        maxY = Double.MIN_VALUE;
        minX = Double.MAX_VALUE;
        minY = Double.MAX_VALUE;

        AnchorPane anchorPane = (AnchorPane) root;
        drawingScene = (AnchorPane) anchorPane.getChildren().get(1);
        drawingScene.getChildren().clear();
        String pathToSet = SetSelector.selectSet(stage);
        if(pathToSet == null) return;
        resource = pathToSet.split(fileSeparator)[pathToSet.split(fileSeparator).length - 1];
        coords = SetSelector.parse(resource);

        circleRadius = 3;
        drawingSceneWidth = drawingScene.getWidth() - 4 * circleRadius;
        drawingSceneHeight = drawingScene.getHeight() - 4 * circleRadius;

        for (CoordsPair coordsPair : coords) {
            if (minX > coordsPair.getLat()) minX = coordsPair.getLat();
            if (minY > coordsPair.getLng()) minY = coordsPair.getLng();
        }
        for (CoordsPair coordsPair : coords) {
            coordsPair.setLat(coordsPair.getLat() - minX);
            coordsPair.setLng(coordsPair.getLng() - minY);
            if (maxX < coordsPair.getLat()) maxX = coordsPair.getLat();
            if (maxY < coordsPair.getLng()) maxY = coordsPair.getLng();
        }
        for (CoordsPair coordsPair : coords) {
            double x = coordsPair.getLat() / maxX * (drawingSceneWidth - 2 * circleRadius) + 4 * circleRadius;
            double y = coordsPair.getLng() / maxY * (drawingSceneHeight - 2 * circleRadius) + 4 * circleRadius;
            Circle dot = new Circle();
            dot.setCenterX(x);
            dot.setCenterY(y);
            dot.setRadius(circleRadius);
            dot.setFill(Color.web("#ED7F27"));

            drawingScene.getChildren().add(dot);
        }
        instanceSelected = true;
    }

    public static void drawSolution(List<Integer> order) {

        drawingScene.getChildren().removeIf(node -> node instanceof Line);


        for (int i = 0; i < order.size(); i++) {
            CoordsPair first;
            CoordsPair second;
            if (i == 0) {
                first = coords.get(order.getFirst());
                second = coords.get(order.getLast());
            } else {
                first = coords.get(order.get(i - 1));
                second = coords.get(order.get(i));
            }

            double c1X = first.getLat() / maxX * (drawingSceneWidth - 2 * circleRadius) + 4 * circleRadius;
            double c1Y = first.getLng() / maxY * (drawingSceneHeight - 2 * circleRadius) + 4 * circleRadius;
            double c2X = second.getLat() / maxX * (drawingSceneWidth - 2 * circleRadius) + 4 * circleRadius;
            double c2Y = second.getLng() / maxY * (drawingSceneHeight - 2 * circleRadius) + 4 * circleRadius;

            Line line = new Line(c1X, c1Y, c2X, c2Y);
            line.setStrokeWidth(2);

            line.setStroke(Color.web("#ECA72C"));

            drawingScene.getChildren().add(line);
        }

    }

    public static void reset() {
        drawingScene.getChildren().clear();
    }
}
