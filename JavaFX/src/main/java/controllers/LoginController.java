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
    private BorderPane parentRootLayout;
    private AnchorPane parentMain;
    private FXMLLoader loader;

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
            } else if(token.getString("error").equals("internal")) {
                connectionStatus.setText("Erreur interne. Veuillez re-essayer plus tard.");
            }
        } else {
            setInstance(new UserInstance(token));
            //getInstance().setToken(token);

            if(instance.tokenIsValid()) {
                instance.initUser();
                instance.setConnected(true);
                loadMainEmployeePage(actionEvent);
            }
            else {
                connectionStatus.setText("Token incorrect. Re-essayez.");
            }
        }
    }
    private void loadMainEmployeePage(ActionEvent actionEvent){
        // Load the Root Layout fxml
        setParentRootLayout(loadBorderPane("/views/RootLayout.fxml"));

        // Set user instance of the Root Layout
        RootLayoutController rootLayoutController = loader.getController();
        rootLayoutController.setInstance(this.instance);

        // Display the Root Layout
        Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        showBorderPane(stageNodeRoot, parentRootLayout);

        // Load the Main fxml
        setParentMain(loadAnchorPane("/views/MainEmployee.fxml"));

        // Init Main Controller
        MainEmployeeController mainEmployeeController = loader.getController();
        mainEmployeeController.init(this.parentRootLayout, this.instance);

        // Display the Main in center of Root Layout
        parentRootLayout.setCenter(parentMain);

    }

    // Load Border Pane from fxml file.
    private BorderPane loadBorderPane(String fxml) {
        FXMLLoader loader = new FXMLLoader();
        BorderPane borderPane = new BorderPane();
        try {
            loader.setLocation(this.getClass().getResource(fxml));
            borderPane = loader.load();
            setLoader(loader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return borderPane;
    }


    private void showBorderPane(Stage stage, Parent rootLayout){
        // Show the scene containing the root layout.
        Scene rootScene = new Scene(rootLayout);
        stage.setScene(rootScene);
        stage.show();

    }

    // Load Border Pane from fxml file.
    private AnchorPane loadAnchorPane(String fxml) {
        FXMLLoader loader = new FXMLLoader();
        AnchorPane anchorPane = new AnchorPane();
        try {
            loader.setLocation(this.getClass().getResource(fxml));
            anchorPane = loader.load();
            setLoader(loader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return anchorPane;
    }


    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

    public UserInstance getInstance() {
        return instance;
    }

    public void setParentRootLayout(BorderPane parentRootLayout) {
        this.parentRootLayout = parentRootLayout;
    }

    public void setParentMain(AnchorPane parentMain) { this.parentMain = parentMain; }

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }

    public Authentication getAuthentifier() {
        return authentifier;
    }

    public void setAuthentifier(Authentication authentifier) {
        this.authentifier = authentifier;
    }
}
