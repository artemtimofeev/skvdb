package org.skvdb.controller.storage;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.common.exception.TableAlreadyExistsException;
import org.skvdb.common.service.UserService;
import org.skvdb.controller.Controller;
import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.common.security.Authority;
import org.skvdb.common.security.AuthorityType;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.storage.v2.BaseStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Authentication
@ControllerMapping(name = StorageControllerMapping.CREATE_TABLE)
public class CreateTableController implements Controller {
    @Autowired
    private BaseStorage storage;

    @Autowired
    private UserService userService;

    @Override
    public Result control(Request request) {
        Map<String, String> body = request.getBody();
        try {
            storage.createTable(body.get("table"));
        } catch (TableAlreadyExistsException e) {
            return new Result(e);
        }

        try {
            userService.grantAuthority(request.getUsername(), new Authority(AuthorityType.READ, body.get("table")));
            userService.grantAuthority(request.getUsername(), new Authority(AuthorityType.WRITE, body.get("table")));
            userService.grantAuthority(request.getUsername(), new Authority(AuthorityType.OWNER, body.get("table")));
        } catch (UserNotFoundException e) {
            return new Result(e);
        }

        return new Result(RequestResult.OK, null);
    }
}
