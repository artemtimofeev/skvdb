package org.skvdb.security;

import org.skvdb.dto.Request;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilterMock implements AuthenticationFilter {
    @Override
    public synchronized boolean check(Request request) {
        return true;
    }
}
