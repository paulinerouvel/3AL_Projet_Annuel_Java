package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.services.*;
import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.IntegerDetail;
import fr.wastemart.maven.javaclient.services.Details.ProductDetail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static fr.wastemart.maven.javaclient.services.Product.*;
import static fr.wastemart.maven.javaclient.services.Warehouse.fetchWarehouse;
import static fr.wastemart.maven.javaclient.services.Warehouse.jsonToWarehouse;

public class DetailsProductController extends GenericController {

    private Integer listId;
    private Integer warehouseId;
    private Integer destId;
    private String option;
    private Product product;
    private File photo;

    @FXML private TextField libelle;
    @FXML private TextField desc;
    @FXML private TextField photoField;
    @FXML private TextField prix;
    @FXML private TextField quantite;
    @FXML private DatePicker dlc;
    @FXML private TextField codeBarre;
    @FXML private ChoiceBox<String> categorieProduit;

    @FXML private Label photoLabel;
    @FXML private Button parcourirButton;

    public void init(List<Detail> detail) throws Exception {
        StringDetail optionDetail = (StringDetail) detail.get(0);
        option = optionDetail.getValue();

        setProductCategories();



        switch (option) {
            case "add":
                warehouseId = (detail.get(1) != null) ? ((IntegerDetail) detail.get(1)).getValue() : null;
                destId = (detail.get(2) != null) ? ((IntegerDetail) detail.get(2)).getValue() : null;
                clearFields();
                categorieProduit.getSelectionModel().selectFirst();

                photoField.setVisible(false);
                photoLabel.setVisible(false);
                parcourirButton.setVisible(false);
                break;
            case "modify":
                ProductDetail productDetail = (ProductDetail) detail.get(1);
                product = productDetail.getValue();
                setFields(product);
                break;
            case "addtolist":
                listId = (detail.get(1) != null) ? ((IntegerDetail) detail.get(1)).getValue() : null;
                warehouseId = (detail.get(2) != null) ? ((IntegerDetail) detail.get(2)).getValue() : null;
                destId = (detail.get(3) != null) ? ((IntegerDetail) detail.get(3)).getValue() : null;
                clearFields();
                categorieProduit.getSelectionModel().selectFirst();

                photoField.setVisible(false);
                photoLabel.setVisible(false);
                parcourirButton.setVisible(false);

                break;
        }

        categorieProduit.setTooltip(new Tooltip("Sélectionnez un type de produit"));
    }

