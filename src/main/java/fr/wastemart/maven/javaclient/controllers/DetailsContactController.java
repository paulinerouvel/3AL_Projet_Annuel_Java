package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import fr.wastemart.maven.javaclient.services.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static fr.wastemart.maven.javaclient.services.User.*;
import static javafx.collections.FXCollections.observableList;

public class DetailsContactController extends GenericController {
    @FXML private ChoiceBox<String> receiverChoice;
    @FXML private Label receiver;
    @FXML private TextField subject;
    @FXML private TextArea body;

    @Override
    public void init(List<Detail> contactDetails) throws Exception {
        if(contactDetails != null) {
            StringDetail mailDetail = (StringDetail) contactDetails.get(0);
            receiver.setText(mailDetail.getValue());
        } else if(receiverChoice != null) {
            loadAdminReceivers();
        } else {
            initFail();
        }
    }

    @Override
    public void initFail() {
        super.initFail();
        subject.setDisable(true);
        body.setDisable(true);
    }

    private void loadAdminReceivers() throws Exception {
        JSONArray adminUsers = fetchUsersByCategory("Administrateur");

        ObservableList<String> mailList = FXCollections.observableArrayList();

        for (int i = 0; i < adminUsers.length(); i++){
            mailList.add(jsonToUser((JSONObject) adminUsers.get(i)).getMail());
        }

        receiverChoice.setItems(mailList);
        receiverChoice.getSelectionModel().selectFirst();
    }

    public void submitMessage(ActionEvent event) {
        try {
            subject.setStyle("-fx-background-color: #FFFFFF");
            body.setStyle("-fx-background-color: #FFFFFF");

            Integer index;
            if((index = areFieldsValid()) != -1){
                switch(index) {
                    case 0:
                        subject.setStyle("-fx-background-color: #ff7980");
                        break;
                    case 1:
                        body.setStyle("-fx-background-color: #ff7980");
                        break;
                    default:
                        break;
                }
            } else {
                String mail;
                if(receiverChoice != null){
                    mail = receiverChoice.getSelectionModel().getSelectedItem();
                } else {
                    mail = receiver.getText();
                }

                if(sendMail(mail, subject.getText(), body.getText())){
                    clearFields();
                    setInfoText("Message envoyé");

                } else {
                    setInfoText("Echec de l'envoi du message");
                }
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    private Integer areFieldsValid()  {
        if(subject.getText().trim().isEmpty()){ return 0; }
        else if(body.getText().trim().isEmpty()){ return 1; }
        return -1;
    }

    private void clearFields() {
        subject.clear();
        body.clear();
        setInfoText("");
    }
}
