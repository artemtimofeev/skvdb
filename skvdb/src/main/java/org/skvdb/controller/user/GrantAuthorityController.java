package org.skvdb.controller.user;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.Authorization;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.common.security.Authority;
import org.skvdb.common.security.AuthorityType;
import org.skvdb.common.service.UserService;
import org.skvdb.controller.Controller;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Authorization(authorityType = AuthorityType.OWNER)
@Authentication
@ControllerMapping(name = "grant_authority")
public class GrantAuthorityController implements Controller {
    @Autowired
    private UserServiceImpl userService;

    @Override
    public Result control(Request request) {
        Map<String, String> body = request.getBody();
        try {
            userService.grantAuthority(
                    body.get("username"),
                    new Authority(AuthorityType.valueOf(body.get("authority")), body.get("table"))
            );
        } catch (UserNotFoundException e) {
            return new Result(e);
        }
        return new Result(RequestResult.OK, null);
    }
}
