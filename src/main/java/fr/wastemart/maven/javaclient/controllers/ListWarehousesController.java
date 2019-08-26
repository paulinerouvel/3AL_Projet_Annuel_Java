package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.models.Warehouse;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import org.json.JSONArray;
import org.json.JSONObject;

import static fr.wastemart.maven.javaclient.services.Product.fetchProductsByWarehouse;
import static fr.wastemart.maven.javaclient.services.Product.jsonToProduct;
import static fr.wastemart.maven.javaclient.services.Warehouse.fetchAllWarehouse;


public class ListWarehousesController extends GenericController {
    private JSONArray warehouses;
    private JSONArray products;
    private Integer indexOfProductSelected;
    private Integer indexOfListSelected;
    private Integer swapIdWarehouse;

    // warehouse
    @FXML TableView<Warehouse> warehouseTable;
    @FXML TableColumn<Object, Object> listId;
    @FXML TableColumn<String, String> listName;
    @FXML TableColumn<Object, Object> listCity;
    @FXML TableColumn<Object, Object> listPlace;
    @FXML TableColumn<Object, Object> listTotalPlace;

    // products
    @FXML TableView<Product> productsTable;
    @FXML TableColumn<Object, Object> productName;
    @FXML TableColumn<Object, Object> productDesc;
    @FXML TableColumn<Object, Object> productPrice;
    @FXML TableColumn<Object, Object> productInitialPrice;
    @FXML TableColumn<Object, Object> productDlc;
    @FXML TableColumn<Object, Object> productAvailable;
    @FXML TableColumn<Object, Object> productDate;
    @FXML TableColumn<Object, Object> productQuantity;
    @FXML TableColumn<Object, Object> IDwarehouse;

    @FXML
    Label saveLabel;

    public void init() throws Exception {
        displayWarehouseLists();
        displayProductsByWarehouse(warehouses.getJSONObject(0).getInt("id"));
        warehouseTable.getSelectionModel().selectFirst();
    }

    private void displayWarehouseLists() throws Exception {
        warehouseTable.getItems().clear();
        warehouses = fetchAllWarehouse();

        listId.setCellValueFactory(new PropertyValueFactory<>("id"));
        listName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        listCity.setCellValueFactory(new PropertyValueFactory<>("ville"));
        listPlace.setCellValueFactory(new PropertyValueFactory<>("placeLibre"));
        listTotalPlace.setCellValueFactory(new PropertyValueFactory<>("placeTotal"));
        listName.setCellFactory(TextFieldTableCell.forTableColumn());
        // liste combobox entrepot
        ObservableList<Object> idWarehouse = FXCollections.observableArrayList();
        IDwarehouse.setCellFactory(ComboBoxTableCell.forTableColumn(idWarehouse));
        IDwarehouse.setOnEditCommit((TableColumn.CellEditEvent<Object, Object> e) -> {
             swapIdWarehouse = (Integer)e.getNewValue();

        });

        for (int i = 0; i < warehouses.length(); i++) {
            JSONObject list = warehouses.getJSONObject(i);
            Warehouse warehouse = new Warehouse(list.getInt("id"),
                    list.getString("libelle"),
                    list.getString("adresse"),
                    list.getString("ville"),
                    list.getString("codePostal"),
                    list.getString("desc"),
                    list.getString("photo"),
                    list.getInt("placeTotal"),
                    list.getInt("placeLibre")
            );
            warehouseTable.getItems().add(warehouse);
            idWarehouse.add(list.getInt("id"));
        }

    }

    private void displayProductsByWarehouse(Integer id) throws Exception {
        productsTable.getItems().clear();
        products = fetchProductsByWarehouse(id);

        productName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        productDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
        productInitialPrice.setCellValueFactory(new PropertyValueFactory<>("prixInitial"));
        productDlc.setCellValueFactory(new PropertyValueFactory<>("dlc"));
        productAvailable.setCellValueFactory(new PropertyValueFactory<>("enRayon"));
        productDate.setCellValueFactory(new PropertyValueFactory<>("dateMiseEnRayon"));
        productQuantity.setCellValueFactory(new PropertyValueFactory<>("quantite"));


        for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            Product productElement = jsonToProduct(product);

            productsTable.getItems().add(productElement);
        }
    }

    @FXML
    public void clickItem() {
        try {
            refreshSelectedIndices();

            if (indexOfListSelected != -1) {
                displayProductsByWarehouse(warehouses.getJSONObject(indexOfListSelected).getInt("id"));
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    @FXML
    public void validate() {
        try {
            // UPDATE entrepot du produit
            if (swapIdWarehouse != productsTable.getSelectionModel().getSelectedItem().getEntrepotwm()) {
                //update du produit + rechargement
                //updateProduct(productsTable.getSelectionModel().getSelectedItem().getId(), swapIdWarehouse); TODO Switches id of warehouse of product
                init();
                saveLabel.setTextFill(Color.web("#008000", 1));
                saveLabel.setText("Enregistrement validé :)");
                saveLabel.setVisible(true);
            } else {

                saveLabel.setTextFill(Color.web("#ff0000", 1));
                saveLabel.setText("L'enregistrement a echoué :(");
                saveLabel.setVisible(true);
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void refreshSelectedIndices() {
        this.indexOfProductSelected = productsTable.getSelectionModel().getSelectedIndex();
        this.indexOfListSelected = warehouseTable.getSelectionModel().getSelectedIndex();

    }

    // Return button
    public void displayMainPage() {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance());
    }
}