package services;

import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class User {

    private models.User user;

    public JSONObject fetchUserById(Integer idUser) {
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/user?id="+idUser);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            Reader streamReader;
            if (status > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
            } else {
                streamReader = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8);
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

    // Fetch user Category (Get)
    public Integer fetchCategory(Integer userId){
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/user/category?userId=" + userId);

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

            return Integer.valueOf(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public models.User getUser(JSONObject user) {
        System.out.println(user);


        this.user = new models.User(
                user.getInt("id"),
                user.getString("libelle"),
                fetchCategory(user.getInt("id")),
                user.getString("nom"),
                user.getString("prenom"),
                user.getString("mail"),
                user.getString("tel"),
                user.getString("adresse"),
                user.getString("ville"),
                user.getInt("codePostal"),
                user.getString("pseudo"),
                user.getString("mdp"),
                user.isNull("photo") ? null : user.getString("photo"),
                user.isNull("desc") ? null : user.getString("desc"),
                user.getInt("tailleOrganisme"),
                user.getInt("estValide"),
                user.getString("siret"),
                user.isNull("dateDeNaissance") ? null : LocalDate.parse(user.getString("dateDeNaissance"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")),
                user.isNull("nbPointSourire") ? null : user.getInt("nbPointSourire")
        );

        return this.user;
    }



    public Integer saveUser(models.User user, String operation) {
        HttpURLConnection http = null;
        CloseableHttpClient client = null;
        URL url = null;
        try {
            // Form url and json for login
            if(operation.equals("reg")){
                url = new URL("https://wastemart-api.herokuapp.com/user/register");
            } else if (operation.equals("sav")){
                url = new URL("https://wastemart-api.herokuapp.com/user/") ;

            }

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
                            "\t\"codePostal\":"+user.getCodePostal()+",\n" +
                            "\t\"pseudo\":\""+user.getPseudo()+"\",\n" +
                            "\t\"mdp\":\""+user.getMdp()+"\",\n" +
                            "\t\"photo\":\""+user.getPhoto()+"\",\n" +
                            "\t\"desc\":\""+user.getDesc()+"\",\n" +
                            "\t\"tailleOrganisme\":"+user.getTailleOrganisme()+",\n" +
                            "\t\"estValide\":"+user.getEstValide()+",\n" +
                            "\t\"siret\":\""+user.getSiret()+"\",\n" +
                            "\t\"dateDeNaissance\":\""+user.getDateDeNaissance()+"\",\n" +
                            "\t\"nbPointsSourire\":"+user.getNbPointsSourire()+"\n" +
                            "}";
            byte[] out = jsonBody.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            // Instantiate connection
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            http = (HttpURLConnection) con;
            if(operation.equals("sav")){
                ((HttpURLConnection) con).setRequestMethod("PUT");
            }


            // Form request, connect and send json
            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }
            // Form returned token and verify it
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
