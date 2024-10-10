package org.skvdb.server.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.Result;
import org.skvdb.exception.ClosedConnectionException;
import org.skvdb.util.RequestParser;
import org.skvdb.util.ResultConverter;

import java.io.*;
import java.net.Socket;

public class NetworkService {
    private Socket clientSocket;

    private BufferedReader reader;

    private BufferedWriter writer;

    private static final Logger logger = LogManager.getLogger();

    public NetworkService(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void init() throws IOException {
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    public Request receive() throws IOException {
        StringBuilder answer = new StringBuilder();
        String line = "";
        do {
            line = reader.readLine();
            if (line == null) {
                throw new ClosedConnectionException("Reached end of the input stream");
            }
            answer.append(line);
        } while (!line.endsWith("}end"));

        answer.delete(answer.length() - 3, answer.length());

        return RequestParser.parseFromJson(answer.toString());
    }

    public void send(Result result) throws IOException {
        writer.write(ResultConverter.convertToJson(result) + "end");
        writer.write("\n");
        writer.flush();
    }

    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            logger.error(e);
        }
        try {
            reader.close();
        } catch (IOException e) {
            logger.error(e);
        }
        try {
            writer.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
