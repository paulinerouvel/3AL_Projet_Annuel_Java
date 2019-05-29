import Annotations.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("WasteMart");
        GridPane gridLog = new GridPane();
        gridLog.setAlignment(Pos.CENTER);
        gridLog.setHgap(10);
        gridLog.setVgap(10);
        gridLog.setPadding(new Insets(25, 25, 25, 25));
        Text scenetitle = new Text("Bienvenue sur WasteMart");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridLog.add(scenetitle, 0, 0, 2, 1);
        Label userName = new Label("Login");
        gridLog.add(userName, 0, 1);
        TextField userTextField = new TextField();
        gridLog.add(userTextField, 1, 1);
        Label pw = new Label("Mot de passe");
        gridLog.add(pw, 0, 2);
        PasswordField pwBox = new PasswordField();
        gridLog.add(pwBox, 1, 2);
        Button btn = new Button("Se connecter");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        gridLog.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        gridLog.add(actiontarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
               // actiontarget.setText("Erreur : login ou mot de passe introuvable");

                Label modalityLabel = new Label("Bonjour tout le monde...!");
                Button closeButton = new Button("Se déconnecter");
                VBox root = new VBox();
                root.getChildren().addAll(modalityLabel, closeButton);
                Scene scene = new Scene(root, primaryStage.getMaxWidth(), primaryStage.getMaxHeight());
                primaryStage.setScene(scene);
                primaryStage.setMaximized(true);
            }
        });

        primaryStage.setMaximized(true); // met en plein écran au lancement
        Scene logScene = new Scene(gridLog, primaryStage.getMaxWidth(), primaryStage.getMaxHeight() ); // crée la scène
        primaryStage.setScene(logScene);
        primaryStage.show();
    }

    // sample ouverture d'une nouvelle fenêtre
    private void showDialog() {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Label modalityLabel = new Label("Bonjour tout le monde...!");
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> stage.close());
        VBox root = new VBox();
        root.getChildren().addAll(modalityLabel, closeButton);
        Scene scene = new Scene(root, 200, 100);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
