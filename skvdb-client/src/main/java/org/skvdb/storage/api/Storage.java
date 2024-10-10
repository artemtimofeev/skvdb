package org.skvdb.storage.api;

public interface Storage {
    <Value> Table<Value> findTableByName(String name, Class<?> valueClass) throws TableNotFoundException;

    <Value> Table<Value> createTable(String name, Class<?> valueClass) throws TableAlreadyExistsException;

    boolean isTablePresented(String name);
}
