package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import models.User;
import services.UserInstance;

import java.net.URL;
import java.util.ResourceBundle;

public class MainEmployeeController implements Initializable {
    @FXML private BorderPane parentRootLayout;
    @FXML private UserInstance instance;
    @FXML private LoginController loginController;
    @FXML private ProductListController productListController;
    @FXML private Label employeeName;

    @FXML @Override
    public void initialize(URL location, ResourceBundle resources){
    }

    public void init(BorderPane rootLayout, UserInstance instance) {


        try {
            setRootLayout(rootLayout);
            setInstance(instance);
            employeeName.setText(this.instance.getUser().getNom());
        } catch (Exception e) {
            employeeName.setText("<Error, please disconnect>");
        }
    }

    public void displaySelfUserInfos(ActionEvent actionEvent) throws Exception {
        UserInfoController userInfoController = new UserInfoController();
        userInfoController.displayUserInfos(actionEvent, instance.getUser());
    }



    public UserInstance getInstance() {
        return instance;
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

    public BorderPane getRootLayout() {
        return parentRootLayout;
    }

    public void setRootLayout(BorderPane rootLayout) {
        this.parentRootLayout = rootLayout;
    }
}
