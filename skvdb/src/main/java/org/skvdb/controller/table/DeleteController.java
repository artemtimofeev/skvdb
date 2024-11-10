package org.skvdb.controller.table;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.Authorization;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.common.exception.TableNotFoundException;
import org.skvdb.common.security.AuthorityType;
import org.skvdb.common.storage.Table;
import org.skvdb.controller.Controller;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.storage.v2.BaseStorage;
import org.skvdb.storage.v2.BaseTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Authentication
@ControllerMapping(name = "delete")
@Authorization(authorityType = AuthorityType.WRITE)
public class DeleteController implements Controller {
    @Autowired
    private BaseStorage storage;

    @Override
    public Result control(Request request) {
        Map<String, String> body = request.getBody();
        try {
            BaseTable table = storage.findTableByName(body.get("table"));
            table.delete(body.get("key"));
            return new Result(RequestResult.OK, null);
        } catch (TableNotFoundException e) {
            return new Result(e);
        }
    }
}
