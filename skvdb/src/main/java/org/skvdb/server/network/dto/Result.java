package org.skvdb.server.network.dto;

import java.util.Map;

public class Result {
    private RequestResult requestResult;

    private Map<String, String> body;

    private String errorMessage;

    public Result(RequestResult requestResult, Map<String, String> body) {
        this.requestResult = requestResult;
        this.body = body;
    }

    public Result(Exception e) {
        this.requestResult = RequestResult.ERROR;
        this.errorMessage = e.toString();
    }

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
