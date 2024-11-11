package org.skvdb.controller.storage;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.Authorization;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.common.dto.User;
import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.common.security.Authority;
import org.skvdb.common.security.AuthorityType;
import org.skvdb.common.service.UserService;
import org.skvdb.controller.Controller;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.storage.v2.BaseStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Authentication
@ControllerMapping(name = "delete_table")
@Authorization(authorityType = AuthorityType.OWNER)
public class DeleteTableController implements Controller {
    @Autowired
    private BaseStorage storage;

    @Autowired
    private UserService userService;

    @Override
    public Result control(Request request) {
        Map<String, String> body = request.getBody();
        String tableToDelete = body.get("table");
        List<User> users = userService.getAllUsers();

        for (User user : users) {
            try {
                if (userService.hasAnyAuthority(user.username(), tableToDelete)) {
                    userService.revokeAuthority(user.username(), new Authority(AuthorityType.READ, tableToDelete));
                    userService.revokeAuthority(user.username(), new Authority(AuthorityType.WRITE, tableToDelete));
                    userService.revokeAuthority(user.username(), new Authority(AuthorityType.OWNER, tableToDelete));
                }
            } catch (UserNotFoundException e) {
                return new Result(e);
            }
        }
        storage.deleteTable(tableToDelete);
        return new Result(RequestResult.OK, null);
    }
}
