package controllers;

//import fr.wastemart.maven.annotationprocessor.annotation.AutoImplement;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import services.UserInstance;

//@AutoImplement(as = "User", builder = true)
public class LoginController {

    private StageManager stageManager;
    private UserInstance userInstance;

    @FXML public TextField login;
    @FXML public PasswordField password;
    @FXML public TextArea connectionStatus;


    public void authenticate(ActionEvent actionEvent) {
        connectionStatus.setText("Trying to connect...");
        userInstance = new UserInstance();
        new Thread(() -> {
            JSONObject token = userInstance.login(login.getText(), password.getText());

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
            userInstance.setToken(token);

            if(userInstance.tokenIsValid()) {
                userInstance.initUser();
                userInstance.setConnected(true);

                StageManager.displayMainPage(userInstance, actionEvent);

            }
            else {
                connectionStatus.setText("Token incorrect. Re-essayez.");
            }
        }
    }

    public void register(ActionEvent actionEvent) {
        stageManager.loadRootlessPage(actionEvent,"/views/Register.fxml");
    }

    public void setUserInstance(UserInstance userInstance) {
        this.userInstance = userInstance;
    }

    public UserInstance getUserInstance() {
        return userInstance;
    }

    public StageManager getStageManager() {
        return stageManager;
    }

    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }
}
