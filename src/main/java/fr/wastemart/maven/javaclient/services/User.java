package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

public class User {
        // --- POST --- //

    // POST a user (Register)
    public static Integer createUser(fr.wastemart.maven.javaclient.models.User user) throws Exception {

        String json = "{\n" +
            "\t\"id\": \""+user.getId()+"\",\n" +
            "\t\"libelle\" : "+user.getLibelle()+",\n" +
            "\t\"nom\": "+user.getNom()+",\n" +
            "\t\"prenom\": "+user.getPrenom()+",\n" +
            "\t\"mail\":\""+user.getMail()+"\",\n" +
            "\t\"tel\":\""+user.getTel()+"\",\n" +
            "\t\"adresse\":\""+user.getAdresse()+"\",\n" +
            "\t\"ville\":\""+user.getVille()+"\",\n" +
            "\t\"codePostal\":\""+user.getCodePostal()+"\",\n" +
            "\t\"pseudo\":\""+user.getPseudo()+"\",\n" +
            "\t\"mdp\":\""+user.getMdp()+"\",\n" +
            "\t\"photo\":"+user.getPhoto()+",\n" +
            "\t\"desc\":"+user.getDesc()+",\n" +
            "\t\"tailleOrganisme\":"+user.getTailleOrganisme()+",\n" +
            "\t\"estValide\":"+user.getEstValide()+",\n" +
            "\t\"siret\":"+user.getSiret()+",\n" +
            "\t\"dateDeNaissance\":"+user.getDateDeNaissance()+",\n" +
            "\t\"nbPointsSourire\":"+user.getNbPointsSourire()+"\n" +
        "}";

        Integer result;

        result = Requester.sendPostRequest("user/register", json, null).getResponseCode();

        return result;
    }

    // POST a category between User and userType
    public static Integer addCategory(Integer userId, Integer categoryUserId) throws Exception {
        String json =
                "{\n" +
                        "\t\"userId\":"+userId+",\n" +
                        "\t\"categoryUserId\":"+categoryUserId+"\n" +
                        "}";

        Integer result;
        result = Requester.sendPostRequest("user/category", json, null).getResponseCode();

        return result;
    }

    // POST mail
    public static Integer sendMail(String mail, String objet, String message) throws Exception {
        String json =
                "{\n" +
                        "\t\"sender\": \"wastemart@gmail.com\",\n" +
                        "\t\"destination\": \""+mail+"\",\n" +
                        "\t\"subject\": \""+objet+"\",\n" +
                        "\t\"message\": \""+message+"\",\n" +
                        "}";

        Integer result;
        result = Requester.sendPostRequest("mail", json, null).getResponseCode();

        return result;
    }


        // --- GET --- //

    // GET User By id : 1, by mail : 2
    public static JSONObject fetchUser(String operation, String data) throws Exception {
        String url = null;
        if(operation.equals("id")){
            url = "user/?id=" + data;
        } else if(operation.equals("mail")) {
            url = "user/?mail=" + data;
        }

        JSONObject user;
        HttpResponse response = Requester.sendGetRequest(url, null);
        user = response.getDataAsJSONObject();
        // TODO Test it, not sure it works (JsonOBJECT was initially returned)

        user.put("categorieUtilisateur",fetchCategory(user.getInt("id")));

        System.out.println("(User.fetchUser) Final user : ");
        System.out.println(user);

        return user;
    }

    // GET user Category
    public static String fetchCategory(Integer userId) throws Exception {
        String result;
        System.out.println("(User.fetchCategory) About to ask Category of user");
        HttpResponse response = Requester.sendGetRequest("user/category?userId=" + userId, null);
        result = response.getData();
        // TODO Test : Initially return Integer.valueOf(content.toString());

        System.out.println("(User.fetchCategory) Category of user : ");
        System.out.println(result);

        return result;
    }

    // GET all Users Categories
    public static JSONArray fetchCategories() throws Exception {
        JSONArray result;
        HttpResponse response = Requester.sendGetRequest("user/categories", null);
        result = response.getDataAsJSONArray();

        return result;
    }



    // --- PUT --- //

    // PUT a user (Update)
    public static Integer updateUser(fr.wastemart.maven.javaclient.models.User user) throws Exception {
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
        result = Requester.sendPutRequest("user/", json, null).getResponseCode();

        return result;
    }


        // --- DELETE ---//

    public static JSONArray fetchAllUsers() throws Exception {
        JSONArray users;
        HttpResponse response = Requester.sendGetRequest("user/", null);
        users = response.getDataAsJSONArray();

        for(int i = 0; i < users.length(); i++) {

            //Integer categorieUtilisateur = fetchCategory(users.getJSONObject(i).getInt("id"));
            Integer categorieUtilisateur = 5;

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
    }

    public static Integer initNewUser(String mail, Integer userCategory) throws Exception {
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
                user.getInt("estValide") == 1,
            user.isNull("siret") ? null : user.getString("siret"),
            user.getString("dateDeNaissance"),
            //user.isNull("dateDeNaissance") ? null : LocalDate.parse(user.getString("dateDeNaissance"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")),
            user.isNull("nbPointSourire") ? null : user.getInt("nbPointSourire")
        );
    }

}
