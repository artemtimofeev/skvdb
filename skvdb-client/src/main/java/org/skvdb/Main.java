package org.skvdb;

import org.skvdb.network.Connection;
import org.skvdb.network.ConnectionFactory;
import org.skvdb.storage.api.Storage;
import org.skvdb.storage.api.Table;
import org.skvdb.storage.api.TableAlreadyExistsException;
import org.skvdb.storage.api.TableNotFoundException;


public class Main {
    public static void main(String[] args) throws TableAlreadyExistsException, TableNotFoundException {
        ConnectionFactory cf = new ConnectionFactory();

        cf.setHost("localhost");
        cf.setPort(4004);
        cf.setUsername("user");
        cf.setPassword("password");

        Connection conn = cf.createConnection();

        Connection.Executor executor = conn.createExecutor();
        //create user
        //executor.createUser("user", "password", false);
        Storage storage = executor.getStorage();
        Table<String> table = storage.findTableByName("test", String.class);
        table.set("123", "test");

        System.out.println(table.get("123").value());

        conn.close();

        /*Table table = (Table) Proxy.newProxyInstance(Table.class.getClassLoader(), new Class<?>[]{Table.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Parameter[] parameters = method.getParameters();

                for (Parameter parameter : parameters) {
                    System.out.println("Имя параметра: " + parameter.getName());
                }
                return null;
            }
        });

        table.get("123");*/
    }
}
