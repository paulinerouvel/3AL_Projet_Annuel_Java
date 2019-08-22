package fr.wastemart.maven.javaclient.services.Details;

import fr.wastemart.maven.javaclient.models.User;

public class UserDetail implements Detail {
    private User user;

    public UserDetail(User userDetail) {
        user = userDetail;
    }

    public User getUser(){
        return user;
    }


}