package org.skvdb.storage.v2;

import org.skvdb.common.exception.TableAlreadyExistsException;
import org.skvdb.common.exception.TableNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StructedStorage {
    @Autowired
    private BaseStorage storage;

    public <Value> StructedTable<Value> findTableByName(String name, Class<?> valueClass) throws TableNotFoundException {
        BaseTable table = storage.findTableByName(name);
        return new StructedTable<>(table, valueClass);
    }

    synchronized public <Value> StructedTable<Value> createTable(String name, Class<?> valueClass) throws TableAlreadyExistsException {
        BaseTable table = storage.createTable(name);
        return new StructedTable<>(table, valueClass);
    }

    public boolean isTablePresented(String name) {
        return storage.isTablePresented(name);
    }

    public List<BaseTable> getTables() {
        return storage.getTables();
    }
}
