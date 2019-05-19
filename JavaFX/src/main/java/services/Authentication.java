package services;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;


public class Authentication {

    public String whenPostJsonUsingHttpClient_thenCorrect(String login, String password) throws ClientProtocolException, IOException {

        CloseableHttpClient client = null;
        try {
            URL url = new URL("https://wastemart-api.herokuapp.com/user/login");

            String jsonBody =  "{\"mail\":\""+ login +"\",\"mdp\":\""+ password +"\"}";
            byte[] out = jsonBody.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            HttpURLConnection http = (HttpURLConnection) con;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }

            // get the input stream of the process and print it
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));

            String inputLine;
            StringBuilder buffReader = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                buffReader.append(inputLine);
            }

            in.close();



            JSONObject obj = new JSONObject(buffReader.toString());
            String token = obj.getString("token");

            return token;

        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            return "Internal error";
        }
    }
}
