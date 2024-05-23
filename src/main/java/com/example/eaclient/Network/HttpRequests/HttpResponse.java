package com.example.eaclient.Network.HttpRequests;

public class HttpResponse {
    private final int responseCode;
    private final String responseBody;

    public HttpResponse(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {

        return responseBody;
    }
}
