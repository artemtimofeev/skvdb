package org.skvdb.dto;

public interface Dto {
    default String token() {
        return null;
    }
}
