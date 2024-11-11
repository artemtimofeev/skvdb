package org.skvdb.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.exception.BadRequestException;
import org.skvdb.controller.Controller;
import org.skvdb.exception.ControllerNotFoundException;
import org.skvdb.server.network.NetworkService;
import org.skvdb.server.network.dto.Request;
import org.skvdb.server.network.dto.Result;
import org.skvdb.service.ControllerMappingService;
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
    private ControllerMappingService controllerMappingService;

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
            logger.debug("Клиенту (id={}) отправлен результат {} {}", client.getId(), result.getBody(), result.getErrorMessage());
        }
    }

    private Result resolveRequest(Request request) throws BadRequestException {
        String methodName = request.getMethodName();
        try {
            return controllerMappingService.getController(methodName).control(request);
        } catch (Exception e) {
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
