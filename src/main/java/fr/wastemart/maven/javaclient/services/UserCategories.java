package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;

public class UserCategories {
        // --- POST --- //

        // --- GET --- //

    // GET all Users Categories
    public static JSONArray fetchCategories() {
        return Requester.sendGetRequest("user/categories");
    }


        // --- PUT --- //

        // --- DELETE ---//

}
