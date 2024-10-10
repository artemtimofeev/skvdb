package org.skvdb.controller.user;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.annotation.SuperuserController;
import org.skvdb.controller.Controller;
import org.skvdb.exception.UserAlreadyExistsException;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Authentication
@SuperuserController
@ControllerMapping(name = "create_user")
public class CreateUserController implements Controller {
    @Autowired
    private UserService userService;

    @Override
    public Result control(Request request) {
        Map<String, String> body = request.getBody();
        String username = body.get("username");
        String password = body.get("password");
        boolean isSuperuser = Boolean.parseBoolean(body.get("isSuperuser"));
        if (username == null || password == null) {
            return new Result(RequestResult.ERROR, null);
        }
        try {
            userService.createUser(username, password, isSuperuser);
            return new Result(RequestResult.OK, null);
        } catch (UserAlreadyExistsException e) {
            return new Result(RequestResult.ERROR, null);
        }
    }
}
