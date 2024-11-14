package org.skvdb.controller.table;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.Authorization;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.common.exception.TableNotFoundException;
import org.skvdb.controller.Controller;
import org.skvdb.common.security.AuthorityType;
import org.skvdb.exception.KeyNotFoundException;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.storage.v2.BaseStorage;
import org.skvdb.storage.v2.BaseTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Authentication
@ControllerMapping(name = "get")
@Authorization(authorityType = AuthorityType.READ)
public class GetController implements Controller {
    @Autowired
    private BaseStorage storage;

    @Override
    public Result control(Request request) {
        Map<String, String> body = request.getBody();

        BaseTable table = null;
        try {
            table = storage.findTableByName(body.get("table"));
        } catch (TableNotFoundException e) {
            return new Result(e);
        }
        String value = null;
        try {
            value = table.get(body.get("key"));
        } catch (KeyNotFoundException e) {
            return new Result(e);
        }

        Map<String, String> answerBody = new HashMap<>();
        answerBody.put("value", value);

        return new Result(RequestResult.OK, answerBody);
    }
}
