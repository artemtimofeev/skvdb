package org.skvdb.server;

import org.skvdb.dto.QueryDto;
import org.skvdb.dto.QueryResultDto;
import org.skvdb.dto.RequestResult;

import java.util.Map;

public class QueryHandler {
    private Map<String, String> container;

    private Storage storage;

    public QueryHandler() {
        storage = new Storage();
        container = storage.readFromDisk();
        storage.initWriter();
        storage.dumpToStorage(container);
    }

    private synchronized String get(String key) {
        return container.get(key);
    }

    private synchronized String set(String key, String value) {
        storage.addToDisk(key, value);
        return container.put(key, value);
    }

    public QueryResultDto handleQuery(QueryDto queryDto) {
        String key = queryDto.key();
        String value = queryDto.value();
        String type = queryDto.type();

        if (type.equals("get")) {
            return new QueryResultDto(RequestResult.OK, get(key));
        }
        if (type.equals("set")) {
            return new QueryResultDto(RequestResult.OK, set(key, value));
        }
        return new QueryResultDto(RequestResult.ERROR, "Unknown method");
    }

    public void close() {
        storage.close();
    }
}
