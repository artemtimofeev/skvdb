package org.skvdb.storage.v2;

import org.skvdb.exception.KeyNotFoundException;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class BaseTable {
    private Map<String, String> container = new TreeMap<>();

    private TableMetaData tableMetaData;

    public BaseTable(TableMetaData tableMetaData) {
        this.tableMetaData = tableMetaData;
    }

    public String get(String key) throws KeyNotFoundException {
        String res = container.get(key);
        if (res == null) {
            throw new KeyNotFoundException();
        }
        return res;
    }

    public void set(String key, String value) {
        container.put(key, value);
    }

    public void delete(String key) {
        container.remove(key);
    }

    public boolean containsKey(String key) {
        return container.containsKey(key);
    }

    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }

    public Iterator<Map.Entry<String, String>> getIterator() {
        return container.entrySet().iterator();
    }
}
