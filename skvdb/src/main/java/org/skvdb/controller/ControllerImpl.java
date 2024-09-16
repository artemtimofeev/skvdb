package org.skvdb.controller;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.dto.Request;
import org.skvdb.dto.Result;
import org.springframework.stereotype.Component;

@Component
@Authentication
public class ControllerImpl implements Controller {

    @Override
    public Result get(Request request) {
        return null;
    }
}
