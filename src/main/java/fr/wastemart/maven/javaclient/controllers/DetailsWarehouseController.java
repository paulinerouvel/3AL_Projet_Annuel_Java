package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Warehouse;
import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import fr.wastemart.maven.javaclient.services.Details.WarehouseDetail;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

import static fr.wastemart.maven.javaclient.services.Warehouse.createWarehouse;
import static fr.wastemart.maven.javaclient.services.Warehouse.updateWarehouse;

public class DetailsWarehouseController extends GenericController {

    private String option;
    private Warehouse warehouseToModif;
    private Object[] registerFields;

    @FXML private TextField libelle;
    @FXML private TextField adresse;
    @FXML private TextField ville;
    @FXML private TextField codePostal;
    @FXML private TextField desc;
    @FXML private TextField photo;
    @FXML private TextField placeTotal;
    @FXML private TextField placeLibre;

    public void init(List<Detail> detail) {

        StringDetail optionDetail = (StringDetail) detail.get(0);
        option = optionDetail.getValue();

        if(option == "modify"){

            WarehouseDetail warehouseDetail = (WarehouseDetail) detail.get(1);
            warehouseToModif = warehouseDetail.getValue();


            libelle.setText(warehouseToModif.getLibelle());
            adresse.setText(warehouseToModif.getAdresse());
            ville.setText(warehouseToModif.getVille());
            codePostal.setText(warehouseToModif.getCodePostal());
            desc.setText(warehouseToModif.getDesc());
            photo.setText(warehouseToModif.getPhoto());
            placeTotal.setText(String.valueOf(warehouseToModif.getPlaceTotal()));
            placeLibre.setText(String.valueOf(warehouseToModif.getPlaceLibre()));

        }

        registerFields = new Object[8];
        registerFields[0] = libelle;
        registerFields[1] = adresse;
        registerFields[2] = ville;
        registerFields[3] = codePostal;
        registerFields[4] = desc;
        registerFields[5] = photo;
        registerFields[6] = placeTotal;
        registerFields[7] = placeLibre;

    }

    public void save() {
        try {
            for (Object registerField : registerFields) {
                ((TextField) registerField).setStyle("-fx-background-color: #FFFFFF");
            }

            Integer indexFieldVerif = areTextFieldsValid(registerFields);

            if (indexFieldVerif == -1) {

                Warehouse warehouse = new Warehouse(-1,
                        libelle.getText(),
                        adresse.getText(),
                        ville.getText(),
                        codePostal.getText(),
                        desc.getText(),
                        photo.getText(),
                        Integer.valueOf(placeTotal.getText()),
                        Integer.valueOf(placeLibre.getText())
                );

                // TODO Verif if libre vide alors max

                boolean resultWarehouse = false;

                if(option.equals("add")){
                    resultWarehouse = createWarehouse(warehouse);
                }
                else{
                    warehouse.setId(warehouseToModif.getId());
                    resultWarehouse = updateWarehouse(warehouse, UserInstance.getInstance().getTokenValue());
                }
                if(resultWarehouse) {
                    if (option.equals("add")) {
                       setInfoText("Inscription effectuée");
                        clearFields(registerFields);
                    } else {
                        setInfoText("Modification effectuée");
                        clearFields(registerFields);
                    }

                } else {
                    setInfoText("Enregistrement échouée");
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
        for (int i = 0; i < registerFields.length; i++) {
            Class<?> registerFieldClassType = registerFields[i].getClass();
            if(registerFieldClassType == TextField.class && ((TextField)registerFields[i]).getText().trim().isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private void clearFields(Object[] registerFields) {
        for (Object registerField : registerFields) {
            ((TextField) registerField).clear();
        }
    }

    // TODO make select folder
    public void selectFolder(ActionEvent actionEvent) {
        Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stageNodeRoot);

        if(!(selectedDirectory == null)){
            photo.setText(selectedDirectory.getAbsolutePath());
        }
    }

    // TODO upload picture
    private String uploadPicture(String text) {
        String onlineLocation = "";
        return onlineLocation;
    }

}

