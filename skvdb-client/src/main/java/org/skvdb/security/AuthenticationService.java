package org.skvdb.security;

import org.skvdb.dto.AuthenticationDto;
import org.skvdb.dto.AuthenticationResultDto;
import org.skvdb.dto.RequestResult;
import org.skvdb.exception.AuthenticationException;
import org.skvdb.network.NetworkService;

public class AuthenticationService {
    private final NetworkService networkService;

    public AuthenticationService(NetworkService networkService) {
        this.networkService = networkService;
    }

    public String authenticate(String username, String password) {
        AuthenticationResultDto authenticationResultDto = (AuthenticationResultDto) networkService.send(new AuthenticationDto(username, password));
        if (authenticationResultDto.result().equals(RequestResult.OK)) {
            return authenticationResultDto.token();
        }
        throw new AuthenticationException("Username or password is invalid");
    }
}
