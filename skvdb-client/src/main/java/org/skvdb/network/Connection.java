package org.skvdb.network;

import org.skvdb.common.service.UserService;
import org.skvdb.common.storage.Storage;
import org.skvdb.service.UserServiceImpl;
import org.skvdb.storage.StorageImpl;

public class Connection {
    private String username;
    private String password;
    private NetworkService networkService;

    private Storage storage;
    private UserService userService;

    Connection(String host, int port, String username, String password) {
        networkService = new NetworkService(host, port);
        this.username = username;
        this.password = password;

        this.storage = new StorageImpl(username, password, networkService);
        this.userService = new UserServiceImpl(username, password, networkService);
    }

    public Executor createExecutor() {
        return new Executor();
    }

    public void close() {
        networkService.close();
    }

    public class Executor {
        public Storage getStorage() {
            return storage;
        }

        public UserService getUserService() {
            return userService;
        }
    }
}
