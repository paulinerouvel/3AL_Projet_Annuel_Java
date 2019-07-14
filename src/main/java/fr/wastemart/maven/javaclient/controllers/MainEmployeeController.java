package fr.wastemart.maven.javaclient.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import fr.wastemart.maven.javaclient.services.UserInstance;

import java.net.URL;
import java.util.ResourceBundle;

public class MainEmployeeController implements Initializable {

    private StageManager stageManager;
    @FXML private UserInstance instance;
    @FXML private Label employeeName;

    @FXML @Override
    public void initialize(URL location, ResourceBundle resources){
    }

    public void init(UserInstance instance) {
        try {
            setInstance(instance);
            employeeName.setText(this.instance.getUser().getNom());
        } catch (Exception e) {
            employeeName.setText("<Error, please disconnect>");
        }
    }

    public void displayProfessionnalSuggestion(ActionEvent actionEvent) throws Exception {
        stageManager.loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", "/fr.wastemart.maven.javaclient/views/ProSuggestionList.fxml",
                instance);

    }

    public void displayRegisterRequests(ActionEvent actionEvent) throws Exception {}

    public void displaySelfUserInfos(ActionEvent actionEvent) throws Exception {
        stageManager.loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", "/fr.wastemart.maven.javaclient/views/UserInfo.fxml",
                instance);
    }

    public void displayPlugins(ActionEvent actionEvent) throws Exception {
        stageManager.loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", "/fr.wastemart.maven.javaclient/views/Plugins.fxml",
                instance);
    }

    public UserInstance getInstance() {
        return instance;
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

    public StageManager getStageManager() {
        return stageManager;
    }

    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }
}
