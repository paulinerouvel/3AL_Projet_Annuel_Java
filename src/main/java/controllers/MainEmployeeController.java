package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.User;
import services.UserInstance;

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
        stageManager.loadPage(actionEvent, "/views/RootLayout.fxml","/views/ProSuggestionList.fxml",
                instance);

    }

    public void displayConsumerSuggestion(ActionEvent actionEvent) throws Exception {
        stageManager.loadPage(actionEvent, "/views/RootLayout.fxml","/views/ConsSuggestionList.fxml",
                instance);

    }

    public void displayWarehouseList(ActionEvent actionEvent) throws Exception {
        stageManager.loadPage(actionEvent, "/views/RootLayout.fxml","/views/WarehouseList.fxml",
                instance);

    }
    public void displayOrderList(ActionEvent actionEvent) throws Exception {
        stageManager.loadPage(actionEvent, "/views/RootLayout.fxml","/views/OrderList.fxml",
                instance);

    }

    public void displayUserList(ActionEvent actionEvent) throws Exception {
        stageManager.loadPage(actionEvent, "/views/RootLayout.fxml","/views/UserList.fxml",
                instance);

    }

    public void displayRegisterRequests(ActionEvent actionEvent) throws Exception {}

    public void displaySelfUserInfos(ActionEvent actionEvent) throws Exception {
        stageManager.loadPage(actionEvent, "/views/RootLayout.fxml","/views/UserInfo.fxml",
                instance);
    }

    public void displayCustomerDetail(ActionEvent actionEvent, Integer userID) throws Exception {
        stageManager.loadPage(actionEvent, "/views/RootLayout.fxml","/views/CustomerDetail.fxml",
                instance);
    }

    public void displayPlugins(ActionEvent actionEvent) throws Exception {
        stageManager.loadPage(actionEvent, "/views/RootLayout.fxml","/views/Plugins.fxml",
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