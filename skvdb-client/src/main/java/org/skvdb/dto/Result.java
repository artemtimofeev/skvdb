package org.skvdb.dto;

import java.util.Map;

public class Result {
    private RequestResult requestResult;

    private Map<String, String> body;

    public Result(RequestResult requestResult, Map<String, String> body) {
        this.requestResult = requestResult;
        this.body = body;
    }

    public Result() {}

    public RequestResult getRequestResult() {
        return requestResult;
    }

    public Map<String, String> getBody() {
        return body;
    }
}
