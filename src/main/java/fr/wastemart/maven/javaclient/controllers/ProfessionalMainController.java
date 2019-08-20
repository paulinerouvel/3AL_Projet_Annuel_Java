package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfessionalMainController extends GenericController {
    @FXML private Label proName;

    public void init() {
        try {
            proName.setText(UserInstance.getInstance().getUser().getNom());
        } catch (Exception e) {
            proName.setText("<Error, please disconnect>");
        }
    }

    public void displayProducts(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/ProfessionalListProducts.fxml",
                UserInstance.getInstance());
    }

    public void displayAdminContact(ActionEvent actionEvent) {}

    public void displaySelfUserInfos(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedUserInfos.fxml",
                UserInstance.getInstance());
    }

    public void displayPlugins(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedListPlugins.fxml",
                UserInstance.getInstance());
    }

    public void displayListesProduits(ActionEvent actionEvent) {

    }
}
