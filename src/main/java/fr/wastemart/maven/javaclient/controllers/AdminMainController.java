package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminMainController extends GenericController {
    @FXML private Label info;
    @FXML private Label adminName;

    @Override
    public void init() {
        adminName.setText(UserInstance.getInstance().getUser().getNom());
    }

    @Override
    public void initFail() {
        adminName.setText("<Error>");
        info.setText("An error occurred, please disconnect");
    }

    public void displayProducts(ActionEvent actionEvent) {}

    public void displayUsers(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/AdminListUsers.fxml",
                UserInstance.getInstance());

    }

    public void displayRegisterRequests(ActionEvent actionEvent) {
        //stageManager.loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/GlobalRootLayout.fxml","/views/UserRegistrationList.fxml",
        //        instance);
    }

    public void displayWarehouses(ActionEvent actionEvent) {}

    public void displayCommandList(ActionEvent actionEvent) {}

    public void displaySelfUserInfos(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedUserInfos.fxml",
                UserInstance.getInstance());
    }

    public void displayPlugins(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedListPlugins.fxml",
                UserInstance.getInstance());
    }
}
