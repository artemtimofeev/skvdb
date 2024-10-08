В ветке spring-based находится версия сервера на Spring Framework

`skvdb` означает simple key-value database

`/scvdb` -- сервер базы данных

`/scvdb-client` -- клиент для подключения к БД

1. Клиент-серверная модель с взаимодействием по TCP;
2. Поддержка одновременной работы сервера с большим числом клиентов;
3. Аутентификация по логину и паролю, после первого запроса по токену;
4. Данные хранятся в формате ключ — значение;
5. Доступны операции `get(String key) -> String value` и `set(String key, String value)`;
6. Данные хранятся в оперативной памяти сервера с записью в хронологическом порядке модифицирующих операций в лог. После перезапуска сервера база восстанавливается из лога, затем файл с логом минимизируется (в полученном файле остаются только конечные операции для каждого ключа).

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
1. Функционал создания и управления пользователями (сейчас просто захордкожен один пользователь с фиксированным логином и паролем в `security.AuthenticationService`)
2. Разграничение прав доступа
3. Создание таблиц (на текущий момент передача названия таблицы в функциях `get` и `set` игнорируется)
4. Обработка исключительных ситуаций (корректная обработка на сервере разрыва соединения с клиентом; попытка восстановления соединения в клиентском функционале)
5. Решить проблему с дублированием кода в клиенте и сервере (например `dto`)
6. Рефакторинг протокола (разделить аутентификацию и бизнес-объекты)
7. Исправить потенциальную проблему с потерей данных при аварийном отключении во время инициализации сервера
8. Настройка логирования
9. Реализовать механизм корректного завершения работы сервера при получении соответствующего сигнала
