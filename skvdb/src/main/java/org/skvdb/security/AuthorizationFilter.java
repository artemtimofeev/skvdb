package org.skvdb.security;

import org.skvdb.dto.Request;

public interface AuthorizationFilter {
    boolean check(Request request);
}
