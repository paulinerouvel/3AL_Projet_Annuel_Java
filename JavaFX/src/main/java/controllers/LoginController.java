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
import models.User;
import services.Authentication;

import java.io.IOException;

public class LoginController {

    private UserInstance instance;

    private Authentication authentifier = new Authentication();

    private BorderPane rootLayout;

    @FXML public TextField login;
    @FXML public PasswordField password;
    @FXML public Label connectionStatus;
    @FXML public TextArea infoText;

    /*
    public void loadScene(){
        // loading code
        FXMLLoader fxmlLoader = new FXMLLoader();
        AnchorPane root = (AnchorPane) fxmlLoader.load(getClass().getResource("views/Login.fxml"));
        controllers.LoginController myController = (controllers.LoginController) fxmlLoader.getController();
        this.setScene(scene);
    }
    */

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

    public void authenticate(ActionEvent actionEvent) throws Exception {

        String token = null;
        connectionStatus.setText("Trying to connect...");

        try {


            setInstance(new UserInstance());


            token = this.authentifier.login(
                    login.getText(), password.getText()
            );

            this.instance.setToken(token);

            if(this.instance.tokenIsValid()){

                infoText.setText("You are connected !");

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/Main.fxml"));
                rootLayout = loader.load();
                Scene scene = new Scene(rootLayout);

                Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                stageNodeRoot.setScene(scene);
                stageNodeRoot.show();
            }
            else {
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
