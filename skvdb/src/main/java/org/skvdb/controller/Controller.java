package org.skvdb.controller;

import org.skvdb.dto.Request;
import org.skvdb.dto.Result;

public interface Controller {
    Result get(Request request);
}
