package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import services.UserInstance;

import javax.swing.*;
import java.io.IOException;

public class RootLayoutController {

    private UserInstance instance;
    private AnchorPane rootLayout;
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


    public void disconnect(ActionEvent actionEvent) throws Exception {
        try {
            this.instance.disconnect();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/Login.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);

            Stage stageNodeRoot = (Stage) menuBar.getScene().getWindow();

            stageNodeRoot.setScene(scene);
            stageNodeRoot.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}