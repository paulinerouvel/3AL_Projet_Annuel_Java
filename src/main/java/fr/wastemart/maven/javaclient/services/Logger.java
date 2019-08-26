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

    private void sendLogFile(File logFile) throws Exception{
        System.out.println("(Logger.sendLogFile) Sending file");
        String url = "http://51.75.143.205:8080/logs/javaclient/";
        String charset = "UTF-8";
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        URLConnection connection = new URL(url).openConnection();

        connection.setDoOutput(true);
        connection.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (
                OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true)
        ) {

            // Send text file.
            writer.append(boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(logFile.getName()).append("\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=").append(charset).append(CRLF); // Text file itself must be saved in this charset!*/
            writer.append(CRLF).flush();
            Files.copy(logFile.toPath(), output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // End of multipart/form-data.
            writer.append("--").append(boundary).append("--").append(CRLF).flush();
        }

        // Request is lazily fired whenever you need to obtain information about response.
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        ((HttpURLConnection) connection).disconnect();
        System.out.println("(Logger.sendLogFile) " + responseCode); // Should be 200

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
