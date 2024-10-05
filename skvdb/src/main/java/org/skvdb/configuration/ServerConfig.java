package org.skvdb.configuration;

import org.skvdb.configuration.settings.ServerSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {
    @Bean
    public static ServerSettings getDatabaseConfiguration() {
        return new ServerSettings(4004, 1);
    }
}
