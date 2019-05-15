package controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class RootLayoutController {

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
}