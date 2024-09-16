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

    public NetworkService(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    public Request receive() {
        StringBuilder answer = new StringBuilder();
        String line = "";
        do {
            try {
                line = reader.readLine();
                answer.append(line);
            } catch (IOException e) {
                throw new NetworkException(e);
            }
        } while (!line.substring(line.length() - 4).equals("}end"));

        answer.delete(answer.length() - 3, answer.length());

        return DtoConverterService.convertFromJson(answer.toString());
    }

    public void send(Result result) {
        try {
            writer.write(DtoConverterService.convertToJson(result) + "end");
            writer.write("\n");
            writer.flush();
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
}
