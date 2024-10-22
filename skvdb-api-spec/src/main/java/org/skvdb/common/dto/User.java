package org.skvdb.common.dto;

import org.skvdb.common.security.Authority;

import java.util.Set;

/**
 *
 * @param username
 * @param passwordHash
 * @param isSuperuser
 * @param authorities
 */
public record User(
        String username,
        String passwordHash,
        boolean isSuperuser,
        boolean isProtected,
        Set<Authority> authorities
) {
}
