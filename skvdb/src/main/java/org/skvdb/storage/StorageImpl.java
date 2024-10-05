package org.skvdb.storage;

import org.skvdb.exception.TableAlreadyExistsException;
import org.skvdb.exception.TableNotFoundException;
import org.skvdb.storage.api.Storage;
import org.skvdb.storage.api.Table;
import org.skvdb.storage.api.TableMetaData;
import org.skvdb.storage.api.ValueType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StorageImpl implements Storage {
    private List<Table<String>> tableStringList = new ArrayList<>();
    private List<Table<Map<String, String>>> tableMapList = new ArrayList<>();
    private List<Table<List<String>>> tableListList = new ArrayList<>();

    private <T> Table<T> findTableByName(String name, List<Table<T>> tableList) throws TableNotFoundException {
        for (Table<T> table : tableList) {
            TableMetaData tableMetaData = table.getTableMetaData();
            if (tableMetaData.name().equals(name)) {
                return table;
            }
        }
        throw new TableNotFoundException();
    }

    synchronized private <T> Table<T> createTable(String name, ValueType valueType) throws TableAlreadyExistsException {
        if (isTablePresented(name)) {
            throw new TableAlreadyExistsException();
        }
        return new TableImpl<>(new TableMetaData(name, valueType));
    }

    @Override
    public boolean isTablePresented(String name) {
        for (Table<?> table : tableMapList) {
            TableMetaData tableMetaData = table.getTableMetaData();
            if (tableMetaData.name().equals(name)) {
                return true;
            }
        }
        for (Table<?> table : tableStringList) {
            TableMetaData tableMetaData = table.getTableMetaData();
            if (tableMetaData.name().equals(name)) {
                return true;
            }
        }
        for (Table<?> table : tableListList) {
            TableMetaData tableMetaData = table.getTableMetaData();
            if (tableMetaData.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Table<String> findTableStringByName(String name) throws TableNotFoundException {
        return findTableByName(name, tableStringList);
    }

    @Override
    public Table<List<String>> findTableListByName(String name) throws TableNotFoundException {
        return findTableByName(name, tableListList);
    }

    @Override
    public Table<Map<String, String>> findTableMapByName(String name) throws TableNotFoundException {
        return findTableByName(name, tableMapList);
    }

    @Override
    public Table<String> createStringTable(String name) throws TableAlreadyExistsException {
        Table<String> table = createTable(name, ValueType.STRING);
        tableStringList.add(table);
        return table;
    }

    @Override
    public Table<List<String>> createListTable(String name) throws TableAlreadyExistsException {
        Table<List<String>> table = createTable(name, ValueType.LIST);
        tableListList.add(table);
        return table;
    }

    @Override
    public Table<Map<String, String>> createMapTable(String name) throws TableAlreadyExistsException {
        Table<Map<String, String>> table = createTable(name, ValueType.MAP);
        tableMapList.add(table);
        return table;
    }


}
