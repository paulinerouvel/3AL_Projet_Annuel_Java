package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import fr.wastemart.maven.javaclient.services.UserInstance;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static fr.wastemart.maven.javaclient.pluginmanager.PluginLoader.getPluginsNames;


public class SharedListPluginsController extends GenericController {
    @FXML private TextField pluginPath;

    private static String[] localPlugins;
    private static ArrayList<String> onlinePlugins;

    @FXML private ListView<String> localPluginsList;
    @FXML private ListView<String> onlinePluginsList;


    public void init() {
        try {
            pluginPath.setText("/mnt/externalHDD/ESGI/Matières/Projet_Annuel/3AL_Java/JavaFX/3AL_ClientJavaFX/newJavaFX/JavaFX/src/main/resources/plugins");
            fetchInstalledPlugins();
            fetchOnlinePlugins();

        } catch (Exception e) {
            localPluginsList.getItems().add("Error");
        }
    }

    public void selectFolder(ActionEvent actionEvent) {
        Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stageNodeRoot);

        if(selectedDirectory == null){
            //No Directory selected
        }else{
            pluginPath.setText(selectedDirectory.getAbsolutePath());
            System.out.println(selectedDirectory.getAbsolutePath());
        }
        fetchInstalledPlugins();
        fetchOnlinePlugins();
    }

    public void installPlugin(ActionEvent actionEvent) throws Exception {
        ObservableList<Integer> selectedIndices = onlinePluginsList.getSelectionModel().getSelectedIndices();

        if(selectedIndices.get(0) >= 0 && selectedIndices.get(0) < onlinePlugins.size()){
            String url = "http://51.75.143.205:8000/" + onlinePlugins.get(selectedIndices.get(0));

            try (BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
                 FileOutputStream fileOS = new FileOutputStream(pluginPath.getText() + "/" + onlinePlugins.get(selectedIndices.get(0)))) {
                //infoText.setText("Installing : "+ pluginPath + onlinePlugins.get(selectedIndices.get(0)));
                byte data[] = new byte[1024];
                int byteContent;
                while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                    fileOS.write(data, 0, byteContent);
                }
            } catch (IOException e) {
                // handles IO exceptions
            }

            fetchInstalledPlugins();
            fetchOnlinePlugins();
            //reloadPage(actionEvent);
        }
        else{
            //infoText.setText("Erreur");
        }
    }

    private void fetchInstalledPlugins(){
        localPluginsList.getItems().clear();

        try {
            localPlugins = getPluginsNames(pluginPath.getText());

            for (String localPlugin : localPlugins) {
                this.localPluginsList.getItems().add(localPlugin.substring(localPlugin.lastIndexOf("/")+1, localPlugin.length()));
            }
            if (!localPluginsList.getItems().isEmpty()) {
                localPluginsList.getSelectionModel().select(0);

            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public void uninstallPlugin(ActionEvent actionEvent) throws Exception {
        ObservableList<Integer> selectedIndices = localPluginsList.getSelectionModel().getSelectedIndices();
        System.out.println(selectedIndices);

        File dir = new File(pluginPath.getText());

        final File[] pluginsList = dir.listFiles();

        assert pluginsList != null;
        String choice = String.valueOf(selectedIndices.get(0));

        if(Integer.parseInt(choice) >= 0 && Integer.parseInt(choice) < pluginsList.length) {
            String p = pluginsList[Integer.parseInt(choice)].getPath();

            File pluginToDelete = new File(p);

            if(pluginToDelete.delete()){
                //infoText.setText("Plugin correctement désinstallé");
                fetchInstalledPlugins();
                fetchOnlinePlugins();
                //reloadPage(actionEvent);
            }
            else{
                //infoText.setText("Le plugin n'a pas pu être correctement désinstallé");
            }

        } else {
            //infoText.setText("Erreur");

        }
    }

    private void fetchOnlinePlugins(){
        String urlPlugin = "http://51.75.143.205:8000";
        onlinePlugins = new ArrayList<String>();
        onlinePluginsList.getItems().clear();
        try {
            Document document = Jsoup.connect(urlPlugin).get();

            Elements link = document.select("a[href]");
            for (Element links : link) {
                onlinePlugins.add(links.text());
            }

            for (String onlinePlugin : onlinePlugins) {
                this.onlinePluginsList.getItems().add(onlinePlugin);
            }

            if (!onlinePluginsList.getItems().isEmpty()) {
                onlinePluginsList.getSelectionModel().select(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void enablePlugin(ActionEvent actionEvent) throws Exception {}
    public void disablePlugin(ActionEvent actionEvent) throws Exception {}

    public void reloadPage(ActionEvent actionEvent){
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedListPlugins.fxml", UserInstance.getInstance());
    }

    // Return button
    public void displayMainPage(ActionEvent actionEvent) {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance(), actionEvent);
    }
}
