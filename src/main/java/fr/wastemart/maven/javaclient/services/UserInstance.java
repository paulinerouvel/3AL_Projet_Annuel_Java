package fr.wastemart.maven.javaclient.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.wastemart.maven.javaclient.models.User;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class UserInstance {

    public boolean isConnected = false;
    private JSONObject token;
    private User user;

    public boolean tokenIsValid() throws JWTVerificationException {
        String token = getTokenValue();
        try {
            Algorithm algorithm = Algorithm.HMAC256(" SFGQDFB54QSDF5G4W5XV43QGREgdfg54214542sdf24242sf424bjksgdfsqfgZR");
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptLeeway(30)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            //Date expiresAt = jwt.getExpiresAt();

            return true;
        } catch (JWTVerificationException exception) {
            System.out.println("Token is incorrect!\n" +
                    "Time is : "+ java.time.LocalDateTime.now() +"\n" +
                    "Exception : "+exception);
            return false;
        }
    }

    public void initUser(){
        JSONObject fetchedUser = fr.wastemart.maven.javaclient.services.User.fetchUser("id", String.valueOf(token.getInt("userId")));

        //Integer fetchedUserCategory = fr.wastemart.maven.javaclient.services.User.fetchCategory(fetchedUser.getInt("id"));
        //JSONObject fetchedCategory = new JSONObject();
        //fetchedCategory.put("Category_user_id", "2");

        //fetchedUser.put("userCategory", fetchedUserCategory);
        setUser(fr.wastemart.maven.javaclient.services.User.jsonToUser(fetchedUser));
    }

    // Login (Post)
    public JSONObject login(String login, String password){
        HttpURLConnection http = null;
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
            http = (HttpURLConnection) con;

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

            System.out.println(new JSONObject(buffReader.toString()));
            // Form returned token and verify it
            return new JSONObject(buffReader.toString());

        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (http != null && http.getResponseCode() > 299) {
                    return new JSONObject("{\"error\":" + http.getResponseCode() + "}");
                } else {
                    return new JSONObject("{\"error\":\"internal\"}");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return new JSONObject("{\"error\":\"internal\"}");
            }
        }
    }

    /*public void setCategorieUtilisateur(JSONObject userCategory){
        this.user.setLibelle(userCategory.getString("libelle"));
    }*/




    public void disconnect(){
            this.setToken(null);
    }

    public String getTokenValue() {
        return token.getString("token");
    }

    public Integer getTokenUserId() {
        return token.getInt("userId");
    }

    public Integer getTokenUserCategory() { return token.getInt("userCategory"); }

    public void setToken(JSONObject token) {
        this.token = token;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean Connected) {
        isConnected = Connected;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}