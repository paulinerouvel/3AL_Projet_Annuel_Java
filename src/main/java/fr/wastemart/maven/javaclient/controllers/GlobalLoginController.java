package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import fr.wastemart.maven.javaclient.services.UserInstance;

public class GlobalLoginController extends GenericController {
    private UserInstance userInstance;

    @FXML public TextField login;
    @FXML public PasswordField password;
    @FXML public TextArea connectionStatus;

    public void init(UserInstance instance, String data) {
        connectionStatus.setText(data);
    }

    public void authenticate(ActionEvent actionEvent) {
        connectionStatus.setText("Trying to connect...");
        userInstance = new UserInstance();
        new Thread(() -> {
            //JSONObject token = userInstance.login(login.getText(), password.getText());

            Platform.runLater(() -> {
                StageManager.getInstance().loadPage(actionEvent,
                        "/fr.wastemart.maven.javaclient/views/EmployeeMain.fxml",
                        userInstance);
                //processLoginAttempt(token, actionEvent); // TODO WIP uncomment
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
            userInstance.setToken(token);

            if(userInstance.tokenIsValid()) {
                userInstance.initUser();
                userInstance.setConnected(true);

                StageManager.getInstance().displayMainPage(userInstance, actionEvent);

            }
            else {
                connectionStatus.setText("Token incorrect. Re-essayez.");
            }
        }
    }

    public void displayRegister(ActionEvent actionEvent) {
        StageManager.getInstance().loadRootlessPage(actionEvent, "/fr.wastemart.maven.javaclient/views/GlobalRegister.fxml");
    }
}