    @FXML
    private void submitProduct() {
        try {
            clearInfoText();

            libelle.setStyle("-fx-background-color: #FFFFFF");
            desc.setStyle("-fx-background-color: #FFFFFF");
            prix.setStyle("-fx-background-color: #FFFFFF");
            quantite.setStyle("-fx-background-color: #FFFFFF");
            dlc.setStyle("-fx-background-color: #FFFFFF");
            codeBarre.setStyle("-fx-background-color: #FFFFFF");

            Integer indexWrong = areTextFieldsValid();


            if (indexWrong != -1) {
                switch (indexWrong) {
                    case 0:
                        libelle.setStyle("-fx-background-color: #ff7980");
                        setInfoText("Remplissez tous les champs correctement");
                        break;
                    case 1:
                        desc.setStyle("-fx-background-color: #ff7980");
                        setInfoText("Remplissez tous les champs correctement");
                        break;
                    case 2:
                        prix.setStyle("-fx-background-color: #ff7980");
                        setInfoText("Remplissez tous les champs correctement");
                        break;
                    case 3:
                        quantite.setStyle("-fx-background-color: #ff7980");
                        setInfoText("Remplissez tous les champs correctement");
                        break;
                    case 4:
                        dlc.setStyle("-fx-background-color: #ff7980");
                        setInfoText("La date ne doit pas être inférieure à aujourd'hui");
                        break;
                    case 5:
                        codeBarre.setStyle("-fx-background-color: #ff7980");
                        setInfoText("Remplissez tous les champs correctement");
                        break;
                    default:
                        break;

                }

            } else {

                fr.wastemart.maven.javaclient.models.Warehouse affectedWareHouse =
                        jsonToWarehouse(fetchWarehouse(warehouseId));


                if(affectedWareHouse.getPlaceLibre() - Integer.valueOf(quantite.getText()) >= 0) {


                    Product newProduct = new Product(product == null ? -1 : product.getId(),
                            libelle.getText(),
                            desc.getText(),
                            null,
                            Float.valueOf(prix.getText()),
                            Integer.valueOf(quantite.getText()),
                            DateFormatter.dateToString(dlc.getValue().toString()),
                            codeBarre.getText(),
                            false,
                            product == null ? null : product.getDateMiseEnRayon() == null ? null : product.getDateMiseEnRayon().toString(),
                            categorieProduit.getSelectionModel().getSelectedIndex() + 1,
                            listId,
                            warehouseId,
                            destId
                    );


                    boolean resultProduct;

                    if (option.equals("add") || option.equals("addtolist")) {
                        if (addProduct(newProduct, UserInstance.getInstance().getTokenValue())) {
                            clearFields();
                            setInfoText("Produit créé avec succès");
                        } else {
                            setInfoText("Le produit n'a pas pu être créé");
                        }


                    } else if (option.equals("modify")) {
                        if (!photoField.getText().isEmpty() && !photoField.getText().equals(String.valueOf(product.getPhoto()))) {

                            String photoName;
                            if ((photoName = sendPhoto(photo, product.getId())) != null) {
                                newProduct.setPhoto(photoName);
                            } else {
                                newProduct.setPhoto(product.getPhoto());
                            }
                        } else {
                            newProduct.setPhoto(product.getPhoto());
                        }

                        resultProduct = updateProduct(newProduct, UserInstance.getInstance().getTokenValue());

                        if (resultProduct) {
                            setInfoText("Produit modifié avec succès");
                        } else {
                            setInfoText("Le produit n'a pas pu être modifié");
                        }
                    }
                } else {
                    setInfoText("La taille de l'entrepôt est dépassée, place libre : "+ affectedWareHouse.getPlaceLibre());
                }
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    @FXML
    private void changeProductPicture() {
        try {
            FileChooser fileChooser = new FileChooser();

            File actualDirectory = new File(System.getProperty("user.dir"));
            fileChooser.setInitialDirectory(actualDirectory);

            photo = fileChooser.showOpenDialog(StageManager.getInstance().getStage());

            if (photo != null && photo.exists()) {
                photoField.setText(photo.getAbsolutePath());
                photoField.positionCaret(photoField.getLength());

                setInfoText("Image changée");
            }  // Else No File selected


        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    private Integer areTextFieldsValid() {
        if(libelle.getText().trim().isEmpty()){ return 0; }
        else if(desc.getText().trim().isEmpty()){ return 1; }
        else if(!NumberUtils.isNumber(prix.getText()) || prix.getText().trim().isEmpty()
                || Integer.valueOf(quantite.getText()) < 1){
            return 2;
        }
        else if(!NumberUtils.isNumber(quantite.getText())  || quantite.getText().trim().isEmpty()
                || Integer.valueOf(quantite.getText()) < 1 ) {
            return 3;
        }
        else if(dlc.getValue().toString().trim().isEmpty() || dlc.getValue().compareTo(LocalDate.now()) < 0) {
            return 4;
        } else if(codeBarre.getText().trim().isEmpty()) {
            return 5;
        }

        return -1;
    }

    private void clearFields() {
        libelle.clear();
        desc.clear();
        photoField.clear();
        prix.clear();
        quantite.clear();
        dlc.setValue(LocalDate.now());
        codeBarre.clear();
        setInfoText("");
    }

    private void setFields(Product product) {
        libelle.setText(product.getLibelle());
        desc.setText(product.getDesc());
        photoField.setText(product.getPhoto());
        prix.setText(String.valueOf(product.getPrix()));
        quantite.setText(String.valueOf(product.getQuantite()));
        dlc.setValue(product.getCodeBarre() == null ? LocalDate.now() : LocalDate.parse(product.getDlc())); // Nullable
        codeBarre.setText(product.getCodeBarre() == null ? "" : product.getCodeBarre()); // Nullable
        categorieProduit.getSelectionModel().select(product.getCategorieProduit() - 1);
        listId = product.getListProduct();
        warehouseId = product.getEntrepotwm();
        destId = product.getDestinataire();
        setInfoText("");

        if(option.equals("modify") && (photo = fetchPhoto(product.getPhoto())) != null){
            photoField.setText(photo.getAbsolutePath());
        }


    }

    private void setProductCategories(){
        JSONArray productCategories = fetchProductCategories();

        ObservableList<String> categories = FXCollections.observableArrayList();

        for (int i = 0; i < productCategories.length(); i++){
            JSONObject category = (JSONObject) productCategories.get(i);
            categories.add(String.valueOf(category.get("libelle")));
        }

        categorieProduit.setItems(categories);
    }


}
