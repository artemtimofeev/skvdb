`skvdb` означает simple key-value database

`/scvdb` -- сервер базы данных

`/scvdb-client` -- клиент для подключения к БД

1. Клиент-серверная модель с взаимодействием по TCP;
2. Поддержка одновременной работы сервера с большим числом клиентов;
3. Аутентификация по логину и паролю, после первого запроса по токену;
4. Данные хранятся в формате ключ — значение;
5. Доступны операции `get(String key) -> String value` и `set(String key, String value)`;
6. Данные хранятся в оперативной памяти сервера с сбросом модифицирующих операций в лог. После перезапуска сервера база восстанавливается из лога с его сжатием.

### Пример подключения со стороны клиента

```java
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
```

### TODO
1. Пользователь администратор
2. Функционал создания и управления пользователями (сейчас просто захордкожен один пользователь с фиксированным логином и паролем в `security.AuthenticationService`)
3. Разграничение прав доступа
4. Создание таблиц (на текущий момент передача названия таблицы в функциях `get` и `set` игнорируется)
5. Обработка исключительных ситуаций (клиент разорвал соединение с сервером, попытка воостановления соединения)
6. Решить проблему с дублированием кода в клиенте и сервере (например `dto`)
