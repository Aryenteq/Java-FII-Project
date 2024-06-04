package utils;

import GA.Parameters;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClassicalListener implements Runnable {
    Parent root;
    Stage stage;
    Scene scene;

    public ClassicalListener(Parent root, Stage stage, Scene scene) {
        this.root = root;
        this.stage = stage;
        this.scene = scene;
    }

    @Override
    public void run() {
        while (true) {
            if (Parameters.isBestPathChanged()) {
                Parameters.setBestPathChanged(false);
                ProblemDisplay.drawInstance(root, stage);
                //ProblemDisplay.drawSolution(Parameters.getBestPath());
                System.out.println(Parameters.isBestPathChanged());
            }

        }
    }
}
