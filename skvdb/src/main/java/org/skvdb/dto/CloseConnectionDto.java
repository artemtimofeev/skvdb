package org.skvdb.dto;

public record CloseConnectionDto(String close, String token) implements Dto {
}
