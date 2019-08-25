package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EmployeeMainController extends GenericController {
    @FXML private Label employeeName;
    @FXML private Button profileButton;

    @Override
    public void init() throws Exception {
        employeeName.setText(UserInstance.getInstance().getUser().getNom()+" "+UserInstance.getInstance().getUser().getPrenom());
    }

    @Override
    public void initFail() {
        employeeName.setText("<Error>");
        setInfoText("An error occurred, please disconnect");
    }

    public void displayOrderList() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_ORDERS"), UserInstance.getInstance());
    }

    public void displayPrivatesSuggestion() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_PRIVATES_SUGGESTIONS"), UserInstance.getInstance());
    }

    public void displayProsSuggestion() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_PROS_SUGGESTIONS"), UserInstance.getInstance());
    }

    public void displayWarehouseList() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_WAREHOUSES"), UserInstance.getInstance());
    }

    public void displayUserList() {
        StageManager.getInstance().loadPage(dotenv.get("EMPLOYEE_LIST_USERS"), UserInstance.getInstance());
    }


    public void displaySelfUserInfos() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_USER_INFOS"), UserInstance.getInstance());
    }

    public void displayPlugins() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_PLUGINS"), UserInstance.getInstance());
    }
}
