package org.skvdb.rest;

import com.sun.net.httpserver.HttpServer;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class RestApiServer {
    @PostConstruct
    public void start() throws IOException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 100);
        httpServer.setExecutor(executorService);
        httpServer.createContext("/", new BaseHandler());
        httpServer.start();
    }
}
