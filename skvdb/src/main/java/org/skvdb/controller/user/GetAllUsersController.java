package org.skvdb.controller.user;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.annotation.SuperuserController;
import org.skvdb.common.dto.User;
import org.skvdb.common.exception.UserAlreadyExistsException;
import org.skvdb.controller.Controller;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Authentication
@SuperuserController
@ControllerMapping(name = "get_all_users")
public class GetAllUsersController implements Controller {
    @Autowired
    private UserServiceImpl userService;

    @Override
    public Result control(Request request) {
        Map<String, String> body = new HashMap<>();
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            body.put(user.username(), String.valueOf(user.isSuperuser()));
        }

        return new Result(RequestResult.OK, body);
    }
}
