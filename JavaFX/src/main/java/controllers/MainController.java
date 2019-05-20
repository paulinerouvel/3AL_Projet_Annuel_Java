package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.UserInstance;

public class MainController {
    private UserInstance instance;
    @FXML private LoginController loginController;
    @FXML private ProductListController productListController;
    @FXML private Label test;

    @FXML private void initialize(){
        test.setText("Initialized. Your token is : "+instance.getToken());
    }
    
    public void init() throws Exception {

    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }
}
