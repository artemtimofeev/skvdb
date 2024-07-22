package org.skvdb.security;

import org.skvdb.dto.AuthenticationDto;
import org.skvdb.dto.AuthenticationResultDto;
import org.skvdb.dto.Dto;
import org.skvdb.dto.RequestResult;
import org.skvdb.util.RandomStringGenerator;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private Map<String, String> userToPassword = new HashMap<>();

    private Map<String, String> tokenToUser = new HashMap<>();

    public AuthenticationService() {
        userToPassword.put("user", "password");
    }

    public synchronized AuthenticationResultDto authenticate(AuthenticationDto authenticationDto) {
        String username = authenticationDto.username();
        String password = authenticationDto.password();
        if (isValidPassword(username, password)) {
            String token = generateToken(username);
            return new AuthenticationResultDto(RequestResult.OK, token);
        }
        return new AuthenticationResultDto(RequestResult.ERROR, null);
    }

    public synchronized boolean isValidToken(Dto dto) {
        return isValidToken(dto.token());
    }

    public synchronized boolean isValidPassword(String username, String password) {
        return userToPassword.containsKey(username) && userToPassword.get(username).equals(password);
    }

    public synchronized boolean isValidToken(String token) {
        return tokenToUser.containsKey(token);
    }

    public synchronized String generateToken(String username) {
        String token = new RandomStringGenerator().generateRandomString(30);
        tokenToUser.put(token, username);
        return token;
    }

}
