package org.skvdb.storage;

import org.skvdb.exception.TableAlreadyExistsException;
import org.skvdb.exception.TableNotFoundException;
import org.skvdb.storage.api.Storage;
import org.skvdb.storage.api.Table;
import org.skvdb.storage.api.TableMetaData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StorageImpl implements Storage {
    private List<Table<?>> tables = new ArrayList<>();

    @Override
    public <Value> Table<Value> findTableByName(String name, Class<?> valueClass) throws TableNotFoundException {
        for (Table<?> table : tables) {
            TableMetaData tableMetaData = table.getTableMetaData();
            if (tableMetaData.name().equals(name) && tableMetaData.valueClass().equals(valueClass)) {
                return (Table<Value>) table;
            }
        }
        throw new TableNotFoundException();
    }

    @Override
    synchronized public <Value> Table<Value> createTable(String name, Class<?> valueClass) throws TableAlreadyExistsException {
        if (isTablePresented(name)) {
            throw new TableAlreadyExistsException();
        }
        Table<Value> table = new TableImpl<>(new TableMetaData(name, valueClass));
        tables.add(table);
        return table;
    }

    @Override
    public boolean isTablePresented(String name) {
        for (Table<?> table : tables) {
            TableMetaData tableMetaData = table.getTableMetaData();
            if (tableMetaData.name().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
