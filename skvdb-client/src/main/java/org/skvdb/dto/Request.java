package org.skvdb.dto;

import java.util.Map;

public class Request {
    private String username;

    public Request(String username, String password, String token, String methodName, Map<String, String> body) {
        this.username = username;
        this.password = password;
        this.token = token;
        this.methodName = methodName;
        this.body = body;
    }

    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getMethodName() {
        return methodName;
    }

    public Map<String, String> getBody() {
        return body;
    }

    private String token;

    private String methodName;

    private Map<String, String> body;
}