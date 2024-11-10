package org.skvdb.controller.storage;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.controller.Controller;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.RequestResult;
import org.skvdb.server.network.dto.Result;
import org.skvdb.service.dao.TableDao;
import org.skvdb.storage.v2.BaseTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Authentication
@ControllerMapping(name = "get_all_tables")
public class GetAllTablesController implements Controller {
    @Autowired
    private TableDao tableDao;

    @Override
    public Result control(Request request) {
        List<BaseTable> tableList = tableDao.getTables();
        Map<String, String> body = new HashMap<>();

        for (BaseTable table : tableList) {
            body.put(table.getTableMetaData().getName(), "");
        }
        return new Result(RequestResult.OK, body);
    }
}
