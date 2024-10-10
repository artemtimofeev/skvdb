package org.skvdb.controller.storage;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.controller.Controller;
import org.skvdb.exception.TableAlreadyExistsException;
import org.skvdb.exception.UserNotFoundException;
import org.skvdb.security.Authority;
import org.skvdb.security.AuthorityType;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.service.UserService;
import org.skvdb.storage.api.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Authentication
@ControllerMapping(name = "create_table")
public class CreateTableController implements Controller {
    @Autowired
    private Storage storage;

    @Autowired
    private UserService userService;

    @Override
    public Result control(Request request) {
        Map<String, String> requestBody = request.getBody();
        String tableName = requestBody.get("table");
        try {
            storage.createTable(tableName, String.class);
            userService.grantAuthority(request.getUsername(), new Authority(AuthorityType.READ, tableName));
            userService.grantAuthority(request.getUsername(), new Authority(AuthorityType.WRITE, tableName));
            userService.grantAuthority(request.getUsername(), new Authority(AuthorityType.OWNER, tableName));
            return new Result(RequestResult.OK, null);
        } catch (TableAlreadyExistsException | UserNotFoundException e) {
            return new Result(RequestResult.ERROR, null);
        }
    }
}
