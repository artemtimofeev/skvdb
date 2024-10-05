package org.skvdb.security;

import org.skvdb.dto.Request;

public interface AuthenticationFilter {
    boolean check(Request request);
}
