package fr.wastemart.maven.javaclient.services;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Logger {
    private File logFile;

    /** Constructeur privé */
    private Logger(){}

    /** Instance unique pré-initialisée */
    private static Logger INSTANCE = new Logger();

    /** Point d'accès pour l'instance unique du Singleton */
    public static Logger getInstance(){
        if (INSTANCE == null){
            INSTANCE = new Logger();
        }
        return INSTANCE;
    }

    public void reportError(Exception ex){
        if(getLogFile() == null) {
            System.out.println("logFile does not exist, creating it...");
            File createdLogFile = createLogFile(ex);
            if(createdLogFile != null){
                setLogFile(createdLogFile);
                System.out.println("File created");
            } else {
                System.out.println("Failed creating File");
                return;
            }
        }

        try {
            System.out.println("About to send it");
            sendLogFile(logFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public File createLogFile(Exception ex) {
        LocalDateTime ldt = LocalDateTime.now();
        String formatedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.FRANCE).format(ldt);

        File file = new File("<"+formatedDate+"> Java : " + ex.getCause());
        try {
            if(file.createNewFile()){
                System.out.println("File created, path is : "+file.getAbsolutePath());
                return file;
            }
            System.out.println("File not created");
            return null;

        } catch (IOException e) {
            return null;
        }
    }

    public void sendLogFile(File logFile) throws Exception{
        String url = "http://51.75.143.205:8080/logs/javaclient";
        String charset = "UTF-8";
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        URLConnection connection = new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; charset=UTF-8");

        try (
                OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
        ) {
            // Send text file.
            writer.append("--").append(boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"textFile\"; filename=\"" + logFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF); // Text file itself must be saved in this charset!
            writer.append(CRLF).flush();
            Files.copy(logFile.toPath(), output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();
        }

        // Request is lazily fired whenever you need to obtain information about response.
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        System.out.println(responseCode); // Should be 200

    }

    public File getLogFile() {
        return logFile;
    }

    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }
}
