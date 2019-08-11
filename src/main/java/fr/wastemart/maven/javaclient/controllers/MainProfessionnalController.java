package fr.wastemart.maven.javaclient.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import fr.wastemart.maven.javaclient.services.UserInstance;

import java.net.URL;
import java.util.ResourceBundle;

public class MainProfessionnalController extends GenericController {

    private StageManager stageManager;
    @FXML private UserInstance instance;
    @FXML private Label proName;

    public void init(UserInstance instance) {
        try {
            setInstance(instance);
            proName.setText(this.instance.getUser().getNom());
        } catch (Exception e) {
            proName.setText("<Error, please disconnect>");
        }
    }

    public void displayProducts(ActionEvent actionEvent) throws Exception {
        StageManager.loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", "/fr.wastemart.maven.javaclient/views/ProductList.fxml",
                instance);
    }

    public void displayAdminContact(ActionEvent actionEvent) throws Exception {}

    public void displaySelfUserInfos(ActionEvent actionEvent) throws Exception {
        StageManager.loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", "/fr.wastemart.maven.javaclient/views/UserInfo.fxml",
                instance);
    }

    public void displayPlugins(ActionEvent actionEvent) throws Exception {
        StageManager.loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", "/fr.wastemart.maven.javaclient/views/Plugins.fxml",
                instance);
    }

    public void displayListesProduits(ActionEvent actionEvent) throws Exception {

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