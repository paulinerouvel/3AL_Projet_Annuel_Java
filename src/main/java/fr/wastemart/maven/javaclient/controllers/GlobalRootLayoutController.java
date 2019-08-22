package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;

public class GlobalRootLayoutController extends GenericController {
    @FXML public MenuBar menuBar;

    @FXML
    public void closeApplication(ActionEvent event){
        Platform.exit();
    }

    @FXML
    public void showAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos...");
        alert.setHeaderText("Client Java WasteMart v1.3");
        alert.setContentText("(c) Copyright 2019");
        alert.showAndWait();
    }

    public void displayMain() {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance());
    }

    public void disconnect() {
        StageManager.getInstance().loadLoginPage(UserInstance.getInstance(), menuBar);
    }
}