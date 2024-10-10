package org.skvdb.storage;

import org.skvdb.storage.api.Entry;
import org.skvdb.storage.api.Table;
import org.skvdb.storage.api.TableMetaData;

import java.util.Map;
import java.util.TreeMap;

public class TableImpl<Value> implements Table<Value> {

    private Map<String, Value> container = new TreeMap<>();

    private TableMetaData tableMetaData;

    public TableImpl(TableMetaData tableMetaData) {
        this.tableMetaData = tableMetaData;
    }

    @Override
    public Value get(String key) {
        return container.get(key);
    }

    @Override
    public void set(String key, Value value) {
        container.put(key, value);
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public boolean containsKey(String key) {
        return container.containsKey(key);
    }

    @Override
    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }
}
