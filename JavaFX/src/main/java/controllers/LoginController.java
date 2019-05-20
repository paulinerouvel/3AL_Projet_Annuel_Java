package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import services.Authentication;

import java.io.IOException;

public class LoginController {

    private Authentication Authentifier;

    private BorderPane rootLayout;

    @FXML
    public TextField login;

    @FXML
    public PasswordField password;

    @FXML
    public Label connectionStatus;

    @FXML
    public TextArea infoText;

    /*
    public void loadScene(){
        // loading code
        FXMLLoader fxmlLoader = new FXMLLoader();
        AnchorPane root = (AnchorPane) fxmlLoader.load(getClass().getResource("views/Login.fxml"));
        controllers.LoginController myController = (controllers.LoginController) fxmlLoader.getController();
        this.setScene(scene);
    }
    */
    public void setUserInstance(UserAuthentifier Instance) {
        this.Authentifier = Instance ;
    }
    public void authenticate(ActionEvent actionEvent) throws Exception {

        connectionStatus.setText("Trying to connect...");
        Authentication stageNodeAuthentifier = (Authentication)((Node))
        this.Authentifier = new Authentication();
        try {
            boolean connStatus = Authentifier.login(login.getText(), password.getText());
            if (connStatus) {
                infoText.setText("You are connected !");

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/Main.fxml"));
                rootLayout = loader.load();
                Scene scene = new Scene(rootLayout);

                Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                stageNodeRoot.setScene(scene);
                stageNodeRoot.show();
            }
            if (!connStatus) {
                connectionStatus.setText("Identifiant ou mot de passe incorrect");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}




/*
    @Override
    public void initialize(URL url, ResourceBundle rb){
        rootP = root;
    }

}
*/
