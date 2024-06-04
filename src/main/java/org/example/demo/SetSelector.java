package org.example.demo;

import Data.CustomGraph;
import Data.Node;
import GA.Parameters;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SetSelector {
    public static String selectSet(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        // fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setInitialDirectory(new File("./src/main/resources/Routes"));

        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        }
        return null;
    }
    public static List<CoordsPair> parse(String path){
        List<CoordsPair> pairs = new ArrayList<>();
        Parameters.setFileName(path);
        CustomGraph decodedNodes = new CustomGraph();
        for (Node node : decodedNodes.getNodeArr()) {
            //System.out.println(new CoordsPair(node.x(), node.y()));
            pairs.add(new CoordsPair(node.x(), node.y()));
        }
        return pairs;
    }
}
