package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import org.json.JSONArray;
import org.json.JSONObject;
import services.UserInstance;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import static java.lang.Integer.parseInt;


public class CustomerDetailController {
    @FXML
    private Label customerName;
    @FXML
    private TextField customerEmail;
    @FXML
    private TextField customerNumber;
    @FXML
    private TextField customerAddress;
    @FXML
    private TextField customerCity;
    @FXML
    private TextField customerPostalCode;
    @FXML

    private Label info;
    private JSONArray users;
    private StageManager stageManager;
    private UserInstance instance;
    private Integer idUser;
    public void init(Integer idUser) {
        try {
            setidUser(idUser);
            services.User userService = new services.User();
            User userFound = userService.getUser(userService.fetchUserById(idUser));
            System.out.println("Je suis bien dans CustomerDetailController");
            customerName.setText(userFound.getNom());
            customerEmail.setText(userFound.getMail());
            customerNumber.setText(userFound.getTel());
            customerAddress.setText(userFound.getAdresse());
            customerCity.setText(userFound.getVille());
            customerPostalCode.setText(userFound.getCodePostal().toString());
        } catch (Exception e) {
            System.out.println("error : " + e);
        }
    }


    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }
    public void setidUser(Integer idUser) {
        this.idUser = idUser;
    }

    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    public void save(ActionEvent actionEvent) {
        //TODO Contrôle sur les modifs ici ou dans l'api
        try {
            services.User userService = new services.User();
            User userModified = userService.getUser(userService.fetchUserById(idUser));



            userModified.setAdresse(customerAddress.getText() == null ? null : customerAddress.getText());
            userModified.setMail(customerEmail.getText() == null ? null : customerEmail.getText());
            userModified.setTel(customerNumber.getText() == null ? null : customerNumber.getText());
            userModified.setCodePostal(customerPostalCode.getText() == null ? null : Integer.valueOf(customerPostalCode.getText()));
            userModified.setVille(customerCity.getText() == null ? null : customerCity.getText());
            
            System.out.println("Je vais rentrer dans saveUser()");
            //Appel à l'api + sauvegarde bdd

            if(userService.saveUser(userModified, "sav") < 299) {
                // FAIRE UN POP UP
                info.setText("Modification réussie");
            }
            else {
                info.setText("Modification échouée");
            }
        }
        catch (Exception ex) {

            System.out.println(ex);


        }
    }
    // Return button
    public void displayMainPage(ActionEvent actionEvent) throws Exception {
        StageManager.displayMainPage(instance, actionEvent);
    }
}