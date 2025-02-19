package org.skvdb.storage;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QueryHandler {
    private Map<String, String> container;

    private Storage1 storage;

    public QueryHandler() {
        storage = new Storage1();
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

    public void close() {
        storage.close();
    }
}
