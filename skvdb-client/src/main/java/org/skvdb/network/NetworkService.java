package org.skvdb.network;

import org.skvdb.dto.AuthenticationDto;
import org.skvdb.dto.Dto;
import org.skvdb.exception.NetworkException;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

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

    public <T extends Dto> Dto send(T dto) {
        try {
            writer.write(
                    DtoConverterService.convertToJson(new NetworkPacket(
                            dto.getClass().getName(),
                            new String(
                                    Base64.getEncoder().encode(DtoConverterService.convertToJson(dto).getBytes())
                                )
                            )
                    )
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

    private Dto receive() {
        StringBuilder answer = new StringBuilder();
        String line = "";
        do {
            try {
                line = reader.readLine();
                answer.append(line);
            } catch (IOException e) {
                throw new NetworkException(e);
            }
        } while (line.charAt(line.length() - 1) != '}');

        return DtoConverterService.convertFromEncodedJson(answer.toString());
    }
}
