package fr.wastemart.maven.javaclient;

import fr.wastemart.maven.javaclient.services.StageManager;
import fr.wastemart.maven.pluginmanager.PluginManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

import static fr.wastemart.maven.pluginmanager.PluginManager.*;

public class MainApp extends Application {
    /**
     * Calls start() internally
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Entry point, gets primaryStage created by JavaFX
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("WasteMart");
        URL icon = new File(System.getProperty("user.dir")+"/src/main/resources/fr.wastemart.maven.javaclient/images/WasteMartIcon.png").toURI().toURL();
        primaryStage.getIcons().add(new Image(String.valueOf(icon)));
        primaryStage.setResizable(false);

        StageManager.getInstance().setStage(primaryStage);
        StageManager.getInstance().loadLoginPage(null);


        String configFile = System.getProperty("user.dir")+"/activatedPlugins.conf";
        String pluginsFolder = System.getProperty("user.dir")+"/plugins";

        if(new File(configFile).isFile() && new File(pluginsFolder).isDirectory()){
            PluginManager.initialization(configFile, pluginsFolder);
        }
    }
}