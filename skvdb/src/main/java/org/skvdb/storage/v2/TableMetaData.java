package org.skvdb.storage.v2;

public class TableMetaData {
    private final String name;

    public TableMetaData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
