package fr.wastemart.maven.javaclient.services;

import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class User {
    public static JSONArray fetchAllUsers() {
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/user/");
            // /list/products?id=1

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

            JSONArray users = new JSONArray(content.toString());

            for(int i = 0; i < users.length(); i++) {

                Integer categorieUtilisateur = fetchCategory(users.getJSONObject(i).getInt("id"));

                users.getJSONObject(i).put("categorieUtilisateur", categorieUtilisateur > 5 ? null : categorieUtilisateur);

                if (users.getJSONObject(i).isNull("Libelle")) {
                    users.getJSONObject(i).put("Libelle", "");
                }
                if (users.getJSONObject(i).isNull("nom")) {
                    users.getJSONObject(i).put("nom", "");
                }
                if (users.getJSONObject(i).isNull("prenom ")) {
                    users.getJSONObject(i).put("prenom", "");
                }
                if (users.getJSONObject(i).isNull("photo")) {
                    users.getJSONObject(i).put("photo", "");
                }
                if (users.getJSONObject(i).isNull("desc")) {
                    users.getJSONObject(i).put("desc", "");
                }
                if (users.getJSONObject(i).isNull("tailleOrganisme")) {
                    users.getJSONObject(i).put("tailleOrganisme", 0);
                }
                if (users.getJSONObject(i).isNull("dateDeNaissance")) {
                    users.getJSONObject(i).put("destinataire", "");
                }
                if (users.getJSONObject(i).isNull("nbPointsSourire")) {
                    users.getJSONObject(i).put("nbPointsSourire", 0);
                }

            }


            return users;

        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray("{null}");
        }
    }

    // Get User By id : 1, by mail : 2 (Get)
    public static JSONObject fetchUser(String operation, String data) {
        URL url = null;
        try {

            if(operation.equals("id")){
                url = new URL("https://wastemart-api.herokuapp.com/user/?id=" + data);
            } else if(operation.equals("mail")) {
                url = new URL("https://wastemart-api.herokuapp.com/user/?mail=" + data);
            }
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

            JSONObject user =  new JSONObject(content.toString());
            user.put("categorieUtilisateur",fetchCategory(user.getInt("id")));
            return user;

        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject("{null}");
        }
    }

    // Update a user (Put)
    public static Integer updateUser(fr.wastemart.maven.javaclient.models.User user) {
        HttpURLConnection http = null;
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/user/");

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

    // Register a user (Post)
    public static Integer createUser(fr.wastemart.maven.javaclient.models.User user) {
        HttpURLConnection http = null;
        CloseableHttpClient client = null;
        URL url = null;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/user/register");

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

    // Add a category between User and userType (Post)
    public static Integer addCategory(Integer userId, Integer categoryUserId){
        HttpURLConnection http = null;
        CloseableHttpClient client = null;
        URL url = null;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/user/category");

            String jsonBody =
                    "{\n" +
                            "\t\"userId\":"+userId+",\n" +
                            "\t\"categoryUserId\":"+categoryUserId+"\n" +
                            "}";

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


    // Fetch user Category (Get)
    public static Integer fetchCategory(Integer userId){
        URL url;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/user/category?userId=" + userId);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            Reader streamReader;
            if (status > 299) {
                return status;
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

    public static Integer initNewUser(String mail, Integer userCategory) {
        JSONObject fetchedUser = fetchUser("mail", mail);
        return addCategory(fetchedUser.getInt("id"), userCategory);
    }

    public static fr.wastemart.maven.javaclient.models.User jsonToUser(JSONObject user) {
        return new fr.wastemart.maven.javaclient.models.User(
                user.getInt("id"),
                user.getString("libelle"),
                user.getInt("categorieUtilisateur"),
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
                user.getString("dateDeNaissance"),
                //user.isNull("dateDeNaissance") ? null : LocalDate.parse(user.getString("dateDeNaissance"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")),
                user.isNull("nbPointSourire") ? null : user.getInt("nbPointSourire")
        );
    }

    public static Integer sendMail(String mail, String objet, String message) {
        HttpURLConnection http = null;
        CloseableHttpClient client = null;
        URL url = null;
        try {
            url = new URL("https://wastemart-api.herokuapp.com/mail");

            String jsonBody =
                    "{\n" +
                        "\t\"sender\": \"wastemart@gmail.com\",\n" +
                        "\t\"destination\": \""+mail+"\",\n" +
                        "\t\"subject\": \""+objet+"\",\n" +
                        "\t\"message\": \""+message+"\",\n" +
                    "}";

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
            try (OutputStream os = http.getOutputStream()) {
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
