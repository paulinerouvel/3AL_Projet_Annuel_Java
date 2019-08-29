package fr.wastemart.maven.javaclient.services;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Logger {
    private File logFile;

    /** Constructeur privé */
    private Logger(){
        try {
            sendOfflineLogFile();
        } catch (Exception e){
            // TODO Fail, will retry in a few minutes
        }
    }

    /** Instance unique */
    private static Logger INSTANCE;

    /** Point d'accès pour l'instance unique du Singleton */
    public static Logger getInstance(){
        if (INSTANCE == null){
            INSTANCE = new Logger();
        }
        return INSTANCE;
    }

    public void reportError(Exception ex){
        ex.printStackTrace();
        if(getLogFile() == null) {
            System.out.println("(Logger.reportError) logFile does not exist, creating it...");
            File createdLogFile = createLogFile(ex);
            if(createdLogFile != null){
                setLogFile(createdLogFile);
            } else {
                System.out.println("(Logger.reportError) Failed creating File");
                return;
            }

        }

        writeErrorInLogFile(ex);

        try {
            sendLogFile(logFile);
        } catch (Exception e) {
            System.out.println("(Logger.reportError) Failed to send, will send later");
            getLogFile().renameTo(new File("offline_" + getLogFile().getName()));
        }
    }


    private File createLogFile(Exception ex) {
        LocalDateTime ldt = LocalDateTime.now();
        String formatedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss", Locale.FRANCE).format(ldt);
        System.out.println("(Logger.createLogFile) Date formated");
        File file = new File(formatedDate + "-Java-" + ex.getCause() + ".txt");
        try {
            if(file.createNewFile()){
                System.out.println("(Logger.createLogFile) File created, path is : "+file.getAbsolutePath());
                return file;
            }
            System.out.println("(Logger.createLogFile) File not created");
            return null;

        } catch (IOException e) {
            System.out.println("(Logger.createLogFile) IOException error during file creation, formatedDate = "+formatedDate);
            return null;
        }
    }

    private void writeErrorInLogFile(Exception ex) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(getLogFile(),true));
            writer.write( LocalDateTime.now() +" : "+ ex.getMessage() + "\nExecution Detail :\n");

            for(int i = 0; i < ex.getStackTrace().length; i += 1){
                writer.write("Line "+ex.getStackTrace()[i].getLineNumber() +
                        " in File \"" + ex.getStackTrace()[i].getFileName() + "\"" +
                        " -> \"" + ex.getStackTrace()[i].getMethodName() +"\" method\n");
            }
            writer.write(System.getProperty("line.separator"));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean sendLogFile(File logFile) {
        Integer result = 299;

        try {
            result = Requester.sendFile("logs/javaclient/", logFile).getResponseCode();
        } catch (Exception ignored) {

        }

        return result < 299;
    }

    private void sendOfflineLogFile() throws Exception {
        //TODO
        // Récupérer chaque fichiers avec offline_ dans le nom
        //       for(File file in files){
        //           sendLogFile(file);
        //       }
        // Une fois tout envoyé, changer le nom enlever offline_
    }

    private File getLogFile() {
        return logFile;
    }

    private void setLogFile(File logFile) {
        this.logFile = logFile;
    }
}
