package org.skvdb.configuration;

import org.skvdb.configuration.settings.ServerSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ServerConfig {
    @Bean
    public static ServerSettings getDatabaseConfiguration() {
        return new ServerSettings(4004, 100);
    }

    @Bean
    public static ExecutorService getConnectionListenerExecutor() {
        return Executors.newFixedThreadPool(1);
    }
}
