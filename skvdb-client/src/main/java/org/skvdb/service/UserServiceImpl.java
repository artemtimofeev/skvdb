package org.skvdb.service;

import org.skvdb.common.dto.User;
import org.skvdb.common.exception.UserAlreadyExistsException;
import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.common.security.Authority;
import org.skvdb.common.service.UserService;
import org.skvdb.dto.Request;
import org.skvdb.dto.RequestResult;
import org.skvdb.dto.Result;
import org.skvdb.exception.UnsupportedMethodException;
import org.skvdb.network.NetworkService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private final String username;
    private final String password;
    private final NetworkService networkService;

    public UserServiceImpl(String username, String password, NetworkService networkService) {
        this.username = username;
        this.password = password;
        this.networkService = networkService;
    }

    @Override
    public List<User> getAllUsers() {
        throw new UnsupportedMethodException();
    }

    @Override
    public void createUser(String username1, String password1, boolean isSuperuser) {
        Map<String, String> body = new HashMap<>();
        body.put("username", username1);
        body.put("password", password1);
        body.put("isSuperuser", String.valueOf(isSuperuser));
        Request request = new Request(username, password, null, "create_user", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return;
        }
        throw new RuntimeException();
    }

    @Override
    public void deleteUser(String username1) throws UserNotFoundException {
        Map<String, String> body = new HashMap<>();
        body.put("username", username1);
        Request request = new Request(username, password, null, "delete_user", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return;
        }
        throw new RuntimeException();
    }

    @Override
    public void grantAuthority(String username1, Authority authority) throws UserNotFoundException {
        Map<String, String> body = new HashMap<>();
        body.put("username", username1);
        body.put("authority", authority.authorityType().toString());
        body.put("table", authority.tableName());
        Request request = new Request(username, password, null, "grant_authority", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return;
        }
        throw new RuntimeException();
    }

    @Override
    public void revokeAuthority(String username1, Authority authority) throws UserNotFoundException {
        Map<String, String> body = new HashMap<>();
        body.put("username", username1);
        body.put("authority", authority.authorityType().toString());
        body.put("table", authority.tableName());
        Request request = new Request(username, password, null, "revoke_authority", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return;
        }
        throw new RuntimeException();
    }

    @Override
    public void grantSuperuserAuthority(String username1) throws UserNotFoundException {
        Map<String, String> body = new HashMap<>();
        body.put("username", username1);
        Request request = new Request(username, password, null, "grant_superuser_authority", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return;
        }
        throw new RuntimeException();
    }

    @Override
    public void revokeSuperuserAuthority(String username1) throws UserNotFoundException {
        Map<String, String> body = new HashMap<>();
        body.put("username", username1);
        Request request = new Request(username, password, null, "revoke_superuser_authority", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return;
        }
        throw new RuntimeException();
    }

    @Override
    public boolean isSuperuser(String username1) throws UserNotFoundException {
        Map<String, String> body = new HashMap<>();
        body.put("username", username1);
        Request request = new Request(username, password, null, "is_superuser", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return Boolean.parseBoolean(result.getBody().get("result"));
        }
        throw new RuntimeException();
    }

    @Override
    public boolean hasAuthority(String username1, Authority authority) throws UserNotFoundException {
        Map<String, String> body = new HashMap<>();
        body.put("username", username1);
        body.put("authority", authority.authorityType().toString());
        body.put("table", authority.tableName());
        Request request = new Request(username, password, null, "has_authority", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return Boolean.parseBoolean(result.getBody().get("result"));
        }
        throw new RuntimeException();
    }

    @Override
    public boolean hasAnyAuthority(String username1, String tableName) throws UserNotFoundException {
        Map<String, String> body = new HashMap<>();
        body.put("username", username1);
        body.put("table", tableName);
        Request request = new Request(username, password, null, "has_any_authority", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return Boolean.parseBoolean(result.getBody().get("result"));
        }
        throw new RuntimeException();
    }
}
