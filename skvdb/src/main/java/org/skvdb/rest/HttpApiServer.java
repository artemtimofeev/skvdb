package org.skvdb.rest;

import com.sun.net.httpserver.HttpServer;
import jakarta.annotation.PostConstruct;
import org.skvdb.configuration.settings.ServerSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class HttpApiServer {
    @Autowired
    private HttpBridgeHandler httpBridgeHandler;

    @Autowired
    private ServerSettings serverSettings;

    @PostConstruct
    public void start() throws IOException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(serverSettings.httpPort()), serverSettings.httpBacklog());
        httpServer.setExecutor(executorService);
        httpServer.createContext("/", httpBridgeHandler);
        httpServer.start();
    }
}
