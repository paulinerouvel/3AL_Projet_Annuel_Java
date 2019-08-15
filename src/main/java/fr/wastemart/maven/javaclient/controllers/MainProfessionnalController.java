package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainProfessionnalController extends GenericController {
    @FXML private UserInstance instance;
    @FXML private Label proName;

    public void init(UserInstance instance) {
        try {
            setInstance(instance);
            proName.setText(this.instance.getUser().getNom());
        } catch (Exception e) {
            proName.setText("<Error, please disconnect>");
        }
    }

    public void displayProducts(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", "/fr.wastemart.maven.javaclient/views/ProductList.fxml",
                instance);
    }

    public void displayAdminContact(ActionEvent actionEvent) throws Exception {}

    public void displaySelfUserInfos(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", "/fr.wastemart.maven.javaclient/views/UserInfo.fxml",
                instance);
    }

    public void displayPlugins(ActionEvent actionEvent) throws Exception {
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/RootLayout.fxml", "/fr.wastemart.maven.javaclient/views/Plugins.fxml",
                instance);
    }

    public void displayListesProduits(ActionEvent actionEvent) throws Exception {

    }

    public UserInstance getInstance() {
        return instance;
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }
}
