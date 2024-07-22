package org.skvdb.server;

import org.skvdb.dto.*;
import org.skvdb.network.NetworkService;
import org.skvdb.security.AuthenticationService;

import java.net.Socket;

public class Connection {
    private NetworkService networkService;
    private AuthenticationService authenticationService;
    private QueryHandler queryHandler;

    Connection(AuthenticationService authenticationService, Socket clientSocket, QueryHandler queryHandler) {
        this.authenticationService = authenticationService;
        this.queryHandler = queryHandler;
        this.networkService = new NetworkService(clientSocket);
    }

    public void listen() {
        while (true) {
            Dto dto = networkService.receive();

            if (dto instanceof AuthenticationDto) {
                AuthenticationResultDto authenticationResultDto = authenticationService.authenticate((AuthenticationDto) dto);
                networkService.send(authenticationResultDto);
            } else if (!authenticationService.isValidToken(dto)){
                break;
            }

            if (dto instanceof CloseConnectionDto) {
                networkService.send(dto);
                break;
            }

            if (dto instanceof QueryDto) {
                networkService.send(queryHandler.handleQuery((QueryDto) dto));
            }
        }
    }

    public void close() {
        try{
            networkService.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
