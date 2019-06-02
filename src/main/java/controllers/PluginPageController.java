package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import services.UserInstance;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static pluginmanager.PluginLoader.getPluginsNames;



public class PluginPageController {

    private StageManager stageManager;
    private UserInstance instance;

    //private String pluginPath = "/mnt/externalHDD/ESGI/Matières/Projet_Annuel/3AL_Java/JavaFX/3AL_ClientJavaFX/JavaFX/src/main/resources/plugins/";
    private String pluginPath = "/run/media/alexandrebis-x220/Elements/ESGI/Matières/Projet_Annuel/3AL_Java/JavaFX/3AL_ClientJavaFX/JavaFX/src/main/resources/plugins/";
    private static String[] localPlugins;
    private static ArrayList<String> onlinePlugins;

    @FXML private ListView<String> localPluginsList;
    @FXML private ListView<String> onlinePluginsList;
    @FXML private Label employeeName;



    public void init(UserInstance instance) {
        try {

            setInstance(instance);
            fetchInstalledPlugins();
            fetchOnlinePlugins();
            employeeName.setText(this.instance.getUser().getNom());

            for (String localPlugin : localPlugins) {
                this.localPluginsList.getItems().add(localPlugin.substring(localPlugin.lastIndexOf("/")+1, localPlugin.length()));
            }

            for (int i = 0; i < onlinePlugins.size(); i++) {
                this.onlinePluginsList.getItems().add(onlinePlugins.get(i));
            }

            if (!localPluginsList.getItems().isEmpty()) {
                localPluginsList.getSelectionModel().select(0);

            }
            if (!onlinePluginsList.getItems().isEmpty()) {
                onlinePluginsList.getSelectionModel().select(0);
            }
        } catch (Exception e) {
            localPluginsList.getItems().add("Error");
        }
    }

    public void installPlugin(ActionEvent actionEvent) throws Exception {
        ObservableList<Integer> selectedIndices = onlinePluginsList.getSelectionModel().getSelectedIndices();

        if(selectedIndices.get(0) >= 0 && selectedIndices.get(0) < onlinePlugins.size()){
            String url = "http://51.75.143.205:8000/" + onlinePlugins.get(selectedIndices.get(0));

            try (BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
                 FileOutputStream fileOS = new FileOutputStream(pluginPath + onlinePlugins.get(selectedIndices.get(0)))) {
                //infoText.setText("Installing : "+ pluginPath + onlinePlugins.get(selectedIndices.get(0)));
                byte data[] = new byte[1024];
                int byteContent;
                while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                    fileOS.write(data, 0, byteContent);
                }
            } catch (IOException e) {
                // handles IO exceptions
            }

            reloadPage(actionEvent);
        }
        else{
            //infoText.setText("Erreur");
        }
    }

    public void uninstallPlugin(ActionEvent actionEvent) throws Exception {
        ObservableList<Integer> selectedIndices = localPluginsList.getSelectionModel().getSelectedIndices();
        System.out.println(selectedIndices);

        File dir = new File(pluginPath);

        final File[] pluginsList = dir.listFiles();

        assert pluginsList != null;
        String choice = String.valueOf(selectedIndices.get(0));

        if(Integer.parseInt(choice) >= 0 && Integer.parseInt(choice) < pluginsList.length) {
            String p = pluginsList[Integer.parseInt(choice)].getPath();

            File pluginToDelete = new File(p);

            if(pluginToDelete.delete()){
                //infoText.setText("Plugin correctement désinstallé");

                reloadPage(actionEvent);
            }
            else{
                //infoText.setText("Le plugin n'a pas pu être correctement désinstallé");
            }

        } else {
            //infoText.setText("Erreur");

        }



    }

    private void fetchInstalledPlugins(){
        try {
            localPlugins = getPluginsNames(pluginPath);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchOnlinePlugins(){
        String urlPlugin = "http://51.75.143.205:8000";
        onlinePlugins = new ArrayList<String>();

        try {
            Document document = Jsoup.connect(urlPlugin).get();

            Elements link = document.select("a[href]");
            for (Element links : link) {
                onlinePlugins.add(links.text());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayMainEmployee(ActionEvent actionEvent) throws Exception {
        stageManager.loadPage(actionEvent,"/views/RootLayout.fxml","/views/MainEmployee.fxml", instance);
    }


    public void disconnect(ActionEvent actionEvent) {
        stageManager.loadLoginPage(actionEvent, instance);
    }

    public UserInstance getInstance() {
        return instance;
    }

    public void setInstance(UserInstance instance) {
        this.instance = instance;
    }

    public StageManager getStageManager() {
        return stageManager;
    }

    public void setStageManager(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    public void reloadPage(ActionEvent actionEvent){
        stageManager.loadPage(actionEvent, "/views/RootLayout.fxml","/views/Plugins.fxml", instance);
    }
}
