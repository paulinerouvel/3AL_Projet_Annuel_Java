package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
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

    public void displayProfessionnalSuggestion(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/EmployeeListProsSuggestions.fxml",
                UserInstance.getInstance());
    }

    public void displayConsumerSuggestion(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/EmployeeListPrivatesSuggestions.fxml",
                UserInstance.getInstance());
    }

    public void displayWarehouseList(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedListWarehouses.fxml",
                UserInstance.getInstance());
    }

    public void displayOrderList(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/AdminListOrders.fxml",
                UserInstance.getInstance());
    }

    public void displayUserList(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/AdminListUsers.fxml",
                UserInstance.getInstance());
    }

    public void displayCustomerDetail(ActionEvent actionEvent, Integer userID) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedDetailsCustomer.fxml",
                UserInstance.getInstance());
    }

    public void displaySelfUserInfos(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedUserInfos.fxml",
                UserInstance.getInstance());
    }

    public void displayPlugins(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedListPlugins.fxml",
                UserInstance.getInstance());
    }
}
