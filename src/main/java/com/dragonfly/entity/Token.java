package com.dragonfly.entity;

import java.util.UUID;

public class Token {
    private static final int VALIDITY = 24; // 24 hours

    private String username;
    private String tokenID;
    private long creationDate;
    private long expirationDate;

    public Token(String username)
    {
        this.username = username;
        tokenID = UUID.randomUUID().toString();
        creationDate = System.currentTimeMillis();
        expirationDate = creationDate + VALIDITY * 3600000;
    }

    public Token() {}

    public String getUsername() {
        return username;
    }

    public String getTokenID() {
        return tokenID;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public long getExpirationDate() {
        return expirationDate;
    }
}
