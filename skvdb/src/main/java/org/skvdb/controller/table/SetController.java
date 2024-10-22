package org.skvdb.controller.table;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.Authorization;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.common.exception.TableNotFoundException;
import org.skvdb.controller.Controller;
import org.skvdb.common.security.AuthorityType;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.common.storage.Storage;
import org.skvdb.common.storage.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Authentication
@ControllerMapping(name = "set")
@Authorization(authorityType = AuthorityType.WRITE)
public class SetController implements Controller {
    @Autowired
    private Storage storage;

    @Override
    public Result control(Request request) throws TableNotFoundException {
        Map<String, String> body = request.getBody();

        Table<String> table = storage.findTableByName(body.get("table"), String.class);
        table.set(body.get("key"), body.get("value"));
        return new Result(RequestResult.OK, null);
    }
}
