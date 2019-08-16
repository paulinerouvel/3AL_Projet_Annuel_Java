package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.models.Product;
import fr.wastemart.maven.javaclient.models.Warehouse;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;

import static fr.wastemart.maven.javaclient.services.Product.fetchProductsByWarehouse;
import static fr.wastemart.maven.javaclient.services.Warehouse.fetchAllWarehouse;
import static fr.wastemart.maven.javaclient.services.Warehouse.updateProductWarehouse;


public class SharedListWarehousesController extends GenericController {
    private UserInstance instance;
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

    public void init(){

        try{
            displayWarehouseLists();
            displayProductsByWarehouse(warehouses.getJSONObject(0).getInt("id"));
            warehouseTable.getSelectionModel().selectFirst();
        }
        catch (Exception ex) {
            System.out.println("Problème init" + ex);
        }
    }

    private void displayWarehouseLists() {

        try {
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
        catch (Exception ex) {
            System.out.println("Pb avec displayWarehouseList" + ex);
        }


    }


    private void displayProductsByWarehouse(Integer id) {
        try {
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

                Product productElement = new Product(product.getInt("id"),
                        product.getString("libelle"),
                        product.getString("desc"),
                        product.getString("photo"),
                        product.getFloat("prix"),
                        product.getFloat("prixInitial"),
                        product.getInt("quantite"),
                        ZonedDateTime.parse(product.getString("dlc")).toLocalDate(),
                        product.getString("codeBarre"),
                        product.getInt("enRayon"),
                        product.getString("dateMiseEnRayon"),
                        product.getInt("categorieProduit_id"),
                        product.getInt("listProduct_id"),
                        product.getInt("entrepotwm_id"),
                        product.getInt("destinataire")
                );

                productsTable.getItems().add(productElement);
            }
        }
        catch(Exception ex) {
            System.out.println("probleme displayProductsByWarehouse " + ex);
        }


    }



    @FXML
    public void clickItem(MouseEvent event) {
        refreshSelectedIndices();

        if(indexOfListSelected != -1){
            displayProductsByWarehouse(warehouses.getJSONObject(indexOfListSelected).getInt("id"));
        }
    }

    @FXML
    public void validate(MouseEvent event) {
        // UPDATE entrepot du produit
        if(swapIdWarehouse != productsTable.getSelectionModel().getSelectedItem().getEntrepotwm()) {
            //update du produit + rechargement
            updateProductWarehouse(productsTable.getSelectionModel().getSelectedItem().getId(), swapIdWarehouse);
            init();
            saveLabel.setTextFill(Color.web("#008000", 1));
            saveLabel.setText("Enregistrement validé :)");
            saveLabel.setVisible(true);
        }
        else {

            saveLabel.setTextFill(Color.web("#ff0000", 1));
            saveLabel.setText("L'enregistrement a echoué :(");
            saveLabel.setVisible(true);
        }
    }

    public void refreshSelectedIndices() {
        this.indexOfProductSelected = productsTable.getSelectionModel().getSelectedIndex();
        this.indexOfListSelected = warehouseTable.getSelectionModel().getSelectedIndex();

    }

    // Return button
    public void displayMainPage(ActionEvent actionEvent) {
        StageManager.getInstance().displayMainPage(instance, actionEvent);
    }

    public UserInstance getInstance() {
        return instance;
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

}
