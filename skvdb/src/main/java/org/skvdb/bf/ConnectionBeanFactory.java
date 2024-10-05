package org.skvdb.bf;

import org.skvdb.server.Client;
import org.skvdb.server.Connection;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnectionBeanFactory {
    @Autowired
    private ObjectFactory<Connection> connectionBeanFactory;

    public Connection getConnection(Client client) {
        Connection connection = connectionBeanFactory.getObject();
        connection.setClient(client);
        return connection;
    }
}
