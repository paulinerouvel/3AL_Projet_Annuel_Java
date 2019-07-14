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
            users = services.UserInstance.fetchUsersByID(idUser);
            JSONObject userFound = users.getJSONObject(0);
            User user = new User(
                    userFound.getInt("id"),
                    userFound.getString("libelle"),
                    userFound.getInt("userCategory"),
                    userFound.getString("nom"),
                    userFound.getString("prenom"),
                    userFound.getString("mail"),
                    userFound.getString("tel"),
                    userFound.getString("adresse"),
                    userFound.getString("ville"),
                    userFound.getInt("codePostal"),
                    userFound.getString("pseudo"),
                    userFound.getString("mdp"),
                    userFound.getString("photo"),
                    userFound.getString("desc"),
                    userFound.getInt("tailleOrganisme"),
                    userFound.getInt("estValide"),
                    userFound.getString("siret"),
                    ZonedDateTime.parse(userFound.getString("dlc")).toLocalDate(),
                    userFound.getInt("nbPointsSourire")
            );
            customerName.setText(instance.getUser().getNom());
            customerEmail.setText(instance.getUser().getMail());
            customerNumber.setText(instance.getUser().getTel());
            customerAddress.setText(instance.getUser().getAdresse());
            customerCity.setText(instance.getUser().getVille());
            customerPostalCode.setText(instance.getUser().getCodePostal().toString());
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
            User user = instance.getUser();

            //user.setVille(employeeCity.getText() == null ? null : employeeCity.getText());
            //System.out.println("city set)");
            //user.setTel(employeeNumber.getText() == null ? null : employeeNumber.getText());
            //System.out.println("number set)");
            //user.setMdp(employeePWD.getText() == null ? null : employeePWD.getText());
            //System.out.println(" pwd set)");
            //user.setMail(employeeEmail.getText() == null ? null : employeeEmail.getText());
            //System.out.println("mail set)");
            //user.setCodePostal(employeePostalCode.getText() == null ? null : Integer.valueOf(employeePostalCode.getText()));
            //System.out.println("CP set)");
            //user.setAdresse(employeeAddress.getText() == null ? null : employeeAddress.getText());
            System.out.println("address set)");
            System.out.println("Je vais rentrer dans saveUser()");
            //Appel à l'api + sauvegarde bdd
            if(instance.saveUser(instance.getUser(), "sav") < 299) {
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