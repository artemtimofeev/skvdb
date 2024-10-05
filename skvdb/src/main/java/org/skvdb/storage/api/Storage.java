package org.skvdb.storage.api;

import org.skvdb.exception.TableAlreadyExistsException;
import org.skvdb.exception.TableNotFoundException;

import java.util.List;
import java.util.Map;

public interface Storage {
    Table<String> findTableStringByName(String name) throws TableNotFoundException;
    Table<List<String>> findTableListByName(String name) throws TableNotFoundException;
    Table<Map<String, String>> findTableMapByName(String name) throws TableNotFoundException;

    Table<String> createStringTable(String name) throws TableAlreadyExistsException;
    Table<List<String>> createListTable(String name) throws TableAlreadyExistsException;
    Table<Map<String, String>> createMapTable(String name) throws TableAlreadyExistsException;

    boolean isTablePresented(String name);
}
