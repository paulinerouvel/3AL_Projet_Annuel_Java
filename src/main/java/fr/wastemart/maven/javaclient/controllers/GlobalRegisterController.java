package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.User;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;

import static fr.wastemart.maven.javaclient.services.User.*;

public class GlobalRegisterController extends GenericController {
    private Object[] registerFields;
    private Integer registerFieldsLength = 10;


    @FXML private TextField prenom;
    @FXML private TextField nom;
    @FXML private TextField mail;
    @FXML private TextField tel;
    @FXML private TextField adresse;
    @FXML private TextField ville;
    @FXML private TextField codePostal;
    @FXML private DatePicker dateNaissance;
    @FXML private TextField pseudo;
    @FXML private PasswordField mdp;
    @FXML private ChoiceBox<String> userType;
    @FXML private TextField photo;
    @FXML private Label organismLabel;
    @FXML private TextField libelle;
    @FXML private TextField description;
    @FXML private TextField tailleOrganisme;
    @FXML private TextField siret;


    public void init() {
        userType.setItems(FXCollections.observableArrayList("Employé", "Professionnel", "Admin"));

        registerFields = new Object[16];
        registerFields[0] = prenom;
        registerFields[1] =  nom;
        registerFields[2] = mail;
        registerFields[3] = tel;
        registerFields[4] = adresse;
        registerFields[5] = ville;
        registerFields[6] = codePostal;
        registerFields[7] = dateNaissance;
        registerFields[8] = pseudo;
        registerFields[9] = mdp;
        registerFields[10] = userType;
        registerFields[11] = photo;
        registerFields[12] = libelle;
        registerFields[13] = description;
        registerFields[14] = tailleOrganisme;
        registerFields[15] = siret;


        //clearFields(registerFields);
        ((DatePicker)registerFields[7]).setValue(LocalDate.of(1998,10,13));
        userType.getSelectionModel().selectFirst();

        userType.setTooltip(new Tooltip("Sélectionnez le type d'utilisateur"));
    }

    public void refreshUser() {
        if(userType.getSelectionModel().getSelectedIndex() == 0 || userType.getSelectionModel().getSelectedIndex() == 2){
            organismLabel.setVisible(false);
            libelle.setVisible(false);
            description.setVisible(false);
            tailleOrganisme.setVisible(false);
            siret.setVisible(false);
            registerFieldsLength = 10;
        } else {
            organismLabel.setVisible(true);
            libelle.setVisible(true);
            description.setVisible(true);
            tailleOrganisme.setVisible(true);
            siret.setVisible(true);
            registerFieldsLength = 16;
        }
    }

    public void register() {
        try {
            for (Object registerField : registerFields) {
                ((Control) registerField).setStyle("-fx-background-color: #FFFFFF");
            }

            Integer indexFieldVerif = areTextFieldsValid(registerFields);
            if (indexFieldVerif == -1) {

                Integer userCategorySelected = userType.getSelectionModel().getSelectedIndex();
                Integer userCategory = userCategorySelected == 0 ? 4 :
                        userCategorySelected == 1 ? 2 : 5;

                User user = new User(-1,
                        (userType.getSelectionModel().getSelectedIndex() == 1 && !libelle.getText().isEmpty()) ? libelle.getText() : null,
                        userCategory,
                        nom.getText(),
                        prenom.getText(),
                        mail.getText(),
                        tel.getText(),
                        adresse.getText(),
                        ville.getText(),
                        codePostal.getText().matches("-?(0|[1-9]\\d*)") ? Integer.valueOf(codePostal.getText()) : 0,
                        pseudo.getText(),
                        mdp.getText(),
                        photo.getText(),
                        //uploadPicture(photo.getText()),
                        description.getText(),
                        (userType.getSelectionModel().getSelectedIndex() == 1) ? Integer.valueOf(tailleOrganisme.getText()) : null,
                        false,
                        (userType.getSelectionModel().getSelectedIndex() == 1 && !siret.getText().isEmpty()) ? siret.getText() : null,
                        dateNaissance.getValue().toString(),
                        0
                );

                if (userType.getSelectionModel().getSelectedIndex() == 1) {
                    user.setSiret(siret.getText());
                }
                user.setNbPointsSourire(0);
                user.setEstValide(false);

                Integer createUserResult = createUser(user);

                if(createUserResult < 299 && RegisterNewUser(mail.getText(), userCategory) < 299) {

                    setInfoText("Demande d'inscription effectuée");
                    clearFields(registerFields);
                } else if (createUserResult == 401) {
                    setInfoText("L'utilisateur existe déjà");
                } else {
                    setInfoText("Demande d'inscription échouée");
                }

            } else {
                ((Control) registerFields[indexFieldVerif]).setStyle("-fx-background-color: #ff7980");
                setInfoText("Veuillez remplir les champs");
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    private Integer areTextFieldsValid(Object[] registerFields) {
        for (int i = 0; i < registerFieldsLength; i++) {
            Class<?> registerFieldClassType = registerFields[i].getClass();
            if(registerFieldClassType == TextField.class && ((TextField)registerFields[i]).getText().trim().isEmpty()) {
                return i;
            } else if(registerFieldClassType == DatePicker.class && ((DatePicker)registerFields[i]).getValue().toString().trim().isEmpty()) {
                return i;
            } else if(registerFieldClassType == PasswordField.class && ((PasswordField)registerFields[i]).getText().trim().isEmpty()) {
                return i;
            } else if(registerFieldClassType == ChoiceBox.class && ((ChoiceBox)registerFields[i]).getSelectionModel().getSelectedIndex() == 3) {
                return i;
            }
        }
        return -1;
    }

    private void clearFields(Object[] registerFields) {
        for (int i = 0; i < registerFieldsLength; i++) {
            Class<?> registerFieldClassType = registerFields[i].getClass();
            if(registerFieldClassType == TextField.class) {
                ((TextField)registerFields[i]).clear();
            } else if(registerFieldClassType == PasswordField.class) {
                ((PasswordField)registerFields[i]).clear();
            } else if(registerFieldClassType == ChoiceBox.class) {
                ((ChoiceBox)registerFields[i]).getSelectionModel().selectFirst();
            }
        }
    }

    public void selectFolder(ActionEvent actionEvent) {
        Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stageNodeRoot);

        if(!(selectedDirectory == null)){
            photo.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private String uploadPicture(String text) {
        String onlineLocation = "";
        return onlineLocation;
    }

    public void displayLoginPage() {
        StageManager.getInstance().loadLoginPage(null);
    }
}
