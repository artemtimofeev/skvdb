package org.skvdb.configuration.settings;

public record ServerSettings(int port, int connectionsPoolSize, int httpPort, int httpBacklog) {
}
