package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLLoginController implements Initializable {

    @FXML
    private AnchorPane root;

    public static AnchorPane rootP;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        rootP = root;
    }

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
