package org.skvdb;

import org.skvdb.server.Connection;
import org.skvdb.server.ConnectionFactory;

import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory(4004);

        Semaphore connectionsLimit = new Semaphore(1);

        while (true) {
            connectionsLimit.acquire();
            Thread thread = new Thread(() -> {
                Connection connection = connectionFactory.createConnection();
                try {
                    connection.listen();
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    connection.close();
                }
                connectionsLimit.release();
            });
            thread.start();
        }
        //connectionFactory.close();
    }
}