package org.skvdb.controller.table;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.Authorization;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.controller.Controller;
import org.skvdb.exception.TableNotFoundException;
import org.skvdb.security.AuthorityType;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.storage.api.Storage;
import org.skvdb.storage.api.Table;
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
    public Result control(Request request) {
        Map<String, String> requestBody = request.getBody();
        String key = requestBody.get("key");
        String value = requestBody.get("value");
        String tableName = requestBody.get("table");

        try {
            Table<String> table = storage.findTableByName(tableName, String.class);
            table.set(key, value);
            return new Result(RequestResult.OK, null);
        } catch (TableNotFoundException e) {
            return new Result(RequestResult.ERROR, null);
        }
    }
}
