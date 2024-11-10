package org.skvdb.service.dao;

import org.skvdb.storage.v2.BaseStorage;
import org.skvdb.storage.v2.BaseTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableDao {
    @Autowired
    private BaseStorage storage;

    public List<BaseTable> getTables() {
        return storage.getTables();
    }
}
