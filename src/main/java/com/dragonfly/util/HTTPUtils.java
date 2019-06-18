package com.dragonfly.util;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPUtils {

    public static final String REST_BASE = "http://192.168.43.90:8080/dragonfly/rest";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String CONTENT_TYPE = "application/json; charset=utf-8";

    private static final Gson gson = new Gson();

    public static HTTPResponse get(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        BufferedReader in;
        if (con.getResponseCode() < 299) // success
            in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));

        else
            in = new BufferedReader(new InputStreamReader(
                    con.getErrorStream()));

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new HTTPResponse(response.toString(), con.getResponseCode());
    }

    public static HTTPResponse post(String url, String body) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", CONTENT_TYPE);

        // POST Body
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(body.getBytes());
        os.flush();
        os.close();

        BufferedReader in;
        if (con.getResponseCode() <= 299)
            in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
        else
            in = new BufferedReader(new InputStreamReader(
                    con.getErrorStream()));


        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new HTTPResponse(response.toString(), con.getResponseCode());
    }
}
