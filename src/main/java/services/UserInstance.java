package services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import models.User;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

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

    public void initUser() {
        JSONObject user = fetchUser();
        setUser(user);
    }

    public JSONObject fetchUser() {
        try {
            URL url = new URL("https://wastemart-api.herokuapp.com/user/?id="+ getTokenUserId().toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();

            Reader streamReader;
            if (status > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
            }

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            con.disconnect();

            return new JSONObject(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject("{null}");
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(JSONObject user) {
        //System.out.println(user);
        this.user = new User();
        this.user.setId(user.getInt("id"));
        this.user.setLibelle("employee");
        this.user.setNom(user.getString("nom"));
        this.user.setPrenom(user.getString("prenom"));
        this.user.setMail(user.getString("mail"));
        this.user.setTel(user.getString("tel"));
        this.user.setAdresse(user.getString("adresse"));
        this.user.setVille(user.getString("ville"));
        this.user.setCodePostal(user.getInt("codePostal"));
        this.user.setPseudo(user.getString("pseudo"));
        this.user.setMdp(user.getString("mdp"));
        this.user.setPhoto(user.isNull("desc") ? null : user.getString("photo"));
        this.user.setDesc(user.isNull("desc") ? null : user.getString("desc"));
        this.user.setTailleOrganisme(user.getInt("tailleOrganisme"));
        this.user.setEstValide(user.getInt("estValide"));
        this.user.setSiret(user.getString("siret"));
        this.user.setDateDeNaissance(user.isNull("dateDeNaissance") ? null : LocalDate.parse(user.getString("dateDeNaissance")));
        this.user.setNbPointsSourire(user.isNull("nbPointSourire") ? null : user.getInt("nbPointSourire"));
    }

    public void setUser(User user){
        this.user.setAdresse(user.getAdresse());
        this.user.setCodePostal(user.getCodePostal());
        this.user.setMail(user.getMail());
        this.user.setMdp(user.getMdp());
        this.user.setTel(user.getTel());
        this.user.setVille(user.getVille());
    }

    public boolean saveUser(User user) {
        HttpURLConnection http = null;
        CloseableHttpClient client = null;
        try {
            // Form url and json for login
            URL url = new URL("https://wastemart-api.herokuapp.com/user/");

            String jsonBody =
                    "{\n" +
                            "\t\"id\": \""+user.getId()+"\",\n" +
                            "\t\"libelle\" : \""+user.getLibelle()+"\",\n" +
                            "\t\"nom\": \""+user.getNom()+"\",\n" +
                            "\t\"prenom\": \""+user.getPrenom()+"\",\n" +
                            "\t\"mail\":\""+user.getMail()+"\",\n" +
                            "\t\"tel\":\""+user.getTel()+"\",\n" +
                            "\t\"adresse\":\""+user.getAdresse()+"\",\n" +
                            "\t\"ville\":\""+user.getVille()+"\",\n" +
                            "\t\"codePostal\":\""+user.getCodePostal()+"\",\n" +
                            "\t\"pseudo\":\""+user.getPseudo()+"\",\n" +
                            "\t\"mdp\":\""+user.getMdp()+"\",\n" +
                            "\t\"photo\":\""+user.getPhoto()+"\",\n" +
                            "\t\"desc\":\""+user.getDesc()+"\",\n" +
                            "\t\"tailleOrganisme\":\""+user.getTailleOrganisme()+"\",\n" +
                            "\t\"estValide\":\""+user.getEstValide()+"\",\n" +
                            "\t\"siret\":\""+user.getSiret()+"\",\n" +
                            "\t\"dateNaissance\":\""+user.getDateDeNaissance()+"\",\n" +
                            "\t\"nbPointsSourire\":\""+user.getNbPointsSourire()+"\"\n" +
                            "}";
            System.out.println(jsonBody);
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
            // Form returned token and verify it
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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
}