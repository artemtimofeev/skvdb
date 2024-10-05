package org.skvdb.network;

import org.skvdb.dto.Request;
import org.skvdb.dto.Result;
import org.skvdb.exception.NetworkException;

import java.io.*;
import java.net.Socket;

public class NetworkService {
    private Socket clientSocket;

    private BufferedReader reader;

    private BufferedWriter writer;

    NetworkService(String host, int port) {
        try {
            clientSocket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    public Result send(Request request) {
        try {
            writer.write(
                    DtoConverterService.convertToJson(request) + "end"
            );
            writer.write("\n");
            writer.flush();
            return receive();
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    public void close() {
        try {
            clientSocket.close();
            reader.close();
            writer.close();
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    private Result receive() {
        StringBuilder answer = new StringBuilder();
        String line = "";
        do {
            try {
                line = reader.readLine();
                answer.append(line);
            } catch (IOException e) {
                throw new NetworkException(e);
            }
        } while (!line.endsWith("}end"));

        answer.delete(answer.length() - 3, answer.length());

        return DtoConverterService.convertFromJson(answer.toString());
    }
}
