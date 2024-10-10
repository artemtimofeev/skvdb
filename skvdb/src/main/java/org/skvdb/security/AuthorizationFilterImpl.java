package org.skvdb.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.exception.UserNotFoundException;
import org.skvdb.server.network.dto.Request;
import org.skvdb.service.UserService;
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
