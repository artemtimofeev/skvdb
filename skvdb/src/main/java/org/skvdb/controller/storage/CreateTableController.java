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
import org.skvdb.common.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Authentication
@ControllerMapping(name = StorageControllerMapping.CREATE_TABLE)
public class CreateTableController implements Controller {
    @Autowired
    private Storage storage;

    @Autowired
    private UserService userService;

    @Override
    public Result control(Request request) throws TableAlreadyExistsException, UserNotFoundException {
        Map<String, String> body = request.getBody();
        storage.createTable(body.get("table"), String.class);
        userService.grantAuthority(request.getUsername(), new Authority(AuthorityType.READ, body.get("table")));
        userService.grantAuthority(request.getUsername(), new Authority(AuthorityType.WRITE, body.get("table")));
        userService.grantAuthority(request.getUsername(), new Authority(AuthorityType.OWNER, body.get("table")));
        return new Result(RequestResult.OK, null);
    }
}
