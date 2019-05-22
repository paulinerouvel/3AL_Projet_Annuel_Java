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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import services.Authentication;
import services.UserInstance;

import java.io.IOException;

public class LoginController {

    private UserInstance instance;
    private Authentication authentifier = new Authentication();

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

            if(this.instance.tokenIsValid()) {

                Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

                this.instance.setConnected(true);

                // Load root layout from fxml file.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/views/RootLayout.fxml"));
                AnchorPane rootLayout = loader.load();

                // Set user instance of the root layout
                RootLayoutController rootLayoutController = loader.getController();
                rootLayoutController.setInstance(this.instance);

                // Show the scene containing the root layout.
                Scene rootScene = new Scene(rootLayout);
                stageNodeRoot.setScene(rootScene);
                stageNodeRoot.show();

                // Load main from fxml file
                loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/Main.fxml"));
                AnchorPane main = loader.load();

                // Set user instance of the main layout
                MainController mainController = loader.getController();
                mainController.setInstance(this.instance);
                //rootLayoutController.getInstance()

                // Setting main as a child of rootLayout
                rootLayout.getChildren().add(main);

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
