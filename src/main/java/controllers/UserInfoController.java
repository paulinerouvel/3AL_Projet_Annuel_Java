package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import services.UserInstance;

import java.time.LocalDate;


public class UserInfoController {
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

    private StageManager stageManager;
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


    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }


    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;

    }

    public void save(ActionEvent actionEvent) {
        //TODO Contrôle sur les modifs ici ou dans l'api
        try {
            User user = instance.getUser();

            user.setVille(employeeCity.getText() == null ? null : employeeCity.getText());
            System.out.println("city set)");
            user.setTel(employeeNumber.getText() == null ? null : employeeNumber.getText());
            System.out.println("number set)");
            //user.setMdp(employeePWD.getText() == null ? null : employeePWD.getText());
            //System.out.println(" pwd set)");
            user.setMail(employeeEmail.getText() == null ? null : employeeEmail.getText());
            System.out.println("mail set)");
            user.setCodePostal(employeePostalCode.getText() == null ? null : Integer.valueOf(employeePostalCode.getText()));
            System.out.println("CP set)");
            user.setAdresse(employeeAddress.getText() == null ? null : employeeAddress.getText());
            System.out.println("address set)");
            System.out.println("Je vais rentrer dans saveUser()");
            //Appel à l'api + sauvegarde bdd
            if(instance.saveUser(instance.getUser())) {
                // FAIRE UN POP UP
                System.out.println("Modification réussie");
            }
            else {
                System.out.println("Modification échouée");
            }
        }
        catch (Exception ex) {

            System.out.println(ex);


        }
    }
    // Return button
    public void displayMainEmployee(ActionEvent actionEvent) throws Exception {
        stageManager.loadPage(actionEvent,"/views/RootLayout.fxml","/views/MainEmployee.fxml", instance);
    }
}






