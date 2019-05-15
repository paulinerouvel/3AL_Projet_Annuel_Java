package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.NodeParkUser;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;



public class Authentication {

    public String whenPostJsonUsingHttpClient_thenCorrect(String login, String password) throws ClientProtocolException, IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api-node-parc.herokuapp.com/utilisateur/login");

        StringEntity params = new StringEntity("{"+/*'id':0," +
                "'nom':''" +
                "'prenom':''" +
                "'date_de_naissance':''" +
                "'tel':''" +*/
                "'mail':'"+ login +"'" +
                /*"'adresse':''" +
                "'cp':''" +
                "'ville':''" +
                "'type':''" +*/
                "'mot_de_passe':'"+ password +"'}");

        httpPost.setHeader("accept", "application/json");
        httpPost.setHeader("content-type", "application/json");
        httpPost.setEntity(params);

        CloseableHttpResponse response = client.execute(httpPost);
        client.close();

        return Integer.toString(response.getStatusLine().getStatusCode());


    }
    public String Authenticate(){
        try {
            System.out.println("Yo");
            URL url = new URL("https://api-node-parc.herokuapp.com/login");

            NodeParkUser newUser;
            newUser = new NodeParkUser(1, "CHAMPAUD", "Alexandre", "13/10/1998",
                    "0612345678", "analyse@hotmail.fr", "27 rue test", "92100", "Asnières",
                    "Utilisateur", "anna");

            final GsonBuilder builder = new GsonBuilder();
            final Gson gson = builder.create();
            final String jsonUser = gson.toJson(newUser);

            HttpURLConnection urlConn;
            urlConn = (HttpURLConnection) url.openConnection();

            //Query
            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("accept", "application/json");
            urlConn.setRequestProperty("content-type", "application/json");
            //urlConn.addRequestProperty("content-Type", "application/" + "POST");
            if (jsonUser != null) {
                urlConn.setRequestProperty("content-length", Integer.toString(jsonUser.length()));
                urlConn.setDoOutput(true); // moved here
                urlConn.getOutputStream().write(jsonUser.getBytes("UTF8"));
            }

            DataOutputStream wr = new DataOutputStream(urlConn.getOutputStream());
            //wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int status = urlConn.getResponseCode();

            Reader streamReader = null;

            if (status > 299) {
                streamReader = new InputStreamReader(urlConn.getErrorStream());
                System.out.println(streamReader);
            } else {
                streamReader = new InputStreamReader(urlConn.getInputStream());
                System.out.println(streamReader);
            }

            // Reading response
            int responseCode = urlConn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConn.getInputStream())
            );

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());

            return "Win";
        } catch (IOException e) {
            e.printStackTrace();
            return "Lose";
        }

    }

}
