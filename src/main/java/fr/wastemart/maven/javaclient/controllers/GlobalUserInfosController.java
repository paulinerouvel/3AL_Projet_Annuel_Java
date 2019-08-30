package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.User;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

import static fr.wastemart.maven.javaclient.services.User.*;

public class GlobalUserInfosController extends GenericController {

    @FXML
    private Label name;
    @FXML
    private TextField email;
    @FXML
    private TextField address;
    @FXML
    private TextField city;
    @FXML
    private TextField postalCode;
    @FXML
    private TextField phone;
    @FXML
    private PasswordField password;
    @FXML
    private ImageView photoView;
    @FXML
    private TextField photoPathField;
    private File photo;

    public void init() throws Exception {
        name.setText(UserInstance.getInstance().getUser().getNom());
        address.setText(UserInstance.getInstance().getUser().getAdresse());
        city.setText(UserInstance.getInstance().getUser().getVille());
        email.setText(UserInstance.getInstance().getUser().getMail());
        phone.setText(UserInstance.getInstance().getUser().getTel());
        postalCode.setText(UserInstance.getInstance().getUser().getCodePostal().toString());

        if((photo = fetchPhoto(UserInstance.getInstance().getUser().getPhoto())) != null){
            photoView.setImage(new Image(photo.toURI().toURL().toExternalForm()));

        }
    }

    public void save() {
        //TODO Contrôle sur les modifs ici ou dans l'api
        try {
            User user = UserInstance.getInstance().getUser();
            user.setVille(city.getText());
            user.setTel(phone.getText());
            user.setMail(email.getText());
            user.setCodePostal(postalCode.getText() == null ? null : Integer.valueOf(postalCode.getText()));
            user.setAdresse(address.getText());

            if(!password.getText().isEmpty()) {
                user.setMdp(password.getText());
            }

            if(!photoPathField.getText().isEmpty()) {
                System.out.println("(GlobalUserInfosController.save) Photo is not null!");
                String photoName;
                if((photoName = sendPhoto(photo)) != null) {
                    System.out.println("(GlobalUserInfosController.save) Photo is:"+photoName);
                    user.setPhoto(photoName);
                }
            }

            System.out.println("(GlobalUserInfosController.save) About to update User");
            if(updateUser(UserInstance.getInstance().getUser(), UserInstance.getInstance().getTokenValue()) && user.getMdp().length() >=2) {
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
}