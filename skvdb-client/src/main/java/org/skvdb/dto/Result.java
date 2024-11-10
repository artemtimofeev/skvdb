package org.skvdb.dto;

import java.util.Map;

public class Result {
    private RequestResult requestResult;

    private Map<String, String> body;

    private String errorMessage;

    public Result(RequestResult requestResult, Map<String, String> body, String errorMessage) {
        this.requestResult = requestResult;
        this.body = body;
        this.errorMessage = errorMessage;
    }

    public Result() {}

    public RequestResult getRequestResult() {
        return requestResult;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
