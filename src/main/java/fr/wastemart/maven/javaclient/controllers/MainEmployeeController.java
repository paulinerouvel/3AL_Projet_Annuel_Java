package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainEmployeeController extends GenericController {
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
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/ProSuggestionList.fxml",
                instance);
    }

    public void displayConsumerSuggestion(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/ConsSuggestionList.fxml",
                instance);
    }

    public void displayWarehouseList(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/WarehouseList.fxml",
                instance);
    }

    public void displayOrderList(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/OrderList.fxml",
                instance);
    }

    public void displayUserList(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/UserList.fxml",
                instance);
    }

    public void displayCustomerDetail(ActionEvent actionEvent, Integer userID) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/CustomerDetail.fxml",
                instance);
    }

    public void displaySelfUserInfos(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/UserInfo.fxml",
                instance);
    }

    public void displayPlugins(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/Plugins.fxml",
                instance);
    }
}
