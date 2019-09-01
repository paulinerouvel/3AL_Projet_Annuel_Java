package fr.wastemart.maven.javaclient.controllers;

import fr.wastemart.maven.javaclient.services.Logger;
import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.javaclient.services.UserInstance;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.ArrayList;

import static fr.wastemart.maven.pluginmanager.PluginManager.*;

public class GlobalPluginsController extends GenericController {
    private String pluginURL = dotenv.get("WASTEMART_WEBSERVER_PLUGINS");
    @FXML private TextField pluginPath;

    private static File[] localPlugins;
    private static ArrayList<String> onlinePlugins = null;

    @FXML private ListView<String> localPluginsListView;
    @FXML private ListView<String> onlinePluginsListView;

    public void init() { // Ne throw pas car peut être normal
        String configFile = System.getProperty("user.dir")+dotenv.get("DEFAULT_PLUGINS_CONFIGFILE");
        String pluginsFolder = System.getProperty("user.dir")+dotenv.get("DEFAULT_PLUGINS_FOLDER");

        try {
            initialization(pluginsFolder, configFile);

            pluginPath.setText(getPluginFolder());
            pluginPath.positionCaret(pluginPath.getLength());





        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        fetchInstalledPlugins();
        fetchAvailablePlugins();

    }

    public void selectFolder() {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();

            File actualDirectory = new File(pluginPath.getText());
            if(actualDirectory.isDirectory()){
                directoryChooser.setInitialDirectory(actualDirectory);
            } else {
                directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            }

            File selectedDirectory = directoryChooser.showDialog(StageManager.getInstance().getStage());

            if (selectedDirectory != null) {
                pluginPath.setText(selectedDirectory.getAbsolutePath());
            }  // Else No Directory selected

            pluginPath.positionCaret(pluginPath.getLength());

            setPluginFolder(pluginPath.getText());
            fetchInstalledPlugins();
            fetchAvailablePlugins();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void installSelectedPlugin() {
        try {
            setPluginFolder(pluginPath.getText());
            Integer selectedIndice = getSelectedIndex(onlinePluginsListView);

            if (onlinePlugins != null || (selectedIndice >= 0 && selectedIndice < onlinePlugins.size())){
                String pluginName = onlinePlugins.get(selectedIndice);

                installPlugin(pluginURL, pluginName);
                setInfoText("Plug-in téléchargé avec succès");

            } else {
                setInfoText("Sélectionnez un plug-in dans la liste des plugins en ligne");
            }

            fetchInstalledPlugins();
            fetchAvailablePlugins();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoText("Le plug-in n'a pas pu se télécharger, assurez-vous d'avoir assez d'espace disponible");
        }
    }

    public void uninstallSelectedPlugin() {
        try {
            setPluginFolder(pluginPath.getText());


            if(uninstallPlugin(getSelectedIndex(localPluginsListView))) {
                setInfoText("Plug-in désinstallé avec succès");
            } else {
                setInfoText("Sélectionnez un plug-in dans la liste des plugins téléchargés");
            }

            fetchInstalledPlugins();
            fetchAvailablePlugins();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void enablePlugin() {
        try {
            if (getSelectedIndex(localPluginsListView) != -1)
                if (activatePlugin(getSelectedIndex(localPluginsListView))) {
                    setInfoText("Plug-in activé");
                } else {
                    setInfoText("Plug-in déjà activé");
                }
            else {
                setInfoText("Veuillez sélectionner un plug-in");
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    public void disablePlugin() {
        try {
            if (getSelectedIndex(localPluginsListView) != -1)
                if (desactivatePlugin(getSelectedIndex(localPluginsListView))) {
                    setInfoText("Plug-in desactivé");
                } else {
                    setInfoText("Plug-in déjà désactivé");
                }
            else {
                setInfoText("Veuillez sélectionner un plug-in");
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoErrorOccurred();
        }
    }

    private void fetchInstalledPlugins()   {
        Integer previouslySelectedIndex = getSelectedIndex(localPluginsListView);
        localPluginsListView.getItems().clear();

        localPlugins = fetchLocalPlugins();
        if(localPlugins != null) {
            for (File localPlugin : localPlugins) {
                localPluginsListView.getItems().add(localPlugin.getName());
            }

            dynamicallySelectIndex(onlinePluginsListView, onlinePlugins, previouslySelectedIndex);
        }
    }

    private void fetchAvailablePlugins() {
        try {
            Integer previouslySelectedIndex = getSelectedIndex(onlinePluginsListView);

            onlinePlugins = fetchOnlinePlugins(pluginURL);
            if(!onlinePlugins.isEmpty()) {
                onlinePluginsListView.getItems().clear();

                for (String onlinePlugin : onlinePlugins) {
                    this.onlinePluginsListView.getItems().add(onlinePlugin);
                }

                dynamicallySelectIndex(onlinePluginsListView, onlinePlugins, previouslySelectedIndex);
            }

        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            setInfoText("Problème de récupération de la liste des plug-ins disponibles");
        }
    }

    private void dynamicallySelectIndex(ListView<String> ListView, ArrayList<String> Plugins, Integer previousIndex) {
        Integer selectedIndex = getSelectedIndex(ListView);
        if(selectedIndex != -1) {
            if (selectedIndex <= Plugins.size()) {
                ListView.getSelectionModel().select(previousIndex);
            } else {
                ListView.getSelectionModel().select(Plugins.size() - 1);
            }
        }
    }

    private Integer getSelectedIndex(ListView<String> list) {
        return list.getSelectionModel().getSelectedIndex();

    }

    // Return button
    public void displayMainPage() {
        StageManager.getInstance().displayMainPage(UserInstance.getInstance());
    }
}
