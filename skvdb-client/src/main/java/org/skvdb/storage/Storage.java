package org.skvdb.storage;

import org.skvdb.common.exception.TableAlreadyExistsException;
import org.skvdb.common.exception.TableNotFoundException;
import org.skvdb.common.storage.TableMetaData;
import org.skvdb.dto.Request;
import org.skvdb.dto.RequestResult;
import org.skvdb.dto.Result;
import org.skvdb.network.NetworkService;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    private final NetworkService networkService;
    private final String username;
    private final String password;

    public Storage(String username, String password, NetworkService networkService) {
        this.networkService = networkService;
        this.username = username;
        this.password = password;
    }

    public Table findTableByName(String name) throws TableNotFoundException {
        Map<String, String> body = new HashMap<>();
        body.put("table", name);
        Request request = new Request(username, password, null, "find_table_by_name", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            TableMetaData tableMetaData = new TableMetaData(name, null);
            return new Table(networkService, tableMetaData, username, password);
        }
        if (result.getErrorMessage().equals("org.skvdb.common.exception.TableNotFoundException")) {
            throw new TableNotFoundException();
        }
        throw new RuntimeException(result.getErrorMessage());
    }

    public Table createTable(String name) throws TableAlreadyExistsException {
        Map<String, String> body = new HashMap<>();
        body.put("table", name);
        Request request = new Request(username, password, null, "create_table", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            TableMetaData tableMetaData = new TableMetaData(name, null);
            return new Table(networkService, tableMetaData, username, password);
        }
        if (result.getErrorMessage().equals("org.skvdb.common.exception.TableAlreadyExistsException")) {
            throw new TableAlreadyExistsException();
        }
        throw new RuntimeException(result.getErrorMessage());
    }

    public boolean isTablePresented(String name) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        Request request = new Request(username, password, null, "is_table_presented", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return Boolean.parseBoolean(result.getBody().get("result"));
        }
        throw new RuntimeException(result.getErrorMessage());
    }
}
