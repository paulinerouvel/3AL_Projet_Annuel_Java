package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminMainController extends GenericController {
    @FXML private Label adminName;

    @Override
    public void init() throws Exception {
        adminName.setText(UserInstance.getInstance().getUser().getNom()+" "+UserInstance.getInstance().getUser().getPrenom());
    }

    @Override
    public void initFail() {
        adminName.setText("<Error>");
        setInfoText("An error occurred, please disconnect");
    }

    public void displayProducts() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_PRODUCTS"), UserInstance.getInstance());
    }

    public void displayProductLists() {
        StageManager.getInstance().loadPage(dotenv.get("ADMIN_LIST_PRODUCT_LISTS"), UserInstance.getInstance());
    }

    public void displayProSuggestions() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_PROS_SUGGESTIONS"), UserInstance.getInstance());
    }

    public void displayWarehouses() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_WAREHOUSES"), UserInstance.getInstance());
    }

    public void displayUsers() {
        StageManager.getInstance().loadPage(dotenv.get("ADMIN_LIST_USERS"), UserInstance.getInstance());
    }

    public void displayRegisterings() {
        StageManager.getInstance().loadPage(dotenv.get("ADMIN_USER_REGISTRATIONS"), UserInstance.getInstance());
    }

    public void displayOrders() {
        StageManager.getInstance().loadPage(dotenv.get("ADMIN_USER_ORDERS"), UserInstance.getInstance());
    }


    public void displaySelfUserInfos() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_USER_INFOS"), UserInstance.getInstance());
    }

    public void displayPlugins() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_PLUGINS"), UserInstance.getInstance());
    }
}
