package org.skvdb.service.dto;

import org.skvdb.security.Authority;

import java.util.Set;

public record User(
        String username,
        String passwordHash,
        boolean isSuperuser,
        Set<Authority> authorities
) {
}
