package org.example.demo;

import GA.Parameters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    public ProgressBar progressBar;
    @FXML
    CheckBox reverseElitism;
    @FXML
    CheckBox hyperMutatiion;
    @FXML
    TextField mutationGen;
    @FXML
    TextField hyperMutationGen;
    @FXML
    TextField wisdomGen;
    @FXML
    TextField opt2Gen;
    @FXML
    TextField mutationRate;
    @FXML
    TextField hyperMutationRate;
    @FXML
    TextField crosoverRate;
    @FXML
    TextField elitismRate;
    @FXML
    TextField maxNodes;
    @FXML
    TextField maxPopulation;
    @FXML
    TextField maxGenerations;
    @FXML
    VBox generalSettings;
    @FXML
    VBox operators;
    @FXML
    VBox stagnation;

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

    public void switchToModeSelector(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ModeSelectorStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLoad(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("LoadAddresses.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSelect(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SelectAddresses.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToClassical(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ClassicVisualizationStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void loadInstance(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ClassicVisualizationStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ProblemDisplay.drawInstance(root, stage);
    }

    public void switchToParameters(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("ParametersStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void showGeneralSettings(ActionEvent event) throws IOException {
        operators.disableProperty().set(true);
        operators.setOpacity(0.0);
        stagnation.disableProperty().set(true);
        stagnation.setOpacity(0.0);
        generalSettings.disableProperty().set(false);
        generalSettings.setOpacity(1);

        maxNodes.setText(String.valueOf(Parameters.getNodesPerVehicle()));
        maxPopulation.setText(String.valueOf(Parameters.getPopulationSize()));
        maxGenerations.setText(String.valueOf(Parameters.getGenerations()));
        if (Parameters.isReverseElitism())
            reverseElitism.setSelected(true);
        if (Parameters.isHyperMutation())
            hyperMutatiion.setSelected(true);
    }

    public void showOperatorsSettings(ActionEvent event) throws IOException {
        operators.disableProperty().set(false);
        operators.setOpacity(1);
        stagnation.disableProperty().set(true);
        stagnation.setOpacity(0.0);
        generalSettings.disableProperty().set(true);
        generalSettings.setOpacity(0.0);

        mutationRate.setText(String.valueOf(Parameters.getMutationProbability()));
        hyperMutationRate.setText(String.valueOf(Parameters.getHyperMutationProbability()));
        crosoverRate.setText(String.valueOf(Parameters.getCrossoverProbability()));
        elitismRate.setText(String.valueOf(Parameters.getElitism()));
    }

    public void showStagnationSettings(ActionEvent event) throws IOException {
        operators.disableProperty().set(true);
        operators.setOpacity(0.0);
        stagnation.disableProperty().set(false);
        stagnation.setOpacity(1);
        generalSettings.disableProperty().set(true);
        generalSettings.setOpacity(0.0);

        mutationGen.setText(String.valueOf(Parameters.getMaxStagnationUntilAdaptiveMutation()));
        hyperMutationGen.setText(String.valueOf(Parameters.getMaxStagnationUntilHyperMutation()));
        opt2Gen.setText(String.valueOf(Parameters.getMaxStagnationUntil2Opt()));
        wisdomGen.setText(String.valueOf(Parameters.getMaxStagnationUntilWisdom()));
    }

    public void setParameters(ActionEvent event) throws IOException {
        if (generalSettings.getOpacity() != 0.0) {
            Parameters.setReverseElitism(reverseElitism.isSelected());
            Parameters.setHyperMutation(hyperMutatiion.isSelected());
            Parameters.setGenerations(Integer.parseInt(maxGenerations.getText()) );
            Parameters.setNodesPerVehicle(Integer.parseInt(maxNodes.getText()) );
            Parameters.setPopulationSize(Integer.parseInt(maxPopulation.getText()) );

        }
        if(operators.getOpacity() != 0.0)
        {
            Parameters.setMutationProbability(Double.parseDouble(mutationRate.getText()));
            Parameters.setHyperMutationProbability(Double.parseDouble(hyperMutationRate.getText()));
            Parameters.setCrossoverProbability(Double.parseDouble(crosoverRate.getText()));
            Parameters.setElitism(Integer.parseInt(elitismRate.getText()));
        }
        if(stagnation.getOpacity() != 0.0)
        {
            Parameters.setMaxStagnationUntil2Opt(Integer.parseInt(opt2Gen.getText()));
            Parameters.setMaxStagnationUntilWisdom(Integer.parseInt(wisdomGen.getText()));
            Parameters.setMaxStagnationUntilAdaptiveMutation(Integer.parseInt(elitismRate.getText()));
            Parameters.setMaxStagnationUntilHyperMutation(Integer.parseInt(hyperMutationRate.getText()));
        }
    }



    public void onClickExit() {
        System.exit(0);
    }

}
