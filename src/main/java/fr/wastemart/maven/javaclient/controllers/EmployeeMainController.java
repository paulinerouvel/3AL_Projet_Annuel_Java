package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

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

    public void displayProducts() {
        List<Detail> option = new ArrayList<>();
        option.add(new StringDetail("all"));
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_PRODUCTS"), UserInstance.getInstance(), option);
    }

    public void displayProsSuggestions() {
        List<Detail> option = new ArrayList<>();
        option.add(new StringDetail("suggestion"));
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_PROS_SUGGESTIONS"), UserInstance.getInstance(), option);
    }

    public void displayWarehouses() {
        StageManager.getInstance().loadPage(dotenv.get("SHARED_LIST_WAREHOUSES"), UserInstance.getInstance());
    }

    public void displayOrders() {
        StageManager.getInstance().loadPage(dotenv.get("EMPLOYEE_LIST_ORDERS"), UserInstance.getInstance());
    }

    public void displayUsers() {
        StageManager.getInstance().loadPage(dotenv.get("EMPLOYEE_LIST_USERS"), UserInstance.getInstance());
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
