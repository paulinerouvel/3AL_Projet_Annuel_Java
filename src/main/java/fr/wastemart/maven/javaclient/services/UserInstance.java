package fr.wastemart.maven.javaclient.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.wastemart.maven.javaclient.models.User;
import org.json.JSONObject;

import static fr.wastemart.maven.javaclient.services.User.fetchUser;

public class UserInstance {

    public boolean isConnected = false;
    private JSONObject token;
    private User user;

    /** Constructeur privé */
    private UserInstance(){}

    /** Instance unique */
    private static UserInstance INSTANCE;

    /** Point d'accès pour l'instance unique du Singleton */
    public static UserInstance getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserInstance();
        }
        return INSTANCE;
    }

    public boolean tokenIsValid() throws JWTVerificationException {
        String token = getTokenValue();
        try {
            Algorithm algorithm = Algorithm.HMAC256(" SFGQDFB54QSDF5G4W5XV43QGREgdfg54214542sdf24242sf424bjksgdfsqfgZR");
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptLeeway(60)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);

            return true;
        } catch (JWTVerificationException exception) {
            Logger.getInstance().reportError(exception);
            return false;
        }
    }

    public void initUser() {
        User fetchedUser = fetchUser(token.getInt("userId"));
        setUser(fetchedUser);
    }

    // Login (Post)
    public HttpResponse login(String login, String password){
        HttpResponse result;
        String json =  "{\"mail\":\""+ login +"\",\"mdp\":\""+ password +"\"}";

        try {
            result = Requester.sendPostRequest("user/login", json, null);
        } catch (Exception e) {
            Logger.getInstance().reportError(e);
            e.printStackTrace();
            result = new HttpResponse(417, "Erreure interne");
        }

        return result;
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

    public Integer getTokenUserCategory() { return token.getJSONObject("userCategory").getInt("Categorie_utilisateur_id"); }

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