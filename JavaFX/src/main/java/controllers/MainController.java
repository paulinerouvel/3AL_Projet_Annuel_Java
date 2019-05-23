package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import services.UserInstance;

import javax.swing.border.Border;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
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
