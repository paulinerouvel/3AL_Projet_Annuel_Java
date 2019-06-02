package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import services.Authentication;
import services.UserInstance;

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
            setInstance(new UserInstance(token));
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
