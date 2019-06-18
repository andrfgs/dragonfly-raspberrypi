package com.dragonfly.util;

import com.dragonfly.entity.Token;
import com.dragonfly.entity.User;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Caches and stored the current logged in user
 */
public class UserCache {

    private static final User user = new User("andrfgs", "password");
    private static final int unitID = 2;
    private static final Gson gson = new Gson();
    private static Token token;

    public static String getTokenID() throws IOException {
        if (token == null || !isTokenValid())
            token = getToken();

        return token.getTokenID();
    }

    public static String getUsername() { return user.getUsername(); }

    private static boolean isTokenValid() { return System.currentTimeMillis() > token.getExpirationDate(); }

    private static Token getToken() throws IOException {
        HTTPResponse r = HTTPUtils.get(String.format(HTTPUtils.REST_BASE + "/user/login?username=%s&password=%s",
                user.getUsername(), user.getPassword()));

        if (r.getHttpCode() != 200)
            throw new IOException("Error in login: " + r.getContent());

        return gson.fromJson(r.getContent(), Token.class);
    }

    public static int getUnitID() { return unitID; }
}
