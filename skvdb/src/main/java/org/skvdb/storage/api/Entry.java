package org.skvdb.storage.api;

public record Entry<Value>(String key, Value value) {
}
