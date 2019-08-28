package fr.wastemart.maven.javaclient.services;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class Requester {
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
}
