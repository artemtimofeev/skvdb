package org.skvdb.server.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.skvdb.dto.Request;
import org.skvdb.dto.RequestResult;
import org.skvdb.dto.Result;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkServiceTest {
    private static NetworkService networkService;
    private static OutputStream outputStream;
    private static Socket socket;

    private static ArgumentCaptor<Integer> captor;
    private static String requestString;
    private static Request request;

    public static void prepareSocketMock() throws IOException {
        socket = Mockito.mock(Socket.class);
        Mockito.doReturn(new ByteArrayInputStream( requestString.getBytes())).when(socket).getInputStream();

        captor = ArgumentCaptor.forClass(Integer.class);
        outputStream = Mockito.spy(OutputStream.class);
        Mockito.doNothing().when(outputStream).write(captor.capture());

        Mockito.doReturn(outputStream).when(socket).getOutputStream();
    }

    public static void prepareRequestExample() throws JsonProcessingException {
        Map<String, String> body = new HashMap<>();
        body.put("key", "123");
        request = new Request("username", "password", "token", "method", body);
        requestString = convertToJson(request) + "end";
    }

    @BeforeAll
    public static void prepareNetworkService() throws IOException {
        prepareRequestExample();
        prepareSocketMock();

        networkService = new NetworkService(socket);
        networkService.init();
    }

    @Test
    public void receiveTest() throws IOException {
        Request parsedRequest = networkService.receive();
        Assertions.assertEquals(parsedRequest.getUsername(), request.getUsername());
        Assertions.assertEquals(parsedRequest.getBody(), request.getBody());
        Assertions.assertEquals(parsedRequest.getMethodName(), request.getMethodName());
        Assertions.assertEquals(parsedRequest.getPassword(), request.getPassword());
        Assertions.assertEquals(parsedRequest.getToken(), request.getToken());
    }

    private Result prepareResultExample() {
        Map<String, String> body = new HashMap<>();
        body.put("key", "123");
        return new Result(RequestResult.OK, body);
    }

    @Test
    public void sendTest() throws IOException {
        Result result = prepareResultExample();
        networkService.send(result);
        List<Integer> capturedBytesList = captor.getAllValues();
        byte[] capturedBytesArray = convertToByteArray(capturedBytesList);
        String capturedWord = new String(capturedBytesArray);

        Assertions.assertEquals(convertToJson(result) + "end\n", capturedWord);
    }

    public static <T> String convertToJson(T object) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

    private static byte[] convertToByteArray(List<? extends Number> byteList) {
        byte[] byteArray = new byte[byteList.size()];

        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = (byte) (int) byteList.get(i);
        }

        return  byteArray;
    }
}
