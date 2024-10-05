package org.skvdb.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.skvdb.dto.Request;

public class RequestParser {
    public static Request parseFromJson(String networkPacketString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(networkPacketString, Request.class);
    }
}
