package org.skvdb.security;

import org.skvdb.server.network.dto.Request;

public interface AuthorizationFilter {
    boolean check(Request request, AuthorityType authorityType);
}
