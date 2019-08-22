package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.User;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static fr.wastemart.maven.javaclient.services.User.updateUser;

public class SharedUserInfosController extends GenericController {
    @FXML
    private Label employeeName;
    @FXML
    private TextField employeeEmail;
    @FXML
    private TextField employeePWD;
    @FXML
    private TextField employeeAddress;
    @FXML
    private TextField employeeCity;
    @FXML
    private TextField employeePostalCode;
    @FXML
    private TextField employeeNumber;
    @FXML
    private PasswordField pwField;

    public void init() throws Exception {
        employeeName.setText(UserInstance.getInstance().getUser().getNom());
        employeeAddress.setText(UserInstance.getInstance().getUser().getAdresse());
        employeeCity.setText(UserInstance.getInstance().getUser().getVille());
        employeeEmail.setText(UserInstance.getInstance().getUser().getMail());
        employeeNumber.setText(UserInstance.getInstance().getUser().getTel());
        employeePostalCode.setText(UserInstance.getInstance().getUser().getCodePostal().toString());
    }

    public void save(ActionEvent actionEvent) {
        //TODO Contrôle sur les modifs ici ou dans l'api
        try {
            User user = UserInstance.getInstance().getUser();
            String pwd = pwField.getText();
            user.setVille(employeeCity.getText() == null ? null : employeeCity.getText());
            user.setTel(employeeNumber.getText() == null ? null : employeeNumber.getText());
            //user.setMdp(employeePWD.getText() == null ? null : employeePWD.getText());
            //System.out.println(" pwd set)");
            user.setMail(employeeEmail.getText() == null ? null : employeeEmail.getText());
            user.setCodePostal(employeePostalCode.getText() == null ? null : Integer.valueOf(employeePostalCode.getText()));
            user.setAdresse(employeeAddress.getText() == null ? null : employeeAddress.getText());
            if(pwd != null || pwd != "") {
                user.setMdp(pwd);
            }
            System.out.println("Je vais rentrer dans saveUser()");
            //Appel à l'api + sauvegarde bdd
            if(updateUser(UserInstance.getInstance().getUser()) < 299 && user.getMdp().length() >=2) {
                // FAIRE UN POP UP
                setInfoText("Modification réussie");
            }
            else {
                setInfoText("Modification échouée");
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    // Return button
    public void displayMainPage() {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance());
    }
}






