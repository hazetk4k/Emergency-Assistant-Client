package com.example.eaclient.Network;

import java.io.IOException;

public class SimpleRequestManager {
    private static final String BASE_URL = "http://localhost:8080";

    public static HttpResponse sendGetRequest(String endpoint) throws IOException {
        return sendGetRequest(endpoint, null);
    }

    public static HttpResponse sendGetRequest(String endpoint, String queryParams) throws IOException {
        String urlString = BASE_URL + endpoint;
        if (queryParams != null && !queryParams.isEmpty()) {
            urlString += "?" + queryParams;
        }
        return RequestExecutor.executeRequest(urlString, "GET", null);
    }

    public static HttpResponse sendDeleteRequest(String endpoint, String queryParams) throws IOException {
        String urlString = BASE_URL + endpoint;
        if (queryParams != null && !queryParams.isEmpty()) {
            urlString += "?" + queryParams;
        }
        System.out.println(urlString);
        return RequestExecutor.executeRequest(urlString, "DELETE", null);
    }

    public static HttpResponse sendPostRequest(String endpoint, String requestBody) throws IOException {
        String urlString = BASE_URL + endpoint;
        return RequestExecutor.executeRequest(urlString, "POST", requestBody);
    }

    public static HttpResponse sendPutRequest(String endpoint, String requestBody) throws IOException {
        String urlString = BASE_URL + endpoint;
        return RequestExecutor.executeRequest(urlString, "PUT", requestBody);
    }
}



