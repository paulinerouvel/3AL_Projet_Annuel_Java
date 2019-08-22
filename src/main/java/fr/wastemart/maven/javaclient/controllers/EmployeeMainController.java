package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmployeeMainController extends GenericController {
    @FXML private Label employeeName;

    @Override
    public void init() throws Exception {
        employeeName.setText(UserInstance.getInstance().getUser().getNom()+" "+UserInstance.getInstance().getUser().getPrenom());
    }

    @Override
    public void initFail() {
        employeeName.setText("<Error>");
        setInfoText("An error occurred, please disconnect");
    }

    public void displayProfessionnalSuggestion() {
        StageManager.getInstance().loadPage(dotenv.get("EMPLOYEE_LIST_PROS_SUGGESTIONS"), UserInstance.getInstance());
    }

    public void displayConsumerSuggestion() {
        StageManager.getInstance().loadPage(dotenv.get("EMPLOYEE_LIST_PRIVATES_SUGGESTION"), UserInstance.getInstance());
    }

    public void displayWarehouseList() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_WAREHOUSES"), UserInstance.getInstance());
    }

    public void displayOrderList() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_ORDERS"), UserInstance.getInstance());
    }

    public void displayUserList() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_USERS"), UserInstance.getInstance());
    }

    public void displayCustomerDetail(Integer userID) {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_DETAILS_CUSTOMER"), UserInstance.getInstance());
    }

    public void displaySelfUserInfos() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_USER_INFOS"), UserInstance.getInstance());
    }

    public void displayPlugins() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_PLUGINS"), UserInstance.getInstance());
    }
}
