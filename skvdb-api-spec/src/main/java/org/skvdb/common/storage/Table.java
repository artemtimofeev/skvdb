package org.skvdb.common.storage;

import java.util.Iterator;
import java.util.Map;

public interface Table<Value> {
    /**
     *
     * @param key
     * @return
     */
    Value get(String key);

    /**
     *
     * @param key
     * @param value
     */
    void set(String key, Value value);

    /**
     *
     * @param key
     */
    void delete(String key);

    /**
     *
     * @param key
     * @return
     */
    boolean containsKey(String key);

    /**
     *
     * @return
     */
    TableMetaData getTableMetaData();

    Iterator<Map.Entry<String, Value>> getIterator();

}
