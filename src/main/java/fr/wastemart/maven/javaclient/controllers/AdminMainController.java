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

    public void displayProducts() {}

    public void displayUsers() {
        StageManager.getInstance().loadPage("/fr.wastemart.maven.javaclient/views/AdminListUsers.fxml",
                UserInstance.getInstance());

    }

    public void displayRegisterRequests() {
        //stageManager.loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/GlobalRootLayout.fxml","/views/UserRegistrationList.fxml",
        //        instance);
    }

    public void displayWarehouses() {}

    public void displayCommandList() {}

    public void displaySelfUserInfos() {
        StageManager.getInstance().loadPage("/fr.wastemart.maven.javaclient/views/SharedUserInfos.fxml",
                UserInstance.getInstance());
    }

    public void displayPlugins() {
        StageManager.getInstance().loadPage("/fr.wastemart.maven.javaclient/views/SharedListPlugins.fxml",
                UserInstance.getInstance());
    }
}
