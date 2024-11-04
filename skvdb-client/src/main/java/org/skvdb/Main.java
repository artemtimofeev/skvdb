package org.skvdb;

import org.skvdb.common.exception.TableAlreadyExistsException;
import org.skvdb.common.exception.TableNotFoundException;
import org.skvdb.common.storage.Storage;
import org.skvdb.common.storage.Table;
import org.skvdb.network.Connection;
import org.skvdb.network.ConnectionFactory;


public class Main {
    public static void main(String[] args) throws TableAlreadyExistsException, TableNotFoundException {
        ConnectionFactory cf = new ConnectionFactory();

        cf.setHost("158.160.23.111");
        cf.setPort(4004);
        cf.setUsername("admin");
        cf.setPassword("password");

        Connection conn = cf.createConnection();

        Connection.Executor executor = conn.createExecutor();
        //create user
        //executor.createUser("user", "password", false);
        Storage storage = executor.getStorage();
        Table<String> table = storage.createTable("test", String.class);
        //Table<String> table = storage.findTableByName("test", String.class);
        table.set("1", "test");

        System.out.println(table.get("1"));

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
