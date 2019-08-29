package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class ProMainController extends GenericController {
    @FXML private Label proName;

    @Override
    public void init() throws Exception {
        proName.setText(UserInstance.getInstance().getUser().getNom()+" "+UserInstance.getInstance().getUser().getPrenom());
    }

    @Override
    public void initFail() {
        proName.setText("<Error>");
        setInfoText("An error occurred, please disconnect");
    }

    public void displayProductLists() {
        List<Detail> option = new ArrayList<>();
        option.add(new StringDetail("me"));
        StageManager.getInstance().loadPage(dotenv.get("PRO_LIST_PRODUCT_LISTS"), UserInstance.getInstance(), option);
    }

    public void displayContactAdmin() {
        StageManager.getInstance().loadExtraPageWithDetails(dotenv.get("SHARED_DETAILS_CONTACT_ADMIN"), null);
    }


    public void displaySelfUserInfos() {
        StageManager.getInstance().loadPage(dotenv.get("GLOBAL_USER_INFOS"), UserInstance.getInstance());
    }

    public void displayPlugins() {
        StageManager.getInstance().loadPage(dotenv.get("GLOBAL_PLUGINS"), UserInstance.getInstance());
    }
}
