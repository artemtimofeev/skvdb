package org.skvdb;

import org.skvdb.network.Connection;
import org.skvdb.network.ConnectionFactory;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        ConnectionFactory cf = new ConnectionFactory();

        cf.setHost("localhost");
        cf.setPort(4004);
        cf.setUsername("admin1");
        cf.setPassword("password");

        Connection conn = cf.createConnection();

        Connection.Executor executor = conn.createExecutor();
        System.out.println(executor.get("main", "123"));

        conn.close();
    }
}
