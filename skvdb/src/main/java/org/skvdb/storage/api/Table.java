package org.skvdb.storage.api;

public interface Table<Value> {
    Value get(String key);

    void set(String key, Value value);

    void delete(String key);

    boolean containsKey(String key);

    TableMetaData getTableMetaData();
}
