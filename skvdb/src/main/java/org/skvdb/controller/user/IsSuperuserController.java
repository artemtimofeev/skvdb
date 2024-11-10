package org.skvdb.controller.user;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.annotation.SuperuserController;
import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.common.service.UserService;
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
@ControllerMapping(name = "is_superuser")
@SuperuserController
public class IsSuperuserController implements Controller {
    @Autowired
    private UserServiceImpl userService;

    @Override
    public Result control(Request request) {
        Map<String, String> body = request.getBody();
        boolean result = false;
        try {
            result = userService.isSuperuser(body.get("username"));
        } catch (UserNotFoundException e) {
            return new Result(e);
        }
        Map<String, String> answerBody = new HashMap<>();
        answerBody.put("result", String.valueOf(result));
        return new Result(RequestResult.OK, answerBody);
    }
}
