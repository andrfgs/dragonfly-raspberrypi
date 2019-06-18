package com.dragonfly.util;

public class HTTPResponse {

    private String content;
    private int httpCode;

    public HTTPResponse(String content, int httpCode)
    {
        this.content = content;
        this.httpCode = httpCode;
    }

    public HTTPResponse(int httpCode)
    {
        this(null, httpCode);
    }

    public String getContent() {
        return content;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
