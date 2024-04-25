package com.example.eaclient.Network.HttpRequests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RequestExecutor {
    //TODO:Завернуть все в поток? ExecutorService
    public static HttpResponse executeRequest(String urlString, String method, String requestBody) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn;
        try{
            conn = (HttpURLConnection) url.openConnection();
        }catch (Exception e){
            return new HttpResponse(-1, "Connection refused: connect");
        }
        assert conn != null;
        conn.setRequestMethod(method);
        conn.setDoOutput(true);

        if (requestBody != null && !requestBody.isEmpty()) {
            byte[] postData = requestBody.getBytes(StandardCharsets.UTF_8);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(postData.length));
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
        }

        int responseCode;
        responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            System.out.println(responseCode);
            return new HttpResponse(conn.getResponseCode(), "");
        } else {
            System.out.println(responseCode);
            String response = readResponse(conn);
            conn.disconnect();
            return new HttpResponse(responseCode, response);
        }
    }

    private static String readResponse(HttpURLConnection conn) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        System.out.println("Ответ от сервера:" + response);
        return response.toString();
    }
}

