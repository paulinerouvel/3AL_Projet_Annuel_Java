package controllers;

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

    public UserInstance getInstance() {
        return instance;
    }

    public void authenticate(ActionEvent actionEvent) throws Exception {

        JSONObject token = null;
        connectionStatus.setText("Trying to connect...");

        try {

            token = this.authentifier.login(login.getText(), password.getText());
            this.instance = new UserInstance(token);
            //getInstance().setToken(token);

            if(instance.tokenIsValid()) {
                instance.initUser();
                instance.setConnected(true);
                loadMainEmployeePage(actionEvent);

            }
            else {
                connectionStatus.setText("Identifiant ou mot de passe incorrect");
            }

        } catch (IOException e) {
            e.printStackTrace();
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
        setParentMain(loadAnchorPane("/views/Main.fxml"));

        // Init Main Controller
        MainController mainController = loader.getController();
        mainController.init(this.parentRootLayout, this.instance);

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



    public void setParentRootLayout(BorderPane parentRootLayout) {
        this.parentRootLayout = parentRootLayout;
    }

    public void setParentMain(AnchorPane parentMain) {
        this.parentMain = parentMain;
    }

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }
}
