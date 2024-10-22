package org.skvdb.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.common.exception.UserNotFoundException;
import org.skvdb.common.security.Authority;
import org.skvdb.common.security.AuthorityType;
import org.skvdb.common.service.UserService;
import org.skvdb.server.network.dto.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationFilterImpl implements AuthorizationFilter {
    @Autowired
    private UserService userService;

    private static final Logger logger = LogManager.getLogger();

    @Override
    public boolean check(Request request, AuthorityType authorityType, boolean anyAuthority) {
        logger.debug("Пользователь {} проходит авторизацию", request.getUsername());

        String tableName = request.getBody().get("table");
        String username = request.getUsername();

        try {
            return userService.hasAuthority(username, new Authority(authorityType, tableName))
                    || userService.isSuperuser(username)
                    || (anyAuthority && userService.hasAnyAuthority(username, tableName));
        } catch (UserNotFoundException e) {
            logger.info(e);
        }
        return false;
    }
}
