package org.skvdb.storage.api;

import org.skvdb.exception.TableAlreadyExistsException;
import org.skvdb.exception.TableNotFoundException;

public interface Storage {
    <Value> Table<Value> findTableByName(String name, Class<?> valueClass) throws TableNotFoundException;

    <Value> Table<Value> createTable(String name, Class<?> valueClass) throws TableAlreadyExistsException;

    boolean isTablePresented(String name);
}
