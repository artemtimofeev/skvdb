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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Authentication
@SuperuserController
@ControllerMapping(name = "revoke_superuser_authority")
public class RevokeSuperuserAuthorityController implements Controller {
    @Autowired
    private UserService userService;

    @Override
    public Result control(Request request) throws UserNotFoundException {
        Map<String, String> body = request.getBody();
        userService.revokeSuperuserAuthority(body.get("username"));
        return new Result(RequestResult.OK, null);
    }
}
