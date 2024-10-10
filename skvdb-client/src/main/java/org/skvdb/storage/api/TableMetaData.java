package org.skvdb.storage.api;

public record TableMetaData(String name, Class<?> valueClass) {
}
