package org.skvdb.controller.user;

import org.skvdb.controller.Controller;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.Result;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserController implements Controller {
    @Override
    public Result control(Request request) {
        return null;
    }
}
