package org.skvdb.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.exception.BadRequestException;
import org.skvdb.controller.Controller;
import org.skvdb.dto.*;
import org.skvdb.server.network.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
@Scope("prototype")
public class Connection {
    @Autowired
    private Controller controller;

    private Client client;

    private NetworkService networkService;

    private static final Logger logger = LogManager.getLogger();

    public void run() throws IOException, BadRequestException {
        networkService = new NetworkService(client.getSocket());
        networkService.init();
        while (true) {
            Request request = networkService.receive();
            logger.debug("Клиент (id={}) отправил {} запрос с телом: {} [u={}, p={}]", client.getId(), request.getMethodName(), request.getBody(), request.getUsername(), request.getPassword());
            Result result = resolveRequest(request);
            networkService.send(result);
            logger.debug("Клиенту (id={}) отправлен результат {}", client.getId(), result.getBody());
        }
    }

    private Result resolveRequest(Request request) throws BadRequestException {
        String methodName = request.getMethodName();
        try {
            Method method = controller.getClass().getMethod(methodName, Request.class);
            return (Result) method.invoke(controller, request);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new BadRequestException(e);
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void close() {
        logger.info("Закрыто соединение с клиентом id={}", client.getId());
        networkService.close();
    }
}
