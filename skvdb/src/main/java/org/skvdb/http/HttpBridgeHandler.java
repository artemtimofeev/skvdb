package org.skvdb.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.skvdb.exception.BadRequestException;
import org.skvdb.exception.ControllerNotFoundException;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.Result;
import org.skvdb.service.ControllerMappingService;
import org.skvdb.util.ResultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
public class HttpBridgeHandler implements HttpHandler {
    @Autowired
    private ControllerMappingService controllerMappingService;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Request request = parseRequest(exchange);

        try {
            Result result = resolveRequest(request);
            String converted = ResultConverter.convertToJson(result);
            exchange.sendResponseHeaders(200, converted.length());
            OutputStream os = exchange.getResponseBody();
            os.write(converted.getBytes());
            os.close();
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    private Result resolveRequest(Request request) throws BadRequestException {
        String methodName = request.getMethodName();
        try {
            return controllerMappingService.getController(methodName).control(request);
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
    }

    private Request parseRequest(HttpExchange exchange) throws IOException {
        List<String> authorization = exchange.getRequestHeaders().get("Authorization");
        String encodedUsernamePassword = authorization.get(0).split(" ")[1];
        String decodedUsernamePassword = new String(Base64.getDecoder().decode(encodedUsernamePassword));

        String username = decodedUsernamePassword.split(":")[0];
        String password = decodedUsernamePassword.split(":")[1];

        String method = exchange.getRequestURI().getPath().substring(1);

        Map<String, String> body = parseFromJson(new String(exchange.getRequestBody().readAllBytes()));
        return new Request(username, password, null, method, body);
    }

    public static Map<String, String> parseFromJson(String networkPacketString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(networkPacketString, Map.class);
    }
}
