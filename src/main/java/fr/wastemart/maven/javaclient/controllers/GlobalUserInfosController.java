package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.User;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

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
    private ImageView employeePhoto;

    public void init() throws Exception {
        name.setText(UserInstance.getInstance().getUser().getNom());
        address.setText(UserInstance.getInstance().getUser().getAdresse());
        city.setText(UserInstance.getInstance().getUser().getVille());
        email.setText(UserInstance.getInstance().getUser().getMail());
        phone.setText(UserInstance.getInstance().getUser().getTel());
        postalCode.setText(UserInstance.getInstance().getUser().getCodePostal().toString());
        password.setText(UserInstance.getInstance().getUser().getMdp());
        //employeePhoto.setImage();

    }

    public void save() {
        //TODO Contrôle sur les modifs ici ou dans l'api
        try {
            User user = UserInstance.getInstance().getUser();
            user.setVille(city.getText());
            user.setTel(phone.getText());
            user.setMdp(password.getText());
            user.setMail(email.getText());
            user.setCodePostal(postalCode.getText() == null ? null : Integer.valueOf(postalCode.getText()));
            user.setAdresse(address.getText());
            user.setMdp(password.getText());

            if(updateUser(UserInstance.getInstance().getUser()) && user.getMdp().length() >=2) {
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






