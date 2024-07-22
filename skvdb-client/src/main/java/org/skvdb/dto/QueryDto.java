package org.skvdb.dto;

public record QueryDto(String type, String tableName, String key, String value, String token) implements Dto {
}
