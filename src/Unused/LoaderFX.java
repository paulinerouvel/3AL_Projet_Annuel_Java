package fr.wastemart.maven.javaclient.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

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
