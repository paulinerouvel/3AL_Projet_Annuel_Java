package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class User {
        // --- POST --- //

    // POST a user (Register)
    public static boolean createUser(fr.wastemart.maven.javaclient.models.User user) {
        String libelle = user.getLibelle() == null ? "\"\"" : "\""+user.getLibelle()+"\"";
        String nom = user.getNom() == null ? "\"\"" : "\""+user.getNom()+"\"";
        String prenom = user.getPrenom() == null ? "\"\"" : "\""+user.getPrenom()+"\"";
        String photo = user.getPhoto() == null ? "\"\"" : "\""+user.getPhoto()+"\"";
        String desc = user.getDesc() == null ? "\"\"" : "\""+user.getDesc()+"\"";
        String siret = user.getSiret() == null ? "\"\"" : "\""+user.getSiret()+"\"";
        String dateDeNaissance = user.getDateDeNaissance() == null ? "\"\"" : "\""+user.getDateDeNaissance()+"\"";

        String json = "{\n" +
            "\t\"id\":\""+user.getId()+"\",\n" +
            "\t\"libelle\":"+libelle+",\n" +
            "\t\"nom\":"+nom+",\n" +
            "\t\"prenom\":"+prenom+",\n" +
            "\t\"mail\":\""+user.getMail()+"\",\n" +
            "\t\"tel\":\""+user.getTel()+"\",\n" +
            "\t\"adresse\":\""+user.getAdresse()+"\",\n" +
            "\t\"ville\":\""+user.getVille()+"\",\n" +
            "\t\"codePostal\":\""+user.getCodePostal()+"\",\n" +
            "\t\"pseudo\":\""+user.getPseudo()+"\",\n" +
            "\t\"mdp\":\""+user.getMdp()+"\",\n" +
            "\t\"photo\":"+photo+",\n" +
            "\t\"desc\":"+desc+",\n" +
            "\t\"tailleOrganisme\":"+user.getTailleOrganisme()+",\n" +
            "\t\"estValide\":"+user.getEstValide()+",\n" +
            "\t\"siret\":"+siret+",\n" +
            "\t\"dateDeNaissance\":"+dateDeNaissance+",\n" +
            "\t\"nbPointsSourire\":"+user.getNbPointsSourire()+"\n" +
        "}";

        Integer result = 299;

        try {
            result = Requester.sendPostRequest("user/register", json, null).getResponseCode();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result < 299;
    }

    // POST a category between User and userType
    public static boolean addCategory(Integer userId, Integer categoryUserId) {
        String json =
                "{\n" +
                        "\t\"userId\":"+userId+",\n" +
                        "\t\"categoryUserId\":"+categoryUserId+"\n" +
                        "}";

        Integer result = 299;

        try {
            result = Requester.sendPostRequest("user/category", json, null).getResponseCode();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result < 299;
    }

    // POST mail
    public static boolean sendMail(String receiver, String subject, String body) throws Exception {
        body = body.replace("\n","\\n");
        String json =
                "{\n" +
                        "\t\"sender\": \"wastemart@gmail.com\",\n" +
                        "\t\"destination\": \""+receiver+"\",\n" +
                        "\t\"subject\": \""+subject+"\",\n" +
                        "\t\"message\": \""+body+"\"\n" +
                        "}";

        Integer result = 299;

        result = Requester.sendPostRequest("mail", json, null).getResponseCode();

        return result < 299;
    }

    // POST image
    public static String sendPhoto(File photo, Integer userId){
        String result = null;

        String fileName = photo.getName();
        String extension = "";

        if(fileName.contains(".")){
            extension = fileName.substring(fileName.lastIndexOf("."));
        }

        File renamedPhoto = new File("img_profil_" + userId + extension);

        Path copied = Paths.get(renamedPhoto.toURI());
        Path original = photo.toPath();

        try {
            Files.copy(original, copied, REPLACE_EXISTING);
        } catch (IOException e) {
            Logger.getInstance().reportError(e);
        }

        try {
            if(Requester.sendFile("images/", renamedPhoto).getResponseCode() < 299){
                renamedPhoto.delete();
                result = renamedPhoto.getName();
            }

        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }


        return result;
    }

        // --- GET --- //

    // GET User By id : 1, by mail : 2
    public static fr.wastemart.maven.javaclient.models.User fetchUser(Integer id) {
        fr.wastemart.maven.javaclient.models.User user = null;
        String url = "user/?id=" + id;

        try {
            HttpResponse fetchUserResponse = Requester.sendGetRequest(url, null);

            if (fetchUserResponse.getResponseCode() <= 299) {
                JSONObject jsonUser = fetchUserResponse.getDataAsJSONObject();

                user = jsonToUser(jsonUser);
                Integer userCategory = fetchCategory(jsonUser.getInt("id"));
                user.setCategorieUtilisateur(userCategory);
            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return user;
    }

    public static JSONObject fetchCreatedUser(String data) {
        String url = "user/?mail=" + data;

        JSONObject jsonUser = null;
        try {
            HttpResponse fetchUserResponse = Requester.sendGetRequest(url, null);

            if (fetchUserResponse.getResponseCode() <= 299) {
                jsonUser = fetchUserResponse.getDataAsJSONObject();

            }
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return jsonUser;

    }

    public static JSONArray fetchAllUsers() {
        JSONArray users = null;

        try {
            HttpResponse response = Requester.sendGetRequest("user/", null);
            users = response.getDataAsJSONArray();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return users;
    }

    // GET the Category of one User
    public static Integer fetchCategory(Integer userId) {
        Integer result = null;

        try {
            HttpResponse response = Requester.sendGetRequest("user/category?userId=" + userId, null);
            result = response.getDataAsJSONObject().getInt("Categorie_utilisateur_id");
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result;
    }

    // GET the Category of one User
    public static JSONObject fetchCategoryAsJSONObject(Integer userId) {
        JSONObject result = null;

        try {
            HttpResponse response = Requester.sendGetRequest("user/category?userId=" + userId, null);
            result = response.getDataAsJSONObject();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result;
    }

    // GET all Users Categories
    public static JSONArray fetchCategories() {
        JSONArray result = null;

        try {
            HttpResponse response = Requester.sendGetRequest("user/categories", null);
            result = response.getDataAsJSONArray();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result;
    }

    // GET all Users of a Category
    public static JSONArray fetchUsersByCategory(String libelle) {
        JSONArray result = null;

        try {
            HttpResponse response = Requester.sendGetRequest("user/allByCategory?type="+libelle, null);
            result = response.getDataAsJSONArray();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result;
    }

    public static JSONArray fetchInvalidUsersByCategory(String libelle) {
        JSONArray result = null;

        try {
            HttpResponse response = Requester.sendGetRequest("user/allInvalidByCategory?type="+libelle, null);
            result = response.getDataAsJSONArray();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result;
    }

    public static File fetchPhoto(String file) {
        File result = null;

        try {
            result = Requester.downloadFile("images/", file);
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result;
    }


    // --- PUT --- //

    // PUT a user (Update)
    public static boolean updateUser(fr.wastemart.maven.javaclient.models.User user, String token) {
        String libelle = user.getLibelle() == null ? "\"\"" : "\""+user.getLibelle()+"\"";
        String nom = user.getNom() == null ? "\"\"" : "\""+user.getNom()+"\"";
        String prenom = user.getPrenom() == null ? "\"\"" : "\""+user.getPrenom()+"\"";
        String photo = user.getPhoto() == null ? "\"\"" : "\""+user.getPhoto()+"\"";
        String desc = user.getDesc() == null ? "\"\"" : "\""+user.getDesc()+"\"";
        String siret = user.getSiret() == null ? "\"\"" : "\""+user.getSiret()+"\"";
        String dateDeNaissance = user.getDateDeNaissance() == null ? "\"\"" : "\""+user.getDateDeNaissance()+"\"";

        String json = "{\n" +
                "\t\"id\": \""+user.getId()+"\",\n" +
                "\t\"libelle\" : "+libelle+",\n" +
                "\t\"nom\": "+nom+",\n" +
                "\t\"prenom\": "+prenom+",\n" +
                "\t\"mail\":\""+user.getMail()+"\",\n" +
                "\t\"tel\":\""+user.getTel()+"\",\n" +
                "\t\"adresse\":\""+user.getAdresse()+"\",\n" +
                "\t\"ville\":\""+user.getVille()+"\",\n" +
                "\t\"codePostal\":\""+user.getCodePostal()+"\",\n" +
                "\t\"pseudo\":\""+user.getPseudo()+"\",\n" +
                "\t\"mdp\":\""+user.getMdp()+"\",\n" +
                "\t\"photo\":"+photo+",\n" +
                "\t\"desc\":"+desc+",\n" +
                "\t\"tailleOrganisme\":"+user.getTailleOrganisme()+",\n" +
                "\t\"estValide\":"+user.getEstValide()+",\n" +
                "\t\"siret\":"+siret+",\n" +
                "\t\"dateDeNaissance\":"+dateDeNaissance+",\n" +
                "\t\"nbPointsSourire\":"+user.getNbPointsSourire()+"\n" +
                "}";

        Integer result = 299;

        try {
            result = Requester.sendPutRequest("user/", json, token).getResponseCode();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result < 299;
    }


        // --- DELETE ---//

    public static boolean deleteUser(Integer userId, String token) {
        Integer result = 299;

        try {
            result = Requester.sendDeleteRequest("user/" + userId, token).getResponseCode();
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
        }

        return result < 299;
    }




    public static boolean RegisterNewUser(String mail, Integer userCategory) {
        JSONObject createdUser = fetchCreatedUser(mail);
        if(createdUser == null) {
            return false;
        } else {
            return addCategory(createdUser.getInt("id"), userCategory);
        }
    }

    public static fr.wastemart.maven.javaclient.models.User jsonToUser(JSONObject user) {
        try {
            return new fr.wastemart.maven.javaclient.models.User(
                    user.getInt("id"),
                    user.isNull("libelle") ? null : user.getString("libelle"),
                    -1,
                    user.isNull("nom") ? null : user.getString("nom"),
                    user.isNull("prenom") ? null : user.getString("prenom"),
                    user.getString("mail"),
                    user.getString("tel"),
                    user.getString("adresse"),
                    user.getString("ville"),
                    user.getInt("codePostal"),
                    user.getString("pseudo"),
                    user.getString("mdp"),
                    user.isNull("photo") ? null : user.getString("photo"),
                    user.isNull("desc") ? null : user.getString("desc"),
                    user.isNull("tailleOrganisme") ? null : user.getInt("tailleOrganisme"),
                    user.getInt("estValide") == 1,
                    user.isNull("siret") ? null : user.getString("siret"),
                    user.isNull("dateDeNaissance") ? null : DateFormatter.dateToString(user.getString("dateDeNaissance")),
                    //user.isNull("dateDeNaissance") ? null : LocalDate.parse(user.getString("dateDeNaissance"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")),
                    user.isNull("nbPointSourire") ? null : user.getInt("nbPointSourire")
            );
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            return null;
        }
    }

}
