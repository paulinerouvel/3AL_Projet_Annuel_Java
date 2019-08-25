package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProMainController extends GenericController {
    @FXML private Label proName;

    @Override
    public void init() throws Exception {
        proName.setText(UserInstance.getInstance().getUser().getNom()+" "+UserInstance.getInstance().getUser().getPrenom());
    }

    @Override
    public void initFail() {
        proName.setText("<Error>");
        setInfoText("An error occurred, please disconnect");
    }

    public void displayProducts() {
        StageManager.getInstance().loadPage(dotenv.get("PROFESSIONAL_LIST_PRODUCTS"),
                UserInstance.getInstance());
    }

    public void displayAdminContact() {}

    public void displaySelfUserInfos() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_USER_INFOS"),
                UserInstance.getInstance());
    }

    public void displayPlugins() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_PLUGINS"),
                UserInstance.getInstance());
    }

    public void displayListesProduits() {

    }
}
