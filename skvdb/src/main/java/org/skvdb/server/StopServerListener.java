package org.skvdb.server;

import jakarta.annotation.PostConstruct;
import org.skvdb.server.network.ClientSocketService;
import org.skvdb.server.network.SocketAcceptor;
import org.skvdb.service.ServerStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class StopServerListener {
    @Autowired
    private ServerStatusService serverStatusService;

    @Autowired
    private ConnectionPool connectionPool;

    @Autowired
    private SocketAcceptor socketAcceptor;

    @Autowired
    private ClientSocketService clientSocketService;

    //@PostConstruct
    public void init() {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        while (!input.equalsIgnoreCase("stop")) {
            System.out.print("Введите слово (или 'stop' для остановки сервера): ");
            input = scanner.nextLine();
        }

        serverStatusService.shutdown();
        connectionPool.shutdownNow();
        clientSocketService.close();
        socketAcceptor.close();
    }
}
