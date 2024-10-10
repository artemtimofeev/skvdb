package org.skvdb.storage;

import org.skvdb.dto.Request;
import org.skvdb.dto.RequestResult;
import org.skvdb.dto.Result;
import org.skvdb.network.NetworkService;
import org.skvdb.storage.api.*;

import java.util.HashMap;
import java.util.Map;

public class StorageImpl implements Storage {
    private final NetworkService networkService;
    private final String username;
    private final String password;

    public StorageImpl(NetworkService networkService, String username, String password) {
        this.networkService = networkService;
        this.username = username;
        this.password = password;
    }

    @Override
    public <Value> Table<Value> findTableByName(String name, Class<?> valueClass) throws TableNotFoundException {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("valueClass", valueClass.getName());
        Request request = new Request(username, password, null, "find_table_by_name", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            TableMetaData tableMetaData = new TableMetaData(name, valueClass);
            return new TableImpl<>(networkService, tableMetaData, username, password);
        }
        throw new RuntimeException();
    }

    @Override
    public <Value> Table<Value> createTable(String name, Class<?> valueClass) throws TableAlreadyExistsException {
        Map<String, String> body = new HashMap<>();
        body.put("table", name);
        body.put("valueClass", valueClass.getName());
        Request request = new Request(username, password, null, "create_table", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            TableMetaData tableMetaData = new TableMetaData(name, valueClass);
            return new TableImpl<>(networkService, tableMetaData, username, password);
        }
        throw new RuntimeException();
    }

    @Override
    public boolean isTablePresented(String name) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        Request request = new Request(username, password, null, "is_table_presented", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return Boolean.parseBoolean(result.getBody().get("result"));
        }
        throw new RuntimeException();
    }
}
