package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import services.UserInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ProductListController {
    private UserInstance instance;
    private JSONArray lists;

    @FXML TableColumn productLists;
    @FXML TableColumn numberInProductLists;
    @FXML TableColumn productName;
    @FXML TableColumn productDesc;
    @FXML TableColumn productPrice;
    @FXML TableColumn productDlc;
    @FXML TableColumn productAvailable;
    @FXML TableColumn productDate;

    public void init(){
        displayProductLists();
        displayProductDetails();
    }

    private void displayProductLists() {
        lists = fetchLists(instance.getUser().getId());

        ArrayList<String> listdata = new ArrayList<>();
        JSONArray jArray = lists;
        for (int i=0;i<jArray.length();i++){
            listdata.add(jArray.get(i).toString());
        }
    }

    private JSONArray fetchLists(Integer idUser) {
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/list?idUser=" + idUser);
            // /list/products?id=1

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            System.out.println(status);
            Reader streamReader;
            if (status > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
            }

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            con.disconnect();

            return new JSONArray(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray("{null}");
        }
    }

    private void displayProductDetails() {
    }

    public UserInstance getInstance() {
        return instance;
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

    // Return button
    public void displayMainPage(ActionEvent actionEvent) throws Exception {
        StageManager.displayMainPage(instance, actionEvent);
    }
}
