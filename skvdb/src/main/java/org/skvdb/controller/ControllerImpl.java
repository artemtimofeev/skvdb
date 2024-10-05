package org.skvdb.controller;

import org.skvdb.annotation.Authentication;
import org.skvdb.annotation.ControllerMapping;
import org.skvdb.dto.Request;
import org.skvdb.dto.RequestResult;
import org.skvdb.dto.Result;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Authentication
public class ControllerImpl implements Controller {

    @Override
    public Result get(Request request) {
        Map<String, String> answerBody = new HashMap<>();
        answerBody.put("value", "rrrrrrrrr123qwe");
        System.out.println("kekeke");
        return new Result(RequestResult.OK, answerBody);
    }
}
