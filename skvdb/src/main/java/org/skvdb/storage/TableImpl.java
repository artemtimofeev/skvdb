package org.skvdb.storage;

import org.skvdb.common.storage.Table;
import org.skvdb.common.storage.TableMetaData;

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
        container.remove(key);
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
