package fr.wastemart.maven.javaclient.services;

import org.json.JSONArray;
import org.json.JSONObject;

public class HttpResponse {
    private Integer responseCode;
    private String data;

    public HttpResponse(Integer responseCode, String data) {
        this.data = data;
        this.responseCode = responseCode;
    }

    public String toString() {
        return responseCode + "," + data;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getData() {
        return data;
    }

    public JSONObject getDataAsJSONObject() {
        return new JSONObject(data);
    }

    public JSONArray getDataAsJSONArray() {
        return new JSONArray(data);
    }

}
