package controllers;

import javafx.beans.binding.ListBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import models.Order;
import models.Product;
import models.ProductList;
import org.json.JSONArray;
import org.json.JSONObject;
import services.UserInstance;
import models.Warehouse;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static services.Product.deleteProduct;


public class OrderListController {
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
            orders = services.Order.fetchOrder();


            System.out.println("Listdata =" + orders);

            orderId.setCellValueFactory(new PropertyValueFactory<>("id"));
            orderDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            orderUser.setCellValueFactory(new PropertyValueFactory<>("idUser"));

            for (int i = 0; i < orders.length(); i++) {
                JSONObject order = orders.getJSONObject(i);
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
            products = services.Product.fetchProductsByOrder(id);

            System.out.println("Productdata =" + products);

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
