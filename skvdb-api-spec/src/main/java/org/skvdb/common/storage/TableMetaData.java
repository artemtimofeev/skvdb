package org.skvdb.common.storage;

/**
 *
 * @param name
 * @param valueClass
 */
public record TableMetaData(String name, Class<?> valueClass) {
}
