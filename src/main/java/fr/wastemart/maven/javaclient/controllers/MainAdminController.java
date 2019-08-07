package fr.wastemart.maven.javaclient.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import fr.wastemart.maven.javaclient.services.UserInstance;

import java.net.URL;
import java.util.ResourceBundle;

public class MainAdminController extends GenericController {

    private StageManager stageManager;
    @FXML private UserInstance instance;
    @FXML private Label adminName;

    public void init(UserInstance instance) {
        try {
            setInstance(instance);
            adminName.setText(this.instance.getUser().getNom());
        } catch (Exception e) {
            adminName.setText("<Error, please disconnect>");
        }
    }
    public void displayProducts(ActionEvent actionEvent) throws Exception {}
    public void displayUsers(ActionEvent actionEvent) throws Exception {
        stageManager.loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", "/fr.wastemart.maven.javaclient/views/UserList.fxml",
                instance);
    }
    public void displayRegisterRequests(ActionEvent actionEvent) throws Exception {
        //stageManager.loadPage(actionEvent, "/views/RootLayout.fxml","/views/UserRegistrationList.fxml",
        //        instance);
    }
    public void displayWarehouses(ActionEvent actionEvent) throws Exception {}
    public void displayCommandList(ActionEvent actionEvent) throws Exception {}
    public void displaySelfUserInfos(ActionEvent actionEvent) throws Exception {
        stageManager.loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", "/fr.wastemart.maven.javaclient/views/UserInfo.fxml",
                instance);
    }

    public void displayPlugins(ActionEvent actionEvent) throws Exception {
        StageManager.loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", "/fr.wastemart.maven.javaclient/views/Plugins.fxml",
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
