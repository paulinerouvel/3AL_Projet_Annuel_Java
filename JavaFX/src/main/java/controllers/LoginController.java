package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.json.JSONObject;
import services.Authentication;
import services.UserInstance;

import java.io.IOException;

public class LoginController {
    private StageManager stageManager;

    private UserInstance instance;
    private Authentication authentifier = new Authentication();

    @FXML public TextField login;
    @FXML public PasswordField password;
    @FXML public TextArea connectionStatus;


    public void authenticate(ActionEvent actionEvent) {
        connectionStatus.setText("Trying to connect...");

        new Thread(() -> {
            JSONObject token = getAuthentifier().login(login.getText(), password.getText());
            Platform.runLater(() -> {
                processLoginAttempt(token, actionEvent);
            });
        }).start();
    }

    public void processLoginAttempt(JSONObject token, ActionEvent actionEvent){
        if(token.has("error")){
            if(token.getInt("error") == 503){
                connectionStatus.setText("Timeout");
            } else if(token.getInt("error") == 400){
                connectionStatus.setText("Identifiant ou mot de passe incorrect");
            } else {
                connectionStatus.setText("Erreur interne. Veuillez re-essayer plus tard.");
            }
        } else {
            setInstance(new UserInstance(token));
            //getInstance().setToken(token);

            if(instance.tokenIsValid()) {
                instance.initUser();
                instance.setConnected(true);
                stageManager.loadPage(actionEvent,
                        "/views/RootLayout.fxml",
                        "/views/MainEmployee.fxml",
                        instance);
            }
            else {
                connectionStatus.setText("Token incorrect. Re-essayez.");
            }
        }
    }


    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

    public UserInstance getInstance() {
        return instance;
    }

    public Authentication getAuthentifier() {
        return authentifier;
    }

    public void setAuthentifier(Authentication authentifier) {
        this.authentifier = authentifier;
    }

    public StageManager getStageManager() {
        return stageManager;
    }

    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }
}
