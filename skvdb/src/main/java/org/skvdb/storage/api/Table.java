package org.skvdb.storage.api;

public interface Table<Value> {
    Entry<Value> get(String key);

    void set(String key, Value value);

    void set(Entry<Value> entry);

    void delete(String key);

    void delete(Entry<Value> entry);

    boolean containsKey(String key);

    TableMetaData getTableMetaData();
}
