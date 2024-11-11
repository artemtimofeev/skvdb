package org.skvdb.configuration;

import org.skvdb.configuration.settings.SecuritySettings;
import org.skvdb.configuration.settings.ServerSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {
    @Bean
    public static ServerSettings getServerSettings() {
        return new ServerSettings(4004, 100, 8080, 100);
    }

    @Bean
    public static SecuritySettings getSecuritySettings() {
        return new SecuritySettings("admin", "password");
    }
}
