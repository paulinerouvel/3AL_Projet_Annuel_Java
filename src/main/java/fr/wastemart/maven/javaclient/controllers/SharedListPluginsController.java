package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static fr.wastemart.maven.javaclient.pluginmanager.PluginLoader.getPluginsNames;
import static fr.wastemart.maven.javaclient.pluginmanager.PluginManager.*;


public class SharedListPluginsController extends GenericController {
    private String pluginURL = "http://51.75.143.205:8080/plugins/";
    @FXML private TextField pluginPath;

    private static String[] localPlugins;
    private static ArrayList<String> onlinePlugins = null;

    @FXML private ListView<String> localPluginsListView;
    @FXML private ListView<String> onlinePluginsListView;

    public void init() { // Ne throw pas car peut être normal
        pluginPath.setText("");
        fetchInstalledPlugins();
        fetchAvailablePlugins();
    }

    public void selectFolder(ActionEvent actionEvent) {
        try {
            Stage stageNodeRoot = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(stageNodeRoot);

            if (selectedDirectory != null) {
                pluginPath.setText(selectedDirectory.getAbsolutePath());
            }  // Else No Directory selected

            fetchInstalledPlugins();
            fetchAvailablePlugins();
        } catch (Exception e) {
            //Logger.reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void installSelectedPlugin(ActionEvent actionEvent) {
        try {
            setPath(pluginPath.getText());

            Integer selectedIndice = getSelectedIndex(onlinePluginsListView);

            if (onlinePlugins != null || (selectedIndice >= 0 && selectedIndice < onlinePlugins.size())){
                String pluginName = onlinePlugins.get(selectedIndice);

                if(installPlugin(pluginURL, pluginName)){
                    setInfoText("Plug-in téléchargé avec succès");
                } else {
                    setInfoText("Le plug-in n'a pas pu se télécharger, assurez-vous d'avoir assez d'espace disponible");
                }
            } else {
                setInfoText("Sélectionnez un plug-in dans la liste des plugins en ligne");
            }

            fetchInstalledPlugins();
            fetchAvailablePlugins();
        } catch (Exception e) {
            //Logger.reportError(e);
            e.printStackTrace();
            setInfoErrorOccurred();
        }
    }

    public void uninstallSelectedPlugin(ActionEvent actionEvent) {
        try {
            setPath(pluginPath.getText());

            System.out.println("Selected indice: "+ getSelectedIndex(localPluginsListView));
            if(uninstallPlugin(getSelectedIndex(localPluginsListView))) {
                setInfoText("Plug-in désinstallé avec succès");
            } else {
                setInfoText("Sélectionnez un plug-in dans la liste des plugins téléchargés");
            }

            fetchInstalledPlugins();
            fetchAvailablePlugins();
        } catch (Exception e) {
            //Logger.reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void enablePlugin(ActionEvent actionEvent) {
        try {
            if(activatePlugin(localPlugins, getSelectedIndex(localPluginsListView))){
                setInfoText("Plug-in activé");
            } else {
                setInfoText("Plug-in déjà activé");
            }
        } catch (Exception e) {
            //Logger.reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void disablePlugin(ActionEvent actionEvent) {}

    private void fetchInstalledPlugins() {
        try {
            Integer previouslySelectedIndex = getSelectedIndex(localPluginsListView);
            localPluginsListView.getItems().clear();

            localPlugins = getPluginsNames(pluginPath.getText());
            for (String localPlugin : localPlugins) {
                this.localPluginsListView.getItems().add(localPlugin.substring(localPlugin.lastIndexOf("/") + 1, localPlugin.length()));
            }

            dynamicallySelectIndex(onlinePluginsListView, onlinePlugins, previouslySelectedIndex);
        } catch (Exception e) {
            //Logger.reportError(e);
            setInfoText("Veuillez sélectionner un dossier valide");
        }
    }

    private void fetchAvailablePlugins() {
        try {
            Integer previouslySelectedIndex = getSelectedIndex(onlinePluginsListView);
            onlinePlugins = fetchOnlinePlugins(pluginURL);

            onlinePlugins = new ArrayList<String>();
            onlinePluginsListView.getItems().clear();
            Document document = Jsoup.connect(pluginURL).get();

            Elements link = document.select("a[href]");
            for (Element links : link) {
                onlinePlugins.add(links.text());
            }

            for (String onlinePlugin : onlinePlugins) {
                this.onlinePluginsListView.getItems().add(onlinePlugin);
            }

            dynamicallySelectIndex(onlinePluginsListView, onlinePlugins, previouslySelectedIndex);

        } catch (Exception e) {
            //Logger.reportError(e);
            setInfoText("Problème de récupération de la liste des plug-ins disponibles");
        }
    }

    private void dynamicallySelectIndex(ListView<String> ListView, ArrayList<String> Plugins, Integer previousIndex) {

        if (!ListView.getItems().isEmpty() || getSelectedIndex(ListView) > Plugins.size()) {
            ListView.getSelectionModel().select(Plugins.size()-1);
        } else {
            ListView.getSelectionModel().select(previousIndex);

        }
    }

    public void reloadPage(ActionEvent actionEvent){
        StageManager.getInstance().loadPage(actionEvent, "/fr.wastemart.maven.javaclient/views/SharedListPlugins.fxml", UserInstance.getInstance());
    }

    private Integer getSelectedIndex(ListView<String> list) {
        return list.getSelectionModel().getSelectedIndex();

    }

    // Return button
    public void displayMainPage(ActionEvent actionEvent) {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance(), actionEvent);
    }
}
