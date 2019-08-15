package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainAdminController extends GenericController {
    @FXML private UserInstance instance;
    @FXML private Label adminName;

    public void init(UserInstance instance) {
        try {
            setInstance(instance);
            adminName.setText(this.instance.getUser().getNom());
        } catch (Exception e) {
            adminName.setText("<Error, please disconnect>");
        }
    }

    public void displayProducts(ActionEvent actionEvent) throws Exception {}

    public void displayUsers(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/UserList.fxml",
                instance);
    }

    public void displayRegisterRequests(ActionEvent actionEvent) throws Exception {
        //stageManager.loadPage(actionEvent, "/views/RootLayout.fxml","/views/UserRegistrationList.fxml",
        //        instance);
    }

    public void displayWarehouses(ActionEvent actionEvent) throws Exception {}

    public void displayCommandList(ActionEvent actionEvent) throws Exception {}

    public void displaySelfUserInfos(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/UserInfo.fxml",
                instance);
    }

    public void displayPlugins(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/Plugins.fxml",
                instance);
    }

    public UserInstance getInstance() {
        return instance;
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }
}
