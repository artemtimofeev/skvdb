package org.skvdb.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.skvdb.dto.Dto;
import org.skvdb.exception.NetworkException;

import java.io.IOException;
import java.util.Base64;

public class DtoConverterService {
    public static <T extends Dto> String convertToJson(T dto) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Dto convertFromEncodedJson(String networkPacketString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            NetworkPacket networkPacket = objectMapper.readValue(networkPacketString, NetworkPacket.class);
            return (Dto) objectMapper.readValue(
                    Base64.getDecoder().decode(networkPacket.encodedJsonBody().getBytes()),
                    Class.forName(networkPacket.dtoType())
            );
        } catch (ClassNotFoundException | IOException e) {
            throw new NetworkException(e);
        }
    }
}
