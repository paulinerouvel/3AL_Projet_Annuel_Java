package fr.wastemart.maven.javaclient.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import fr.wastemart.maven.javaclient.models.User;
import fr.wastemart.maven.javaclient.services.UserInstance;


public class UserInfoController extends GenericController {
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
    private Label info;
    @FXML
    private PasswordField pwField;

    private UserInstance instance;

    public void init(UserInstance instance) {


        try {
            setInstance(instance);
            employeeName.setText(instance.getUser().getNom());
            employeeAddress.setText(instance.getUser().getAdresse());
            employeeCity.setText(instance.getUser().getVille());
            employeeEmail.setText(instance.getUser().getMail());
            employeeNumber.setText(instance.getUser().getTel());
            employeePostalCode.setText(instance.getUser().getCodePostal().toString());
        } catch (Exception e) {
            employeeName.setText("<Error, please disconnect>");
        }
    }

    public void save(ActionEvent actionEvent) {
        //TODO Contrôle sur les modifs ici ou dans l'api
        try {
            User user = instance.getUser();
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
            if(fr.wastemart.maven.javaclient.services.User.updateUser(instance.getUser()) < 299 && user.getMdp().length() >=2) {
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
    public void displayMainPage(ActionEvent actionEvent) {
        StageManager.getInstance().displayMainPage(instance, actionEvent);
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }
}






