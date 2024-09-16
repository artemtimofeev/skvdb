package org.skvdb;

import org.skvdb.network.Connection;
import org.skvdb.network.ConnectionFactory;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        ConnectionFactory cf = new ConnectionFactory();

        cf.setHost("localhost");
        cf.setPort(4004);
        cf.setUsername("user");
        cf.setPassword("password");

        Connection conn = cf.createConnection();

        Connection.Executor executor = conn.createExecutor();
        /*executor.set("main", "123", "rrrasnn");
        executor.set("main", "12313", "rrfherr");
        executor.set("main", "123335", "rrrsfs");
        executor.set("main", "12321", "rrrtgs");*/
        System.out.println(executor.get("main", "123"));

        //Thread.sleep(100000);

        conn.close();

    }
}
