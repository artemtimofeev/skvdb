package org.skvdb.controller.storage;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.Authorization;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.controller.Controller;
import org.skvdb.exception.TableNotFoundException;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.storage.api.Storage;
import org.skvdb.storage.api.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Authentication
@Authorization(anyAuthority = true)
@ControllerMapping(name = StorageControllerMapping.FIND_TABLE)
public class FindTableController implements Controller {
    @Autowired
    private Storage storage;

    @Override
    public Result control(Request request) {
        String tableName = request.getBody().get("table");
        try {
            Table<String> table = storage.findTableByName(tableName, String.class);
            return new Result(RequestResult.OK, null);
        } catch (TableNotFoundException e) {
            return new Result(RequestResult.ERROR, null);
        }
    }
}
