package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.User;
import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static fr.wastemart.maven.javaclient.services.User.*;

public class GlobalUserInfosController extends GenericController {

    @FXML
    private Label nomLabel;
    @FXML
    private Label prenomLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label telephoneLabel;
    @FXML
    private Label adresseLabel;
    @FXML
    private Label villeLabel;
    @FXML
    private Label codepostalLabel;
    @FXML
    private Label datedenaissanceLabel;
    @FXML
    private Label pseudoLabel;
    @FXML
    private Label motdepasseLabel;
    @FXML
    private Label libelleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label tailleorganismeLabel;
    @FXML
    private Label siretLabel;




    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField email;
    @FXML
    private TextField telephone;
    @FXML
    private TextField adresse;
    @FXML
    private TextField ville;
    @FXML
    private TextField codepostal;
    @FXML
    private DatePicker datedenaissance;
    @FXML
    private TextField pseudo;
    @FXML
    private PasswordField motdepasse;

    @FXML
    private TextField libelle;
    @FXML
    private TextArea description;
    @FXML
    private TextField tailleorganisme;
    @FXML
    private TextField siret;
    @FXML
    private ImageView photoView;
    @FXML
    private TextField photoPathField;
    private File photo;

    private String idCat;
    private Object[] registerFields;



    public void init() throws Exception {





        idCat = UserInstance.getInstance().getUser().getCategorieUtilisateur().toString();


        if(idCat.equals( "2")){


            registerFields = new Object[11];
            registerFields[0] = email;
            registerFields[1] = telephone;
            registerFields[2] = adresse;
            registerFields[3] = ville;
            registerFields[4] = codepostal;
            registerFields[5] = datedenaissance;
            registerFields[6] = pseudo;

            registerFields[7] = libelle;
            registerFields[8] = description;
            registerFields[9] = tailleorganisme;
            registerFields[10] = siret;
        }
        else{
            registerFields = new Object[9];
            registerFields[0] = nom;
            registerFields[1] = prenom;
            registerFields[2] = email;
            registerFields[3] = telephone;
            registerFields[4] = adresse;
            registerFields[5] = ville;
            registerFields[6] = codepostal;
            registerFields[7] = datedenaissance;
            registerFields[8] = pseudo;


        }



        if(idCat.equals( "2")){
            nomLabel.setVisible(false);
            prenomLabel.setVisible(false);
            nom.setVisible(false);
            prenom.setVisible(false);

            libelle.setVisible(true);
            siret.setVisible(true);
            description.setVisible(true);
            tailleorganisme.setVisible(true);
            libelleLabel.setVisible(true);
            siretLabel.setVisible(true);
            descriptionLabel.setVisible(true);
            tailleorganismeLabel.setVisible(true);

            libelle.setText(UserInstance.getInstance().getUser().getLibelle());
            tailleorganisme.setText(UserInstance.getInstance().getUser().getTailleOrganisme().toString());
            siret.setText(UserInstance.getInstance().getUser().getSiret());
            description.setText(UserInstance.getInstance().getUser().getDesc());

        }
        else{

            nomLabel.setVisible(true);
            prenomLabel.setVisible(true);
            nom.setVisible(true);
            prenom.setVisible(true);

            libelle.setVisible(false);
            siret.setVisible(false);
            description.setVisible(false);
            tailleorganisme.setVisible(false);
            libelleLabel.setVisible(false);
            siretLabel.setVisible(false);
            descriptionLabel.setVisible(false);
            tailleorganismeLabel.setVisible(false);


            nom.setText(UserInstance.getInstance().getUser().getNom());
            prenom.setText(UserInstance.getInstance().getUser().getPrenom());


        }

        adresse.setText(UserInstance.getInstance().getUser().getAdresse());
        ville.setText(UserInstance.getInstance().getUser().getVille());
        codepostal.setText(UserInstance.getInstance().getUser().getCodePostal().toString());

        email.setText(UserInstance.getInstance().getUser().getMail());
        telephone.setText(UserInstance.getInstance().getUser().getTel());
        pseudo.setText(UserInstance.getInstance().getUser().getPseudo());
        datedenaissance.setValue(UserInstance.getInstance().getUser().getDateDeNaissance() == null ? LocalDate.now() : LocalDate.parse(UserInstance.getInstance().getUser().getDateDeNaissance()));



        if((photo = fetchPhoto(UserInstance.getInstance().getUser().getPhoto())) != null){
            photoView.setImage(new Image(photo.toURI().toURL().toExternalForm()));

        }
    }

    public void save() {
        try {

            Integer indexFieldVerif = areTextFieldsValid(registerFields);

            if (indexFieldVerif == -1) {
                User user = UserInstance.getInstance().getUser();
                user.setAdresse(adresse.getText());
                user.setVille(ville.getText());
                user.setCodePostal(codepostal.getText() == null ? null : Integer.valueOf(codepostal.getText()));
                user.setTel(telephone.getText());
                user.setMail(email.getText());
                user.setPseudo(pseudo.getText());
                user.setDateDeNaissance(datedenaissance.getValue() == null ? "" : datedenaissance.getValue().toString());

                if (idCat.equals( "2")) {
                    user.setLibelle(libelle.getText());
                    user.setTailleOrganisme(Integer.valueOf(tailleorganisme.getText()));
                    user.setSiret(siret.getText());
                    user.setDesc(description.getText());

                } else {
                    user.setNom(nom.getText());
                    user.setPrenom(prenom.getText());
                }


                if (!motdepasse.getText().isEmpty()) {
                    user.setMdp(motdepasse.getText());
                }

                if (!photoPathField.getText().isEmpty()) {

                    String photoName;
                    if ((photoName = sendPhoto(photo, UserInstance.getInstance().getUser().getId())) != null) {

                        user.setPhoto(photoName);
                    }
                }



                if (updateUser(UserInstance.getInstance().getUser(), UserInstance.getInstance().getTokenValue()) && user.getMdp().length() >= 2) {
                    setInfoText("Modification réussie");
                } else {
                    setInfoText("Modification échouée");
                }
            }
            else{
                setInfoText("Vos champs n'ont pas été correctement remplis");
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void changeProfilePicture() {
        try {
            FileChooser fileChooser = new FileChooser();

            File actualDirectory = new File(System.getProperty("user.dir"));
            fileChooser.setInitialDirectory(actualDirectory);

            photo = fileChooser.showOpenDialog(StageManager.getInstance().getStage());

            if (photo != null && photo.exists()) {
                photoPathField.setText(photo.getAbsolutePath());
                photoPathField.positionCaret(photoPathField.getLength());

                Image image = new Image(photo.toURI().toURL().toExternalForm());

                photoView.setImage(image);
                photoView.setFitWidth(128);

                setInfoText("Image changed");
            }  // Else No File selected


        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    // Return button
    public void displayMainPage() {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance());
    }

    private Integer areTextFieldsValid(Object[] registerFields) {
        for (int i = 0; i < registerFields.length; i++) {
            Class<?> registerFieldClassType = registerFields[i].getClass();

            if(registerFieldClassType == TextField.class && ((TextField)registerFields[i]).getText().trim().isEmpty()) {
                return i;
            } else if(registerFieldClassType == DatePicker.class && (registerFields[i] == null ||
                    ((DatePicker)registerFields[i]).getValue().toString().trim().isEmpty())) {
                return i;
            }
        }
        return -1;
    }
}