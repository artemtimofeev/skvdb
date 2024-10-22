package org.skvdb.common.security;

/**
 *
 * @param authorityType
 * @param tableName
 */
public record Authority(AuthorityType authorityType, String tableName) {
}
