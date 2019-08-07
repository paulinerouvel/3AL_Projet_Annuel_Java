package fr.wastemart.maven.javaclient.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import fr.wastemart.maven.javaclient.models.User;
import fr.wastemart.maven.javaclient.services.UserInstance;

import java.io.File;
import java.time.LocalDate;

public class RegisterController extends GenericController {

    private StageManager stageManager;
    private UserInstance instance = new UserInstance();
    private Object[] registerFields;
    private Integer registerFieldsLength = 10;
    @FXML private Label info;
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
    @FXML private TextField description;
    @FXML private TextField siret;
    @FXML private TextField tailleOrganisme;

    public void init(){
        userType.setItems(FXCollections.observableArrayList("Employé", "Professionnel", "Admin"));

        registerFields = new Object[15];
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
        registerFields[12] = description;
        registerFields[13] = siret;
        registerFields[14] = tailleOrganisme;

        clearFields(registerFields);
        userType.getSelectionModel().selectFirst();

        userType.setTooltip(new Tooltip("Sélectionnez le type d'utilisateur"));
    }

    public void refreshUser(ActionEvent actionEvent) {
        if(userType.getSelectionModel().getSelectedIndex() == 0){
            organismLabel.setVisible(false);
            description.setVisible(false);
            siret.setVisible(false);
            tailleOrganisme.setVisible(false);
            registerFieldsLength = 10;
        } else {
            organismLabel.setVisible(true);
            description.setVisible(true);
            siret.setVisible(true);
            tailleOrganisme.setVisible(true);
            registerFieldsLength = 15;
        }
    }

    public void register(ActionEvent actionEvent) {

        for (Object registerField : registerFields) {
            ((Control) registerField).setStyle("-fx-background-color: #FFFFFF");
        }

        Integer indexFieldVerif = areTextFieldsValid(registerFields);
        if(indexFieldVerif == -1) {

            Integer userCategory = userType.getSelectionModel().getSelectedIndex() == 0 ? 4 :
                    userType.getSelectionModel().getSelectedIndex() == 1 ? 2 : 5;

            User user = new User(-1,
            userType.getSelectionModel().getSelectedItem(),
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
            photo.getText().isEmpty() ? null : uploadPicture(photo.getText()),
            description.getText().isEmpty() ? null : description.getText(),
            (userType.getSelectionModel().getSelectedIndex() == 0 || !tailleOrganisme.getText().isEmpty()) ? 0 : Integer.valueOf(tailleOrganisme.getText()),
            0,
            (userType.getSelectionModel().getSelectedIndex() == 0 || !siret.getText().isEmpty()) ? "null" : siret.getText(),
            dateNaissance.getValue().toString(),
            0
            );

            if(userType.getSelectionModel().getSelectedIndex() != 0) {
                user.setSiret(siret.getText());

            }
            user.setNbPointsSourire(0);
            user.setEstValide(0);

            Integer saveResult = fr.wastemart.maven.javaclient.services.User.createUser(user);
            if(saveResult > 299) {
                info.setText("Demande d'inscription échouée : "+ saveResult);
            }

            Integer addCategoryResult = fr.wastemart.maven.javaclient.services.User.initNewUser(mail.getText(), userCategory);
            if(addCategoryResult < 299){
                info.setText("Demande d'inscription faite");
            } else {
                info.setText("Demande d'inscription échouée : "+ saveResult);
            }

            clearFields(registerFields);


        } else {
            Class<?> registerFieldClassType = registerFields[indexFieldVerif].getClass();
            if(registerFieldClassType.getSuperclass().getSuperclass().equals(Control.class)) {
                ((Control) registerFields[indexFieldVerif]).setStyle("-fx-background-color: #ff7980");
            }

            info.setText("Veuillez remplir les champs");
        }
    }

    private Integer areTextFieldsValid(Object[] registerFields)  {
        for (int i = 0; i < registerFieldsLength; i++) {
            Class<?> registerFieldClassType = registerFields[i].getClass();
            if(registerFieldClassType == TextField.class && ((TextField)registerFields[i]).getText().trim().isEmpty()){
                return i;
            }else if(registerFieldClassType == DatePicker.class && ((DatePicker)registerFields[i]).getValue().toString().trim().isEmpty()){
                return i;
            }else if(registerFieldClassType == PasswordField.class && ((PasswordField)registerFields[i]).getText().trim().isEmpty()){
                return i;
            }else if(registerFieldClassType == ChoiceBox.class && ((ChoiceBox)registerFields[i]).getSelectionModel().getSelectedIndex() == 3){
                return i;
            }
        }
        return -1;
    }



    private void clearFields(Object[] registerFields) {
        for (int i = 0; i < registerFieldsLength; i++) {
            Class<?> registerFieldClassType = registerFields[i].getClass();
            if(registerFieldClassType == TextField.class){
                ((TextField)registerFields[i]).clear();
            }else if(registerFieldClassType == DatePicker.class){
                ((DatePicker)registerFields[i]).setValue(LocalDate.of(2000,01,01));
            }else if(registerFieldClassType == PasswordField.class){
                ((PasswordField)registerFields[i]).clear();
            }else if(registerFieldClassType == ChoiceBox.class){
                ((ChoiceBox)registerFields[i]).getSelectionModel().selectFirst();
            }
        }
        info.setText("");
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

    public void displayLoginPage(ActionEvent actionEvent) throws Exception {
        StageManager.loadRootlessPage(actionEvent, "/fr.wastemart.maven.javaclient/views/Login.fxml");
    }
}
