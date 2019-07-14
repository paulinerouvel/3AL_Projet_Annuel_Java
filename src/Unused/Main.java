import fr.wastemart.maven.javaclient.models.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fr.wastemart.maven.javaclient/views/Login.fxml"));

        primaryStage.setTitle("WasteMart 0.2");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Bienvenue sur WasteMart");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userNameLabel = new Label("Identifiant");
        grid.add(userNameLabel, 0, 1);

        TextField userNameTextField = new TextField();
        grid.add(userNameTextField, 1, 1);

        Label passwordLabel = new Label("Mot de passe");
        grid.add(passwordLabel, 0, 2);

        PasswordField passwordPWField = new PasswordField();
        grid.add(passwordPWField, 1, 2);

        Button connectBtn = new Button("Se connecter");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(connectBtn);
        grid.add(hbBtn, 1, 4);
        ImageView loadingGif = new ImageView();
        hbBtn.getChildren().add(loadingGif);


        User newUser;
        newUser = new User(1, null, "Jean","Ri","jean.ri@email.com", "0000000000",
                "adresse", "ville", 000000, "Rjean", "Rjean12", null,
                null, 0, "Particulier", "7584994495", LocalDate.of(2019, 4,
                25), 10);

        primaryStage.setMaximized(true); // met en plein écran au lancement

        connectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                userNameTextField.setDisable(true);
                passwordPWField.setDisable(true);
                //loadingGif.setImage(new Image(this.getClass().getResource("loading.gif").toExternalForm()));
            }
        });

        Scene scene = new Scene(grid, primaryStage.getMaxWidth(), primaryStage.getMaxHeight() ); // crée la scène

        primaryStage.setScene(scene);
        primaryStage.show();


    }



    public static void main(String[] args) {
        launch(args);
    }
}
