package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.models.Warehouse;
import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.json.JSONArray;
import org.json.JSONObject;

import static fr.wastemart.maven.javaclient.services.Product.fetchProductsByWarehouse;
import static fr.wastemart.maven.javaclient.services.Product.jsonToProduct;
import static fr.wastemart.maven.javaclient.services.Product.updateProduct;
import static fr.wastemart.maven.javaclient.services.Warehouse.fetchAllWarehouse;
import static fr.wastemart.maven.javaclient.services.Warehouse.jsonToWarehouse;



public class ListWarehousesController extends GenericController {
    private JSONArray warehouses;
    private JSONArray products;
    private Integer indexOfProductSelected;
    private Integer indexOfWarehouseSelected;
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
    @FXML TableColumn<Object, Object> productDlc;
    @FXML TableColumn<Object, Object> productAvailable;
    @FXML TableColumn<Object, Object> productDate;
    @FXML TableColumn<Object, Object> productQuantity;
    @FXML TableColumn<Object, Object> IDwarehouse;

    public void init() throws Exception {
        displayWarehouseLists();
        displayProductsByWarehouse(warehouses.getJSONObject(0).getInt("id"));
        warehouseTable.getSelectionModel().selectFirst();
    }

    @FXML
    public void moveProduct(Integer newWarehouseId) {
        clearInfoText();
        refreshSelectedIndices();

        if (indexOfProductSelected != -1) {
            if (newWarehouseId != -1) {
                Product productToSwitch = productsTable.getSelectionModel().getSelectedItem();
                productToSwitch.setEntrepotwm(newWarehouseId);


                if (updateProduct(productToSwitch, UserInstance.getInstance().getTokenValue())) {
                    setInfoText("Produit changé d'entrepôt");

                    if(displayWarehouseLists()) {
                        if (displayProductsByWarehouse(warehouses.getJSONObject(0).getInt("id"))) {
                            warehouseTable.getSelectionModel().selectFirst();
                        }
                    }
                } else {

                    setInfoErrorOccurred();
                }

            }
        } else {
            setInfoText("Veuillez sélectionner un produit");
        }
    }

    private boolean displayWarehouseLists() {
        boolean warehouseFound = false;
        warehouseTable.getItems().clear();
        warehouses = fetchAllWarehouse();

        listId.setCellValueFactory(new PropertyValueFactory<>("id"));
        listName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        listCity.setCellValueFactory(new PropertyValueFactory<>("ville"));
        listPlace.setCellValueFactory(new PropertyValueFactory<>("placeLibre"));
        listTotalPlace.setCellValueFactory(new PropertyValueFactory<>("placeTotal"));
        listName.setCellFactory(TextFieldTableCell.forTableColumn());
        // liste combobox entrepot
        IDwarehouse.setCellValueFactory(new PropertyValueFactory<>("entrepotwm"));
        ObservableList<Object> idWarehouse = FXCollections.observableArrayList();
        IDwarehouse.setCellFactory(ComboBoxTableCell.forTableColumn(idWarehouse));
        IDwarehouse.setOnEditCommit((TableColumn.CellEditEvent<Object, Object> e) -> moveProduct((Integer) e.getNewValue()));

        for (int i = 0; i < warehouses.length(); i++) {
            JSONObject warehouse = warehouses.getJSONObject(i);
            Warehouse warehouseElement = jsonToWarehouse(warehouse);

            if(warehouseElement != null){
                warehouseTable.getItems().add(warehouseElement);
                idWarehouse.add(warehouseElement.getId());
                warehouseFound = true;
            }

        }
        return warehouseFound;
    }

    private boolean displayProductsByWarehouse(Integer id) {
        boolean productFound = false;

        productsTable.getItems().clear();
        products = fetchProductsByWarehouse(id);

        productName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        productDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
        productDlc.setCellValueFactory(new PropertyValueFactory<>("dlc"));
        productAvailable.setCellValueFactory(new PropertyValueFactory<>("enRayon"));
        productDate.setCellValueFactory(new PropertyValueFactory<>("dateMiseEnRayon"));
        productQuantity.setCellValueFactory(new PropertyValueFactory<>("quantite"));


        for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            Product productElement = jsonToProduct(product);

            if(productElement != null) {
                productFound = true;
                productsTable.getItems().add(productElement);

            }
        }
        return productFound;
    }

    @FXML
    public void clickItem() {
        try {
            refreshSelectedIndices();

            if (indexOfWarehouseSelected != -1) {
                displayProductsByWarehouse(warehouses.getJSONObject(indexOfWarehouseSelected).getInt("id"));
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    @FXML
    public void validate() {
        try {
            if (swapIdWarehouse != productsTable.getSelectionModel().getSelectedItem().getEntrepotwm()) {
                //updateProduct(productsTable.getSelectionModel().getSelectedItem().getId(), swapIdWarehouse); TODO Switches id of warehouse of product
                init();
                setInfoText("Enregistrement validé :)");
            } else {
                setInfoText("L'enregistrement a echoué :(");
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void refreshSelectedIndices() {
        this.indexOfProductSelected = productsTable.getSelectionModel().getSelectedIndex();
        this.indexOfWarehouseSelected = warehouseTable.getSelectionModel().getSelectedIndex();
    }

    // Return button
    public void displayMainPage() {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance());
    }
}
