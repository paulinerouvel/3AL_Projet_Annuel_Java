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


    public void displayProsSuggestion() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_PROS_SUGGESTIONS"), UserInstance.getInstance());
    }

    public void displayPrivatesSuggestion() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_PRIVATES_SUGGESTIONS"), UserInstance.getInstance());
    }

    public void displayProducts() {}

    public void displayUsers() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_USERS"), UserInstance.getInstance());

    }

    public void displayRegisterRequests() {
        //stageManager.loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/GlobalRootLayout.fxml","/views/UserRegistrationList.fxml",
        //        instance);
    }

    public void displayWarehouses() {}

    public void displayCommandList() {}

    public void displaySelfUserInfos() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_USER_INFOS"), UserInstance.getInstance());
    }

    public void displayPlugins() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_PLUGINS"), UserInstance.getInstance());
    }
}
