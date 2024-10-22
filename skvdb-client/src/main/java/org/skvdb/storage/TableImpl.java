package org.skvdb.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.skvdb.common.storage.Table;
import org.skvdb.common.storage.TableMetaData;
import org.skvdb.dto.Request;
import org.skvdb.dto.RequestResult;
import org.skvdb.dto.Result;
import org.skvdb.exception.NetworkException;
import org.skvdb.network.NetworkService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TableImpl<Value> implements Table<Value> {
    private final NetworkService networkService;
    private final TableMetaData tableMetaData;

    private final String username;
    private final String password;

    public TableImpl(NetworkService networkService, TableMetaData tableMetaData, String username, String password) {
        this.networkService = networkService;
        this.tableMetaData = tableMetaData;
        this.username = username;
        this.password = password;
    }

    @Override
    public Value get(String key) {
        Map<String, String> body = new HashMap<>();
        body.put("key", key);
        body.put("table", tableMetaData.name());
        Request request = new Request(username, password, null, "get", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return convertFromJson(result.getBody().get("value"));
        }
        throw new RuntimeException();
    }

    private Value convertFromJson(String value) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (Value) objectMapper.readValue(value, tableMetaData.valueClass());
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    @Override
    public void set(String key, Value value) {
        Map<String, String> body = new HashMap<>();
        body.put("key", key);
        body.put("value", convertToJson(value));
        body.put("table", tableMetaData.name());
        Request request = new Request(username, password, null, "set", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return;
        }
        throw new RuntimeException();
    }

    public String convertToJson(Value value) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public boolean containsKey(String key) {
        return false;
    }

    @Override
    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }
}
