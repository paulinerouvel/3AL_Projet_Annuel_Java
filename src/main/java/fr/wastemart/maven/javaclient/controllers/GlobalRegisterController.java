package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.User;
import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import fr.wastemart.maven.javaclient.services.Details.UserDetail;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static fr.wastemart.maven.javaclient.services.User.*;

public class GlobalRegisterController extends GenericController {

    private String option;
    private User userToModif;
    private Object[] registerFields;
    private Integer registerFieldsLength = 10;
    private Integer IdCat;


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


    public void init(List<Detail> detail) {

        StringDetail optionDetail = (StringDetail) detail.get(0);
        option = optionDetail.getValue();







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


        if(option == "modify"){

            userType.setVisible(false);

            UserDetail userDetail = (UserDetail) detail.get(1);
            userToModif = userDetail.getValue();


            JSONObject jsonCate = fr.wastemart.maven.javaclient.services.User.fetchCategoryAsJSONObject(userToModif.getId());

            System.out.println(jsonCate);

            Integer IdCat = jsonCate.getInt("Categorie_utilisateur_id");

            System.out.println(userToModif.getDateDeNaissance());


            if(IdCat == 3 || IdCat == 4 || IdCat == 5){

                nom.setVisible(true);
                prenom.setVisible(true);
                organismLabel.setVisible(false);
                libelle.setVisible(false);
                description.setVisible(false);
                tailleOrganisme.setVisible(false);
                siret.setVisible(false);

                prenom.setText(userToModif.getPrenom());
                nom.setText(userToModif.getNom());
                mail.setText(userToModif.getMail());
                tel.setText(userToModif.getTel());
                adresse.setText(userToModif.getAdresse());
                ville.setText(userToModif.getVille());
                codePostal.setText(String.valueOf(userToModif.getCodePostal()));
                dateNaissance.setValue(userToModif.getDateDeNaissance() == null ? LocalDate.now() : LocalDate.parse(userToModif.getDateDeNaissance()));
                pseudo.setText(userToModif.getPseudo());
                photo.setText(userToModif.getPhoto());


            } else {
                nom.setVisible(false);
                prenom.setVisible(false);
                organismLabel.setVisible(true);
                libelle.setVisible(true);
                description.setVisible(true);
                tailleOrganisme.setVisible(true);
                siret.setVisible(true);

                pseudo.setText(userToModif.getPseudo());
                mail.setText(userToModif.getMail());
                tel.setText(userToModif.getTel());
                adresse.setText(userToModif.getAdresse());
                ville.setText(userToModif.getVille());
                codePostal.setText(String.valueOf(userToModif.getCodePostal()));
                photo.setText(userToModif.getPhoto());
                libelle.setText(userToModif.getLibelle());
                description.setText(userToModif.getDesc());
                tailleOrganisme.setText(String.valueOf(userToModif.getTailleOrganisme()));
                siret.setText(userToModif.getSiret());

            }




        }
        else{

            userType.setVisible(true);
            userType.setItems(FXCollections.observableArrayList("Employé", "Professionnel", "Admin"));
        }


        userType.getSelectionModel().selectFirst();

        userType.setTooltip(new Tooltip("Sélectionnez le type d'utilisateur"));
    }

    public void refreshUser() {


        if(userType.getSelectionModel().getSelectedIndex() == 0 || userType.getSelectionModel().getSelectedIndex() == 2
                || (IdCat != null && (IdCat == 3 || IdCat == 4 || IdCat == 5))){

            nom.setVisible(true);
            prenom.setVisible(true);
            organismLabel.setVisible(false);
            libelle.setVisible(false);
            description.setVisible(false);
            tailleOrganisme.setVisible(false);
            siret.setVisible(false);


            organismLabel.setText("");
            libelle.setText("");
            description.setText("");
            tailleOrganisme.setText("");
            siret.setText("");
            registerFieldsLength = 10;
        } else {
            nom.setText("");
            prenom.setText("");

            nom.setVisible(false);
            prenom.setVisible(false);
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

                boolean resultUser = false;

                if(option == "add"){
                    resultUser = createUser(user);
                }
                else{





                    if(user.getMdp().isEmpty()){
                        user.setMdp(userToModif.getMdp());
                    }
                    user.setEstValide(true);
                    user.setId(userToModif.getId());
                    resultUser = updateUser(user, UserInstance.getInstance().getTokenValue());
                }


                if(option == "add"){

                    if(userType.getSelectionModel().getSelectedIndex() == 0 || userType.getSelectionModel().getSelectedIndex() == 2){
                        if(resultUser && RegisterNewUser(mail.getText(), userCategory)) {
                            setInfoText("Demande d'inscription effectuée");
                            clearFields(registerFields);
                        }  else {
                            setInfoText("Demande d'inscription échouée");
                        }
                    }
                    else{
                        if(resultUser && RegisterNewUser(mail.getText(), userCategory)) {
                            setInfoText("Inscription effectuée");
                            clearFields(registerFields);
                        }  else {
                            setInfoText("Inscription échouée");
                        }
                    }

                }else{


                    if(resultUser && RegisterNewUser(mail.getText(), userCategory)) {
                        setInfoText("Modification effectuée");
                        clearFields(registerFields);
                    }  else {
                        setInfoText("Modification échouée");
                    }


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

    //areTextFieldsValid et mettre une option sur le else if passwordtype.isempty

    private Integer areTextFieldsValid(Object[] registerFields) {
        for (int i = 0; i < registerFieldsLength; i++) {
            Class<?> registerFieldClassType = registerFields[i].getClass();
            if(registerFieldClassType == TextField.class && ((TextField)registerFields[i]).getText().trim().isEmpty()) {
                return i;
            } else if(registerFieldClassType == DatePicker.class && ((DatePicker)registerFields[i]).getValue().toString().trim().isEmpty()) {
                return i;
            }
            else if(option != "modify" && registerFieldClassType == PasswordField.class && ((PasswordField)registerFields[i]).getText().trim().isEmpty()) {
                return i;
            }
            else if(registerFieldClassType == ChoiceBox.class && ((ChoiceBox)registerFields[i]).getSelectionModel().getSelectedIndex() == 3) {
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
