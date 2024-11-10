package org.skvdb.controller;

import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.Result;

public interface Controller {
    Result control(Request request);
}
