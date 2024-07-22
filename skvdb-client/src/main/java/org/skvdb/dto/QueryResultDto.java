package org.skvdb.dto;

public record QueryResultDto(RequestResult result, String value) implements Dto {
}
