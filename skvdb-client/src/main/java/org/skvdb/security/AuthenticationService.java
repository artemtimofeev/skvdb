package org.skvdb.security;

import org.skvdb.network.NetworkService;

public class AuthenticationService {
    private final NetworkService networkService;

    public AuthenticationService(NetworkService networkService) {
        this.networkService = networkService;
    }

    public String authenticate(String username, String password) {
        return "token";
    }
}
