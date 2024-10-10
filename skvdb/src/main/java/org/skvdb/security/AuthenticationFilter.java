package org.skvdb.security;

import org.skvdb.server.network.dto.Request;

public interface AuthenticationFilter {
    boolean check(Request request);
}
