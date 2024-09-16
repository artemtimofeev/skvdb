package org.skvdb;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class StopServerListener {
    @Autowired
    private ServerStatusService serverStatusService;

    @Autowired
    private ConnectionPoolService connectionPoolService;

    @Autowired
    private ExecutorService connectionListenerExecutor;

    @Autowired
    private SocketAcceptor socketAcceptor;

    @PostConstruct
    public void init() {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        while (!input.equalsIgnoreCase("stop")) {
            System.out.print("Введите слово (или 'stop' для остановки сервера): ");
            input = scanner.nextLine();
        }

        serverStatusService.shutdown();
        connectionPoolService.shutdownNow();
        connectionListenerExecutor.shutdownNow();
        try {
            socketAcceptor.close();
        } catch (IOException e) {
        }
    }
}
