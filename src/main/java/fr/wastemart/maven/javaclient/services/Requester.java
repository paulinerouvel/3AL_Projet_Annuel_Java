package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

class Requester {
    static JSONArray sendGetRequest(String route){
        StringBuffer content;

        try {
            // Creating Request
            URL url = new URL("https://wastemart-api.herokuapp.com/"+route);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            Reader streamReader;

            // If failed request, consume stream provided by getErrorStream()
            int status = con.getResponseCode();
            if (status > 299) {
                streamReader = new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8);
            } else {
                streamReader = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8);
            }

            // Parsing Input streamReader
            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            // Closing
            in.close();
            con.disconnect();

        }  catch (IOException e) {
            //Logger.reportError(e);
            return new JSONArray("{null}");
        }

        return new JSONArray(content.toString());
    }

    static Integer sendDeleteRequest(String route){
        StringBuffer content;

        try {
            // Creating Request
            URL url = new URL("https://wastemart-api.herokuapp.com/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            Reader streamReader;

            // If failed request, consume stream provided by getErrorStream()
            int status = con.getResponseCode();
            if (status > 299) {
                return con.getResponseCode();
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
            }

            // Parsing Input streamReader
            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            // Closing
            in.close();
            con.disconnect();

            return con.getResponseCode();

        } catch (IOException e) {
            e.printStackTrace();
            return 503;
        }
    }

    static Integer sendPostRequest(String route, String jsonBody){
        HttpURLConnection http = null;
        try {
            // Creating Request

            URL url = new URL("https://wastemart-api.herokuapp.com/"+route);
            byte[] out = jsonBody.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            // Instantiate connection
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            http = (HttpURLConnection) con;
            ((HttpURLConnection) con).setRequestMethod("POST");

            // Form request, connect and send json
            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }

            return http.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (http != null) {
                    return http.getResponseCode();
                }
                return 299;
            } catch (IOException ex) {
                ex.printStackTrace();
                return 299;
            }
        }

    }

    static Integer sendPutRequest(String route, String jsonBody){
        HttpURLConnection http = null;
        try {
            // Creating Request
            URL url = new URL("https://wastemart-api.herokuapp.com/"+route);
            byte[] out = jsonBody.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            // Instantiate connection
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            http = (HttpURLConnection) con;
            ((HttpURLConnection) con).setRequestMethod("PUT");

            // Form request, connect and send json
            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }

            return http.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (http != null) {
                    return http.getResponseCode();
                }
                return 299;
            } catch (IOException ex) {
                ex.printStackTrace();
                return 299;
            }
        }
    }
}
