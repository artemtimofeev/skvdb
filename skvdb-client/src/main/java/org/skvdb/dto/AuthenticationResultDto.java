package org.skvdb.dto;

public record AuthenticationResultDto(RequestResult result, String token) implements Dto{
}
