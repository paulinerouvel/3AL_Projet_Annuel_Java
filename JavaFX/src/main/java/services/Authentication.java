package services;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

public class Authentication {

    public String whenPostJsonUsingHttpClient_thenCorrect(String login, String password) throws ClientProtocolException, IOException {

        CloseableHttpClient client = null;
        try {
            client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://wastemart-api.herokuapp.com/user/login");

            StringEntity params = new StringEntity("{"+
                    "\"mail\":\"testemail@email.com\","+
                    "\"mdp\":\"toto\"}");

            httpPost.setHeader("accept", "application/json");
            httpPost.setHeader("content-type", "application/json");
            httpPost.setEntity(params);

            CloseableHttpResponse response = client.execute(httpPost);
            client.close();

            //TODO Renvoyer le code directement ou le Token
            return Integer.toString(response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            return "Internal error";
        }
    }
}
