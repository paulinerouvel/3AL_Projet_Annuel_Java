package fr.wastemart.maven.javaclient.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import fr.wastemart.maven.javaclient.models.Order;
import fr.wastemart.maven.javaclient.models.Product;
import org.json.JSONArray;
import org.json.JSONObject;
import fr.wastemart.maven.javaclient.services.UserInstance;

import java.time.ZonedDateTime;

import static fr.wastemart.maven.javaclient.services.Order.fetchOrder;
import static fr.wastemart.maven.javaclient.services.Order.fetchProductsByOrder;
import static fr.wastemart.maven.javaclient.services.Order.jsonToOrder;


public class OrderListController extends GenericController {
    private UserInstance instance;
    private JSONArray orders;
    private JSONArray products;
    private Integer indexOfProductSelected;
    private Integer indexOfListSelected;
    private Integer swapIdWarehouse;


    // order
    @FXML TableView<Order> ordersTable;
    @FXML TableColumn<Object, Object> orderId;
    @FXML TableColumn<Object, Object> orderDate;
    @FXML TableColumn<Object, Object> orderUser;
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

    @FXML
    Label saveLabel;

    public void init(){
        try{
            displayOrderList();
            displayProductsByOrder(orders.getJSONObject(0).getInt("id"));
            ordersTable.getSelectionModel().selectFirst();
        }
        catch (Exception ex) {
            System.out.println("Problème init" + ex);
        }
    }

    private void displayOrderList() {

        try {
            ordersTable.getItems().clear();
            orders = fetchOrder();

            orderId.setCellValueFactory(new PropertyValueFactory<>("id"));
            orderDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            orderUser.setCellValueFactory(new PropertyValueFactory<>("userId"));

            for (int i = 0; i < orders.length(); i++) {
                JSONObject order = orders.getJSONObject(i);
                //Order orderElement = jsonToOrder(order);
                Order orderElement = new Order(order.getInt("id"),
                        ZonedDateTime.parse(order.getString("date")).toLocalDate(),
                        order.getInt("utilisateurID")
                );

                ordersTable.getItems().add(orderElement);

            }

        }
        catch (Exception ex) {
            System.out.println("Pb avec displayOrderList" + ex);
        }


    }


    private void displayProductsByOrder(Integer id) {
        try {
            productsTable.getItems().clear();
            products = fetchProductsByOrder(id);

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
            System.out.println("probleme displayProductsByOrder " + ex);
        }
    }

    @FXML
    public void clickItem(MouseEvent event) {
        refreshSelectedIndices();

        if(indexOfListSelected != -1){
            displayProductsByOrder(orders.getJSONObject(indexOfListSelected).getInt("id"));
        }
    }

    public void refreshSelectedIndices() {
        this.indexOfProductSelected = productsTable.getSelectionModel().getSelectedIndex();
        this.indexOfListSelected = ordersTable.getSelectionModel().getSelectedIndex();

    }

    // Return button
    public void displayMainPage(ActionEvent actionEvent) throws Exception {
        StageManager.displayMainPage(instance, actionEvent);
    }

    public UserInstance getInstance() {
        return instance;
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

}