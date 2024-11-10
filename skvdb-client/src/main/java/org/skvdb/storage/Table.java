package org.skvdb.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.skvdb.common.storage.TableMetaData;
import org.skvdb.dto.Request;
import org.skvdb.dto.RequestResult;
import org.skvdb.dto.Result;
import org.skvdb.network.NetworkService;

import java.util.HashMap;
import java.util.Map;

public class Table {
    private final NetworkService networkService;
    private final TableMetaData tableMetaData;

    private final String username;
    private final String password;

    public Table(NetworkService networkService, TableMetaData tableMetaData, String username, String password) {
        this.networkService = networkService;
        this.tableMetaData = tableMetaData;
        this.username = username;
        this.password = password;
    }

    public String get(String key) {
        Map<String, String> body = new HashMap<>();
        body.put("key", key);
        body.put("table", tableMetaData.name());
        Request request = new Request(username, password, null, "get", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return result.getBody().get("value");
        }
        throw new RuntimeException();
    }

    public void set(String key, String value) {
        Map<String, String> body = new HashMap<>();
        body.put("key", key);
        body.put("value", value);
        body.put("table", tableMetaData.name());
        Request request = new Request(username, password, null, "set", body);
        Result result = networkService.send(request);
        if (result.getRequestResult().equals(RequestResult.OK)) {
            return;
        }
        throw new RuntimeException();
    }

    public String convertToJson(String value) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String key) {

    }

    public boolean containsKey(String key) {
        return false;
    }

    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }
}
