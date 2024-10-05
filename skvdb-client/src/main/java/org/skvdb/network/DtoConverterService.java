package org.skvdb.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.skvdb.dto.Request;
import org.skvdb.dto.Result;
import org.skvdb.exception.NetworkException;

import java.io.IOException;

public class DtoConverterService {
    public static String convertToJson(Request request) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Result convertFromJson(String networkPacketString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(networkPacketString, Result.class);
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }
}
