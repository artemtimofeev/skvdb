/scvdb -- сервер базы данных

/scvdb-client -- клиент для подключения к БД

1. Клиент-серверная модель с взаимодействием по TCP;
2. Поддержка одновременной работы сервера с большим числом клиентов;
3. Аутентификация по логину и паролю, после первого запроса по токену;
4. Данные хранятся в формате ключ — значение;
5. Доступны операции get и set;
6. Данные хранятся в оперативной памяти сервера с сбросом модифицирующих операций в лог. После перезапуска сервера база восстанавливается из лога с его сжатием.

### Пример подключения со стороны клиента

'''
ConnectionFactory cf = new ConnectionFactory();

cf.setHost("localhost");
cf.setPort(4004);
cf.setUsername("user");
cf.setPassword("password12");

Connection conn = cf.createConnection();

Connection.Executor executor = conn.createExecutor();
executor.set("main", "123", "rrrasnn");
executor.set("main", "12313", "rrfherr");
executor.set("main", "123335", "rrrsfs");
executor.set("main", "12321", "rrrtgs");
System.out.println(executor.get("main", "123"));

conn.close();
'''

### TODO
