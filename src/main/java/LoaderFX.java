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

public class LoaderFX {


    public GridPane getGridLogged() {

        GridPane gridLogged = new GridPane();
        gridLogged.setAlignment(Pos.CENTER);
        gridLogged.setHgap(10);
        gridLogged.setVgap(10);
        gridLogged.setPadding(new Insets(25, 25, 25, 25));
        Text scenetitle = new Text("Bienvenue sur WasteMart");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridLogged.add(scenetitle, 0, 0, 2, 1);
        Label associations = new Label("Associations");
        gridLogged.add(associations, 0, 1);
        TextField userTextField = new TextField();
        gridLogged.add(userTextField, 1, 1);
        Label entreprises = new Label("Entreprises");
        gridLogged.add(entreprises, 1, 1);
        Button btn = new Button("Se déconnecter");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        gridLogged.add(hbBtn, 1, 3);

        return gridLogged;
    }

}
