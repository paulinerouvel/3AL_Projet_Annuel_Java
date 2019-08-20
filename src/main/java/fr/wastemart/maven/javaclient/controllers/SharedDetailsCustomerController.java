package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.User;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONArray;

import static fr.wastemart.maven.javaclient.services.User.*;

public class SharedDetailsCustomerController extends GenericController {
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

    private JSONArray users;
    private Integer idUser;

    public void init(Integer idUser) throws Exception {
        setidUser(idUser);
        User userFound = jsonToUser(fetchUser("id", idUser.toString()));
        customerName.setText(userFound.getNom());
        customerEmail.setText(userFound.getMail());
        customerNumber.setText(userFound.getTel());
        customerAddress.setText(userFound.getAdresse());
        customerCity.setText(userFound.getVille());
        customerPostalCode.setText(userFound.getCodePostal().toString());
    }

    public void save(ActionEvent actionEvent) {
        //TODO Contrôle sur les modifs ici ou dans l'api
        try {
            User userModified = jsonToUser(fetchUser("id", idUser.toString()));

            userModified.setAdresse(customerAddress.getText() == null ? null : customerAddress.getText());
            userModified.setMail(customerEmail.getText() == null ? null : customerEmail.getText());
            userModified.setTel(customerNumber.getText() == null ? null : customerNumber.getText());
            userModified.setCodePostal(customerPostalCode.getText() == null ? null : Integer.valueOf(customerPostalCode.getText()));
            userModified.setVille(customerCity.getText() == null ? null : customerCity.getText());
            
            if(updateUser(userModified) < 299) {
                setInfoText("Modification réussie");
            }
            else {
                setInfoText("Modification échouée");
            }

        } catch (Exception e) {
            //Logger.reportError(e);
            setInfoErrorOccurred();
        }
    }
    // Return button
    public void displayMainPage(ActionEvent actionEvent) {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance(), actionEvent);
    }

    public void setidUser(Integer idUser) {
        this.idUser = idUser;
    }
}