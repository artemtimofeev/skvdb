package org.skvdb.common.storage;

import org.skvdb.common.exception.TableAlreadyExistsException;
import org.skvdb.common.exception.TableNotFoundException;

public interface Storage {
    /**
     *
     * @param name
     * @param valueClass
     * @return
     * @param <Value>
     * @throws TableNotFoundException
     */
    <Value> Table<Value> findTableByName(String name, Class<?> valueClass) throws TableNotFoundException;

    /**
     *
     * @param name
     * @param valueClass
     * @return
     * @param <Value>
     * @throws TableAlreadyExistsException
     */
    <Value> Table<Value> createTable(String name, Class<?> valueClass) throws TableAlreadyExistsException;

    /**
     *
     * @param name
     * @return
     */
    boolean isTablePresented(String name);
}
