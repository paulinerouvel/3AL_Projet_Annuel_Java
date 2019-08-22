package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfessionalMainController extends GenericController {
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

    public void displayProducts(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage("/fr.wastemart.maven.javaclient/views/ProfessionalListProducts.fxml",
                UserInstance.getInstance());
    }

    public void displayAdminContact(ActionEvent actionEvent) {}

    public void displaySelfUserInfos(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage("/fr.wastemart.maven.javaclient/views/SharedUserInfos.fxml",
                UserInstance.getInstance());
    }

    public void displayPlugins(ActionEvent actionEvent) {
        StageManager.getInstance().loadPage("/fr.wastemart.maven.javaclient/views/SharedListPlugins.fxml",
                UserInstance.getInstance());
    }

    public void displayListesProduits(ActionEvent actionEvent) {

    }
}
