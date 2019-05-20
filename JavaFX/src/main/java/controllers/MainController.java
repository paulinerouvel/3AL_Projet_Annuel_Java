package controllers;

import javafx.fxml.FXML;
import services.UserInstance;

public class MainController {
    private UserInstance instance;
    @FXML private LoginController loginController;
    @FXML private ProductListController productListController;
    @FXML private void initialize(){

    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }
}
