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
    public Entry<Value> get(String key) {
        return new Entry<>(key, container.get(key));
    }

    @Override
    public void set(String key, Value value) {
        container.put(key, value);
    }

    @Override
    public void set(Entry<Value> entry) {
        container.put(entry.key(), entry.value());
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void delete(Entry<Value> entry) {

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
