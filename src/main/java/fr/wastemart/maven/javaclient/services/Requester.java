package fr.wastemart.maven.javaclient.services;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class Requester {
    static HttpResponse sendGetRequest(String route, String token) throws Exception {
        // Creating Request
        URL url = new URL("https://wastemart-api.herokuapp.com/"+route);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Add request Headers
        con.setRequestMethod("GET");
        if (token != null) {
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        return readResponse(con);
    }

    static HttpResponse sendDeleteRequest(String route, String token) throws Exception { // TODO Test it
        // Creating Request
        URL url = new URL("https://wastemart-api.herokuapp.com/"+route);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Add request Headers
        con.setRequestMethod("DELETE");
        if (token != null) {
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        return readResponse(con);
    }

    static HttpResponse sendPostRequest(String route, String jsonBody, String token) throws Exception {
        // Creating Request
        URL url = new URL("https://wastemart-api.herokuapp.com/"+route);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Add request Headers
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        if (token != null) {
            System.out.println("VT!!");
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        sendJson(con, jsonBody);
        return readResponse(con);
    }

    static HttpResponse sendPutRequest(String route, String jsonBody, String token) throws Exception{
        // Creating Request
        URL url = new URL("https://wastemart-api.herokuapp.com/"+route);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // Add request Headers
        con.setRequestMethod("PUT");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        if (token != null) {
            System.out.println("VT!!");
            con.setRequestProperty("Authorization", "Bearer " + token);
        }

        sendJson(con, jsonBody);
        return readResponse(con);
    }

    private static HttpResponse readResponse(HttpURLConnection con) throws Exception{
        // If failed request, consume stream provided by getErrorStream()
        Reader streamReader = null;
        int status = con.getResponseCode();

        System.out.println("Get status: "+ status);

        if (status > 299) {
            if(con.getErrorStream() != null) {
                new InputStreamReader(con.getErrorStream());
            } else {
                return new HttpResponse(status, null);
                //Logger.reportMessage("Response code : "+status);
            }
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

        System.out.println("Response : ");
        System.out.println(httpResponse.toString());

        return httpResponse;
    }

    private static void sendJson(HttpURLConnection con, String json) throws Exception {
        // Form request, connect and send json
        byte[] output = json.getBytes(StandardCharsets.UTF_8);
        int length = output.length;

        con.setFixedLengthStreamingMode(length);
        con.connect();
        try(OutputStream os = con.getOutputStream()) {
            os.write(output);
        } catch (Exception e){
            //Logger.reportError(e);
        }
    }
}
