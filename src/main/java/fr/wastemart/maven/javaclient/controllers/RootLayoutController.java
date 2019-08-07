package fr.wastemart.maven.javaclient.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import fr.wastemart.maven.javaclient.services.UserInstance;

public class RootLayoutController extends GenericController {

    private UserInstance instance;
    private StageManager stageManager;
    @FXML public MenuBar menuBar;


    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

    public UserInstance getInstance() {
        return instance;
    }

    @FXML
    public void closeApplication(ActionEvent event){
        Platform.exit();
    }

    @FXML
    public void showAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About...");
        alert.setHeaderText("WasteMart Java Client");
        alert.setContentText("(c) Copyright 2019 - Version 0.2");
        alert.showAndWait();
    }


    public void disconnect() throws Exception {

        StageManager.loadLoginPageFromMenuBar(instance, menuBar);
    }

    public StageManager getStageManager() {
        return stageManager;
    }

    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }
}