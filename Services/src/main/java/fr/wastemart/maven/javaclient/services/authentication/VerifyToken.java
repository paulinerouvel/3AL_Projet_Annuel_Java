package fr.wastemart.maven.javaclient.services.authentication;

import javax.xml.bind.DatatypeConverter;

import com.sun.javafx.scene.traversal.Algorithm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;


public class VerifyToken {
    void VerifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
        }
    }

}
