package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import services.Authentication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import javafx.concurrent.Worker;

public class LoginController {
    private Scene scene;

    public void setScene(Scene scene) { this.scene = scene; }

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
        LoginController myController = (LoginController) fxmlLoader.getController();
        this.setScene(scene);
    }
    */

    public void authenticate(ActionEvent actionEvent){

        connectionStatus.setText("Trying to connect...");
        Authentication Authentifier = new Authentication();
        try {
            String connResult = Authentifier.whenPostJsonUsingHttpClient_thenCorrect(login.getText(), password.getText());
            infoText.setText(connResult);
            if(connResult.equals("400")){
                connectionStatus.setText("Identifiant ou mot de passe incorrect.");
            } else {
                connectionStatus.setText(connResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private BorderPane root;

    public static BorderPane rootP;


/*
    private void doAuthenticate() {
        final Cursor oldCursor = scene.getCursor();
        scene.setCursor(Cursor.WAIT);
        calculateButton.setDisable(true);
        final Service<Void> calculateService = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        final int maxIterations = 1000000;
                        for (int iterations = 0; iterations < maxIterations; iterations ++) {
                            System.out.println(iterations);
                        }
                        return null;
                    }
                };
            }
        };
        calculateService.stateProperty().addListener(new ChangeListener<Worker.State>() {

            @Override
            public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State oldValue, Worker.State newValue) {
                switch (newValue) {
                    case FAILED:
                    case CANCELLED:
                    case SUCCEEDED:
                        scene.setCursor(oldCursor);
                        calculateButton.setDisable(false);
                        break;
                }
            }
        });
        calculateService.start();
    }
*/
























/*
    @Override
    public void initialize(URL url, ResourceBundle rb){
        rootP = root;
    }
*/
    /*private void loadSplashScreen() {
        try {
            //Load splash screen view FXML
            StackPane pane = FXMLLoader.load(getClass().getResource(("myAwesomeSplashDesign.fxml")));
            //Add it to root container (Can be StackPane, AnchorPane etc)
            root.getChildren().setAll(pane);

            //Load splash screen with fade in effect
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            //Finish splash with fade out effect
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), pane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            fadeIn.play();

            //After fade in, start fade out
            fadeIn.setOnFinished((e) -> {
                fadeOut.play();
            });

            //After fade out, load actual content
            fadeOut.setOnFinished((e) -> {
                try {
                    AnchorPane parentContent = FXMLLoader.load(getClass().getResource(("/main.fxml")));
                    root.getChildren().setAll(parentContent);
                } catch (IOException a) {
                    a.printStackTrace();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}
