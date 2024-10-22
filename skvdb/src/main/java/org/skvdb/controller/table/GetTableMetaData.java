package org.skvdb.controller.table;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.Authorization;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.common.storage.Storage;
import org.skvdb.common.storage.Table;
import org.skvdb.common.storage.TableMetaData;
import org.skvdb.controller.Controller;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Authentication
@ControllerMapping(name = "get_table_meta_data")
@Authorization(anyAuthority = true)
public class GetTableMetaData implements Controller {
    @Autowired
    private Storage storage;

    @Override
    public Result control(Request request) throws Exception {
        Map<String, String> body = request.getBody();
        Table<String> table = storage.findTableByName(body.get("table"), String.class);
        TableMetaData tableMetaData = table.getTableMetaData();
        Map<String, String> answerBody = new HashMap<>();
        throw new Exception();
    }
}
