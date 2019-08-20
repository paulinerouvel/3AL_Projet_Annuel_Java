package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import fr.wastemart.maven.javaclient.services.UserInstance;

public class GlobalRootLayoutController extends GenericController {
    @FXML public MenuBar menuBar;

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

    public void displayMain(ActionEvent event) {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance(), event);
    }

    public void disconnect() {
        StageManager.getInstance().loadLoginPageFromMenuBar(UserInstance.getInstance(), menuBar);
    }
}