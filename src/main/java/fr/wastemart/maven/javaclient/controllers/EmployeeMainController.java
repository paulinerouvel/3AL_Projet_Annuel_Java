package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmployeeMainController extends GenericController {
    @FXML private Label employeeName;

    public void init(UserInstance instance) {
        System.out.println("Init mainEmployee");
        try {
            setInstance(instance);
            employeeName.setText(this.instance.getUser().getNom());
        } catch (Exception e) {
            employeeName.setText("<Error, please disconnect>");
        }
    }

    public void displayProfessionnalSuggestion(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/EmployeeListProsSuggestions.fxml",
                instance);
    }

    public void displayConsumerSuggestion(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/EmployeeListPrivatesSuggestions.fxml",
                instance);
    }

    public void displayWarehouseList(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedListWarehouses.fxml",
                instance);
    }

    public void displayOrderList(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/AdminListOrders.fxml",
                instance);
    }

    public void displayUserList(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/AdminListUsers.fxml",
                instance);
    }

    public void displayCustomerDetail(ActionEvent actionEvent, Integer userID) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedDetailsCustomer.fxml",
                instance);
    }

    public void displaySelfUserInfos(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedUserInfos.fxml",
                instance);
    }

    public void displayPlugins(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedListPlugins.fxml",
                instance);
    }
}
