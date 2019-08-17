package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;

public class UserCategories {
    public static JSONArray fetchCategories() {
        return Requester.sendGetRequest("user/categories");
    }
}
