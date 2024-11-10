package org.skvdb;

import org.skvdb.common.exception.TableAlreadyExistsException;
import org.skvdb.common.exception.TableNotFoundException;
import org.skvdb.common.storage.TableMetaData;
import org.skvdb.network.Connection;
import org.skvdb.network.ConnectionFactory;
import org.skvdb.storage.Storage;
import org.skvdb.storage.Table;


public class Main {
    public static void main(String[] args) throws TableAlreadyExistsException, TableNotFoundException {
        ConnectionFactory cf = new ConnectionFactory();

        cf.setHost("localhost");
        cf.setPort(4004);
        cf.setUsername("admin");
        cf.setPassword("password");

        Connection conn = cf.createConnection();

        Connection.Executor executor = conn.createExecutor();
        //create user
        //executor.createUser("user", "password", false);
        Storage storage = executor.getStorage();
        //Table<String> table = storage.createTable("test", String.class);
        Table table = storage.findTableByName("test22");
        //table.set("1", "test");

        System.out.println(table.get("100"));

        conn.close();
    }
}
