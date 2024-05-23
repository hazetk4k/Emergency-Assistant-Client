package com.example.eaclient.Network.HttpRequests;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SimpleRequestManager {
    private static final String BASE_URL = "http://localhost:8080";

    private static String encodeParameters(String queryParams) {
        if (queryParams != null && !queryParams.isEmpty()) {
            StringBuilder encodedParams = new StringBuilder();
            String[] pairs = queryParams.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx != -1) {
                    String key = pair.substring(0, idx);
                    String value = pair.substring(idx + 1);
                    if (!encodedParams.isEmpty()) {
                        encodedParams.append("&");
                    }
                    encodedParams.append(key).append("=").append(URLEncoder.encode(value, StandardCharsets.UTF_8));
                }
            }
            return encodedParams.toString();
        } else {
            return "";
        }
    }

    public static HttpResponse sendGetRequest(String endpoint) throws IOException {

        return sendGetRequest(endpoint, null);
    }

    public static HttpResponse sendGetRequest(String endpoint, String queryParams) throws IOException {
        String encodedParams = encodeParameters(queryParams);
        String urlString = BASE_URL + endpoint;
        if (queryParams != null && !queryParams.isEmpty()) {
            urlString += "?" + encodedParams;
        }

        return RequestExecutor.executeRequest(urlString, "GET", null);
    }

    public static HttpResponse sendDeleteRequest(String endpoint, String queryParams) throws IOException {
        String encodedParams = encodeParameters(queryParams);
        String urlString = BASE_URL + endpoint;
        if (queryParams != null && !queryParams.isEmpty()) {
            urlString += "?" + encodedParams;
        }
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



