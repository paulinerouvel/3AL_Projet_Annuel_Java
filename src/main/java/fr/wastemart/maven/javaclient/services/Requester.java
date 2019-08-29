package fr.wastemart.maven.javaclient.services;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Requester {
    static HttpResponse sendGetRequest(String route, String token) throws Exception {
        // Creating Request
        URL url = new URL(StageManager.getInstance().getDotenv().get("WASTEMART_API")+route);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("(Requester.sendGetRequest) Sending GET to : "+route);

        // Add request Headers
        con.setRequestMethod("GET");
        if (token != null) {
            System.out.println("(Requester.sendPostRequest) VT!!");
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        return readResponse(con);
    }

    static HttpResponse sendDeleteRequest(String route, String token) throws Exception {
        // Creating Request
        URL url = new URL(StageManager.getInstance().getDotenv().get("WASTEMART_API")+route);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("(Requester.sendDeleteRequest) Sending DELETE to : "+route);

        // Add request Headers
        con.setRequestMethod("DELETE");
        if (token != null) {
            System.out.println("(Requester.sendDeleteRequest) VT!!");
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        return readResponse(con);
    }

    static HttpResponse sendPostRequest(String route, String jsonBody, String token) throws Exception {
        // Creating Request
        URL url = new URL(StageManager.getInstance().getDotenv().get("WASTEMART_API")+route);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Add request Headers
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        if (token != null) {
            System.out.println("(Requester.sendPostRequest) VT!!");
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        sendJson(con, jsonBody);
        return readResponse(con);
    }

    static HttpResponse sendPutRequest(String route, String jsonBody, String token) throws Exception{
        // Creating Request
        URL url = new URL(StageManager.getInstance().getDotenv().get("WASTEMART_API")+route);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Add request Headers
        con.setRequestMethod("PUT");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        if (token != null) {
            System.out.println("(Requester.sendPutRequest) VT!!");
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        sendJson(con, jsonBody);
        return readResponse(con);
    }

    private static HttpResponse readResponse(HttpURLConnection con) throws Exception{
        // If failed request, consume stream provided by getErrorStream()
        Reader streamReader = null;
        int status = con.getResponseCode();

        System.out.println("(Requester.readResponse) Status: "+ status);

        if (status > 299) {
            return new HttpResponse(status, null);
        } else {
            streamReader = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8);
        }

        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        HttpResponse httpResponse = new HttpResponse(status, response.toString());

        in.close();
        con.disconnect();

        System.out.println("(Requester.readResponse) Data Response : ");
        System.out.println("(Requester.readResponse) " + httpResponse.toString());

        return httpResponse;
    }

    private static void sendJson(HttpURLConnection con, String json) throws Exception {
        System.out.println("(Requester.sendJson) About to send : " + json);
        // Form request, connect and send json
        byte[] output = json.getBytes(StandardCharsets.UTF_8);
        int length = output.length;

        con.setFixedLengthStreamingMode(length);
        con.connect();
        try(OutputStream os = con.getOutputStream()) {
            os.write(output);
        } catch (Exception e){
            Logger.getInstance().reportError(e);
        }
    }

    public static HttpResponse sendFile(String url, File file) throws Exception {
        String FileUrl = StageManager.getInstance().getDotenv().get("WASTEMART_WEBSERVER")+url;
        String charset = "UTF-8";
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        URLConnection connection = new URL(FileUrl).openConnection();

        connection.setDoOutput(true);
        connection.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (
                OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true)
        ) {

            // Send text file.
            writer.append(boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(file.getName()).append("\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=").append(charset).append(CRLF); // Text file itself must be saved in this charset!*/
            writer.append(CRLF).flush();
            Files.copy(file.toPath(), output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // End of multipart/form-data.
            writer.append("--").append(boundary).append("--").append(CRLF).flush();
        }

        // Request is lazily fired whenever you need to obtain information about response.
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        System.out.println("(Requester.sendFile) " + responseCode); // Should be 200

        return readResponse((HttpURLConnection) connection);
    }
}
