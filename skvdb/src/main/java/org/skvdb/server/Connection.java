package org.skvdb.server;

import org.skvdb.ServerStatusService;
import org.skvdb.controller.Controller;
import org.skvdb.dto.*;
import org.skvdb.network.NetworkService;
import org.skvdb.security.AuthenticationService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class Connection implements Runnable{
    private NetworkService networkService;
    private AuthenticationService authenticationService;
    private QueryHandler queryHandler;
    private ServerStatusService serverStatusService;
    private Controller controller;

    public Connection(AuthenticationService authenticationService, Socket clientSocket, QueryHandler queryHandler, ServerStatusService serverStatusService, Controller controller) {
        this.authenticationService = authenticationService;
        this.queryHandler = queryHandler;
        this.networkService = new NetworkService(clientSocket);
        this.serverStatusService = serverStatusService;
        this.controller = controller;
    }

    @Override
    public void run() {
        while (serverStatusService.isRunning()) {
            Request request = networkService.receive();
            String methodName = request.getMethodName();
            try {
                Method method = controller.getClass().getMethod(methodName);
                networkService.send((Result) method.invoke(controller, request));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        close();
    }

    public void close() {
        try{
            networkService.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
