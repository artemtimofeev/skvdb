package org.skvdb.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.server.network.dto.Request;
import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.service.SecurityService;
import org.skvdb.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilterImpl implements AuthenticationFilter {
    @Autowired
    private SecurityService securityService;

    private static final Logger logger = LogManager.getLogger();

    @Override
    public synchronized boolean check(Request request) {
        try {
            logger.debug("Пользователь {} проходит аутентификацию", request.getUsername());
            String passwordHash = securityService.getPasswordHash(request.getUsername());
            return HashUtil.comparePasswordAndHash(request.getPassword(), passwordHash);
        } catch (UserNotFoundException e) {
            logger.info(e);
            return false;
        }
    }
}
