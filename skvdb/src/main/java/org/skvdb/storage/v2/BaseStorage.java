package org.skvdb.storage.v2;

import org.skvdb.common.exception.TableAlreadyExistsException;
import org.skvdb.common.exception.TableNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseStorage {
    private List<BaseTable> tables = new ArrayList<>();

    public BaseTable findTableByName(String name) throws TableNotFoundException {
        for (BaseTable table : tables) {
            TableMetaData tableMetaData = table.getTableMetaData();
            if (tableMetaData.getName().equals(name)) {
                return table;
            }
        }
        throw new TableNotFoundException();
    }

    public void deleteTable(String name) {
        tables.removeIf(table -> table.getTableMetaData().getName().equals(name));
    }

    synchronized public BaseTable createTable(String name) throws TableAlreadyExistsException {
        if (isTablePresented(name)) {
            throw new TableAlreadyExistsException();
        }
        BaseTable table = new BaseTable(new TableMetaData(name));
        tables.add(table);
        return table;
    }

    public boolean isTablePresented(String name) {
        for (BaseTable table : tables) {
            TableMetaData tableMetaData = table.getTableMetaData();
            if (tableMetaData.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public List<BaseTable> getTables() {
        return tables;
    }
}
