package controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import services.Authentication;

public class UserInstance {

    private String token = null;

    public boolean tokenIsValid() throws JWTVerificationException {
        String token = getToken();
        try {
            Algorithm algorithm = Algorithm.HMAC256(" SFGQDFB54QSDF5G4W5XV43QGREgdfg54214542sdf24242sf424bjksgdfsqfgZR");
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptLeeway(15)
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

// other properties as needed...
}