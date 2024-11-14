### Краткая инструкция по настройке клиента и сервера
#### 1. Получение сервера
После регистрации (http://skvdb.tech/signup) открывается возможность создания экземпляра хранилища skvdb (далее Хранилище) (http://skvdb.tech/instances).
На этой странице необходимо нажать кнопку "Create new instance" и через приблизительно 5 минут будет создан новый экзампляр Хранилища.
#### 2. Получение пользователя Хранилища
Когда экземпляр перейдет в статус "RUNNING", станет доступен переход в интерфейс управления экземпляром Хранилища.

После перехода в интерфейс управления, во вкладке "Users" будет доступна кнопка "Create new user", которая позволит создать нового пользователя. Необходимо запомнить password пользователя, он будет нужен при подключении из клиента.
#### 3. Подключение из приложения на Go
##### 3.1 Установка клиентской библиотеки
`go get github.com/artemtimofeev/skvdb/go-skvdb-client`

##### 3.2 helloworld.go
Вместо "localhost" и 4004 необходимо подставить ip и port из таблицы по ссылке http://skvdb.tech/instances, а вместо "admin" и "password" username и password созданного пользователя Хранилища (см. пункт 2).

*Все возможности описаны в документации http://skvdb.tech/documentation.*

```
    package main
    
    import (
    "fmt"
    "github.com/artemtimofeev/skvdb/go-skvdb-client/src/conn"
    )
    
    func main() {
    cf := conn.NewConnectionFactory("localhost", 4004, "admin", "password")

	connection, err := cf.CreateConnection()
	if err != nil {
		panic(err)
	}
	defer connection.Close()

	storage := *connection.GetStorage()
	table, err := storage.CreateTable("testTable")
	if err != nil {
		panic(err)
	}

	table.Set("1", "2")
	fmt.Println(table.Get("1"))
	fmt.Println(table.Get("anotherKey"))
}
```