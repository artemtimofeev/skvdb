package org.skvdb.dto;

public record AuthenticationDto(String username, String password) implements Dto {
}
