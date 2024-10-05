package org.skvdb.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.dto.Request;
import org.skvdb.exception.UserDoesNotExistsException;
import org.skvdb.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilterImpl implements AuthenticationFilter {
    @Autowired
    private AuthenticationService authenticationService;

    private static final Logger logger = LogManager.getLogger();

    @Override
    public synchronized boolean check(Request request) {
        try {
            logger.debug("Пользователь {} проходит аутентификацию", request.getUsername());
            String passwordHash = authenticationService.getPasswordHash(request.getUsername());
            return HashUtil.comparePasswordAndHash(request.getPassword(), passwordHash);
        } catch (UserDoesNotExistsException e) {
            return false;
        }
    }
}
