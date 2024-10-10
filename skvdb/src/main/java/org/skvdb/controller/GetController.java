package org.skvdb.controller;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.Authorization;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.annotation.SuperuserController;
import org.skvdb.exception.TableNotFoundException;
import org.skvdb.security.AuthorityType;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.storage.api.Storage;
import org.skvdb.storage.api.Table;
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
    private Storage storage;

    @Override
    public Result control(Request request) {
        Map<String, String> requestBody = request.getBody();
        String key = requestBody.get("key");
        String tableName = requestBody.get("table");
        Map<String, String> answerBody = new HashMap<>();

        try {
            Table<String> table = storage.findTableByName(tableName, String.class);
            String value = table.get(key);
            answerBody.put("value", value);
            return new Result(RequestResult.OK, answerBody);
        } catch (TableNotFoundException e) {
            return new Result(RequestResult.ERROR, null);
        }
    }
}
