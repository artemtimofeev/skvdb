package org.skvdb.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.skvdb.dto.Dto;
import org.skvdb.exception.NetworkException;

import java.io.IOException;
import java.util.Base64;

public class DtoConverterService {
    public static String convertToJson(Result result) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Request convertFromJson(String networkPacketString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(networkPacketString, Request.class);
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }
}
