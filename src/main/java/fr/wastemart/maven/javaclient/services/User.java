package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

public class User {
        // --- POST --- //

    // POST a user (Register)
    public static Integer createUser(fr.wastemart.maven.javaclient.models.User user) {

        String json = "{\n" +
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

        Integer result;

        try {
            result = Requester.sendPostRequest("user/register", json, null);
        } catch (Exception e) {
            //Logger.reportError(e);
            result = null;
        }

        return result;
    }

    // POST a category between User and userType
    public static Integer addCategory(Integer userId, Integer categoryUserId){
        String json =
                "{\n" +
                        "\t\"userId\":"+userId+",\n" +
                        "\t\"categoryUserId\":"+categoryUserId+"\n" +
                        "}";

        Integer result;
        try {
            result = Requester.sendPostRequest("user/category", json, null);
        } catch (Exception e) {
            //Logger.reportError(e);
            result = null;
        }

        return result;
    }

    // POST mail
    public static Integer sendMail(String mail, String objet, String message) {
        String json =
                "{\n" +
                        "\t\"sender\": \"wastemart@gmail.com\",\n" +
                        "\t\"destination\": \""+mail+"\",\n" +
                        "\t\"subject\": \""+objet+"\",\n" +
                        "\t\"message\": \""+message+"\",\n" +
                        "}";

        Integer result;
        try {
            result = Requester.sendPostRequest("mail", json, null);
        } catch (Exception e) {
            //Logger.reportError(e);
            result = null;
        }

        return result;
    }


        // --- GET --- //

    // GET User By id : 1, by mail : 2
    public static JSONObject fetchUser(String operation, String data) {
        String url = null;
        if(operation.equals("id")){
            url = "user/?id=" + data;
        } else if(operation.equals("mail")) {
            url = "user/?mail=" + data;
        }

        JSONObject user;
        try {
            user = Requester.sendGetRequest(url, null).getJSONObject(0);
            // TODO Test it, not sure it works (JsonOBJECT was initially returned)
            user.put("categorieUtilisateur",fetchCategory(user.getInt("id")));
        } catch (Exception e) {
            //Logger.reportError(e);
            user = null;
        }

        return user;
    }

    // GET user Category
    public static Integer fetchCategory(Integer userId){
        Integer result;
        try {
            result = Requester.sendGetRequest("user/category?userId=" + userId, null).getJSONObject(0).getInt("user_category");
            // TODO Test : Initially return Integer.valueOf(content.toString());
        } catch (Exception e) {
            //Logger.reportError(e);
            result = null;
        }

        return result;
    }

    // GET all Users Categories
    public static JSONArray fetchCategories() {
        JSONArray result;
        try {
             result = Requester.sendGetRequest("user/categories", null);
        } catch (Exception e) {
            //Logger.reportError(e);
            result = null;
        }

        return result;
    }



    // --- PUT --- //

    // PUT a user (Update)
    public static Integer updateUser(fr.wastemart.maven.javaclient.models.User user) {
        String json =
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

        Integer result;
        try {
            result = Requester.sendPutRequest("user/", json, null);
        } catch (Exception e) {
            //Logger.reportError(e);
            result = null;
        }

        return result;
    }


        // --- DELETE ---//


    public static JSONArray fetchAllUsers() {
        JSONArray users;
        try {
            users = Requester.sendGetRequest("user/", null);

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
        } catch (Exception e) {
            //Logger.reportError(e);
            users = null;
        }

        return users;
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

}
