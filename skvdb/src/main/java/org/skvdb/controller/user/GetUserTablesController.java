package org.skvdb.controller.user;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.common.dto.User;
import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.common.security.Authority;
import org.skvdb.controller.Controller;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Authentication
@ControllerMapping(name = "get_user_tables")
public class GetUserTablesController implements Controller {
    @Autowired
    private UserServiceImpl userService;

    @Override
    public Result control(Request request) {
        Map<String, String> body = request.getBody();
        String username = body.get("username");
        User user = null;
        try {
            user = userService.getUser(username);
        } catch (UserNotFoundException e) {
            return new Result(e);
        }

        Map<String, String> answer = new HashMap<>();
        for (Authority authority : user.authorities()) {
            answer.put(authority.tableName() + "$" + authority.authorityType(), String.valueOf(authority.authorityType()));
        }

        return new Result(RequestResult.OK, answer);
    }
}
