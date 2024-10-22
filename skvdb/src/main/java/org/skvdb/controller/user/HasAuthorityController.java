package org.skvdb.controller.user;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.annotation.SuperuserController;
import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.common.security.Authority;
import org.skvdb.common.security.AuthorityType;
import org.skvdb.common.service.UserService;
import org.skvdb.controller.Controller;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Authentication
@ControllerMapping(name = "has_authority")
@SuperuserController
public class HasAuthorityController implements Controller {
    @Autowired
    private UserService userService;

    @Override
    public Result control(Request request) throws UserNotFoundException {
        Map<String, String> body = request.getBody();
        boolean result = userService.hasAuthority(
                body.get("username"),
                new Authority(AuthorityType.valueOf(body.get("authority")), body.get("table"))
        );
        Map<String, String> answerBody = new HashMap<>();
        answerBody.put("result", String.valueOf(result));
        return new Result(RequestResult.OK, answerBody);
    }
}
