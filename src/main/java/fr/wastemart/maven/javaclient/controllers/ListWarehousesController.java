package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.models.Warehouse;
import fr.wastemart.maven.javaclient.services.Details.Detail;
import fr.wastemart.maven.javaclient.services.Details.StringDetail;
import fr.wastemart.maven.javaclient.services.Details.WarehouseDetail;
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

import java.util.ArrayList;
import java.util.List;

import static fr.wastemart.maven.javaclient.services.Product.*;
import static fr.wastemart.maven.javaclient.services.Warehouse.*;


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
        refreshDisplay();
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
    public void displayAddWarehouse() {
        clearInfoText();
        refreshSelectedIndices();

        try{
            List<Detail> details = new ArrayList<Detail>();

            details.add(new StringDetail("add"));
            StageManager.getInstance().loadExtraPageWithDetails(dotenv.get("SHARED_DETAILS_WAREHOUSE"), details);

            refreshDisplay();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }

    }

    @FXML
    public void displayModifyWarehouse() {
        clearInfoText();
        refreshSelectedIndices();

        try {
            Warehouse selectedWarehouse = jsonToWarehouse(warehouses.getJSONObject(indexOfWarehouseSelected));

            List<Detail> details = new ArrayList<Detail>();

            details.add(new StringDetail("modify"));
            details.add(new WarehouseDetail(selectedWarehouse));
            StageManager.getInstance().loadExtraPageWithDetails(dotenv.get("SHARED_DETAILS_WAREHOUSE"), details);

            refreshDisplay();
        } catch (Exception e){
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    @FXML
    public void deleteWarehouse() {
        clearInfoText();
        refreshSelectedIndices();

        if (indexOfWarehouseSelected != -1) {
            JSONArray productList = fetchProductsByWarehouse(warehouseTable.getSelectionModel().getSelectedItem().getId());

            if(!productList.isEmpty()){
                for(int i = 0; i < productList.length(); i ++) {
                    Product product = jsonToProduct((JSONObject) productList.get(i));
                    if(product != null){
                        product.setEntrepotwm(null);
                        if(updateProduct(product,UserInstance.getInstance().getTokenValue())) {
                            setInfoText("Un ou plusieurs produits n'ont pas pu être supprimés");
                        }
                    }

                }
            }

            if(removeWarehouse(warehouseTable.getSelectionModel().getSelectedItem().getId(), UserInstance.getInstance().getTokenValue())){
                setInfoText("Produit supprimé de la liste");
            } else {
                setInfoErrorOccurred();

            }
            refreshDisplay();
        } else {
            setInfoText("Veuillez sélectionner un entrepôt");
        }
    }

    @FXML
    public void moveProduct(Integer newWarehouseId) {
        clearInfoText();
        refreshSelectedIndices();

        if (indexOfProductSelected != -1) {
            if (newWarehouseId != -1) {
                Product productToSwitch = productsTable.getSelectionModel().getSelectedItem();
                productToSwitch.setEntrepotwm(newWarehouseId);

                if (updateProduct(productToSwitch, UserInstance.getInstance().getTokenValue())) { // TODO Switches id of warehouse of product
                    setInfoText("Produit changé d'entrepôt");
                    refreshDisplay();
                } else {
                    setInfoErrorOccurred();
                }

            }
        } else {
            setInfoText("Veuillez sélectionner un produit");
        }
    }

    @FXML
    public void deleteProduct() {
        clearInfoText();
        refreshSelectedIndices();

        if (indexOfWarehouseSelected != -1 && indexOfProductSelected != -1) {
            Product productToRemove = productsTable.getSelectionModel().getSelectedItem();

            if(fr.wastemart.maven.javaclient.services.Product.deleteProduct(productToRemove.getId(), UserInstance.getInstance().getTokenValue())){
                setInfoText("Produit supprimé de l'entrepôt");
                refreshDisplay();
            } else {
                setInfoErrorOccurred();
            }
        } else {
            setInfoText("Veuillez sélectionner un entrepôt et un produit");
        }
    }



    @FXML
    private void refreshDisplay() {
        if(displayWarehouseLists()) {
            if (displayProductsByWarehouse(warehouses.getJSONObject(0).getInt("id"))) {
                warehouseTable.getSelectionModel().selectFirst();
            }
        }
    }

    public void refreshSelectedIndices() {
        this.indexOfProductSelected = productsTable.getSelectionModel().getSelectedIndex();
        this.indexOfWarehouseSelected = warehouseTable.getSelectionModel().getSelectedIndex();
    }

    private boolean displayWarehouseLists() {
        boolean result = false;
        warehouseTable.getItems().clear();
        warehouses = fetchAllWarehouse();

        if(warehouses != null) {
            result = fillWarehouseLists();
        }
        return result;
    }

    private boolean displayProductsByWarehouse(Integer id) {
        boolean result = false;
        productsTable.getItems().clear();
        products = fetchProductsByWarehouse(id);

        if(products != null){
            result = fillProducts();
        }

        return result;
    }

    private boolean fillWarehouseLists() {
        if(warehouses != null) {
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
                Warehouse warehouse = jsonToWarehouse(warehouses.getJSONObject(i));
                if(warehouse != null) {
                    warehouseTable.getItems().add(warehouse);
                    idWarehouse.add(warehouse.getId());

                } else {
                    setInfoText("Un ou plusieurs entrepôts n'ont pas pu être récupérés");

                }

            }
            return true;
        } else {
            setInfoText("Aucun entrepôt récupéré");
            return false;
        }
    }

    private boolean fillProducts() {
        if(!products.isEmpty()) {
            productName.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            productDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
            productPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
            productDlc.setCellValueFactory(new PropertyValueFactory<>("dlc"));
            productQuantity.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            productAvailable.setCellValueFactory(new PropertyValueFactory<>("enRayon"));
            productDate.setCellValueFactory(new PropertyValueFactory<>("dateMiseEnRayon"));

            for (int i = 0; i < products.length(); i++) {
                Product product = jsonToProduct(products.getJSONObject(i));
                if(product != null) {
                    productsTable.getItems().add(product);
                } else {
                    setInfoText("Un ou plusieurs produits n'ont pas pu être récupérés");
                }
            }
            return true;
        } else {
            setInfoText("L'entrepôt est vide !");
            return false;
        }
    }

    // Return button
    public void displayMainPage() {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance());
    }
}
