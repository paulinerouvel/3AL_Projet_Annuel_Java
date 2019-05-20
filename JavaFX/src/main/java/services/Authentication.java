package services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;


public class Authentication {

    public String login(String login, String password) throws ClientProtocolException, IOException {

        CloseableHttpClient client = null;
        try {
            // Form url and json for login
            URL url = new URL("https://wastemart-api.herokuapp.com/user/login");
            String jsonBody =  "{\"mail\":\""+ login +"\",\"mdp\":\""+ password +"\"}";
            byte[] out = jsonBody.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            // Instantiate connection
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            HttpURLConnection http = (HttpURLConnection) con;

            // Form request, connect and send json
            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }

            // Get the input stream returned
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String inputLine;
            StringBuilder buffReader = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                buffReader.append(inputLine);
            }

            in.close();

            // Form returned token and verify it
            JSONObject obj = new JSONObject(buffReader.toString());
            return obj.getString("token");

        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            return e.toString();
        }
    }
}
