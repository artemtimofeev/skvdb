import Header from "../components/Header";
import remarkGfm from "remark-gfm";
import ReactMarkdown from "react-markdown";
import {Container} from "react-bootstrap";
import MyCoolCodeBlock from "../components/CodeBlock";

function Documentation() {
    const markdown1 = `
# Описание системы
skvdb — хранилище пар ключ-значение (далее просто Хранилище). Основные сущности, взаимодействие с которомы осуществляется на стороне клиента:
* **Storage** — сущность, предоставляющая доступ к методам поиска Таблицы, создания новой Таблицы, удаления Таблицы, получения списка всех Таблиц. Сущность имплементирована типом Storage (см. пункт 5);
* **Таблица** — сущность, предоставляющая доступ к методам операций над парами ключ и значение. Сущность имплементирована типом Table (см. пункт 6);
* **Пользователь** — пользователь Хранилища, который имеет набор Привилегий;
* **Суперпользователь** — пользователь, обладающий повышенными привилегиями;
* **Привилегия** — право доступа определенного типа к определенной Таблице (см. пункт 9);
* **UserService** — сущность, предоставляющая доступ к управлению пользовательскими привилегиями, а также к управлению пользователями. Сущность имплементирована типом Table (см. пункт 4).

Настройки Хранилища:
* **Максимальное число подключений**. По умолчанию этот параметр равняется 100. *На текущий момент нет возможности изменить его при создании Хранилища через менеджер.*
# Go клиент
#### 1. Установка клиента
`
    const code1 = "go get github.com/artemtimofeev/skvdb/go-skvdb-client"

    const markdown2 = `
#### 2. Тип ConnectionFactory
ConnectionFactory предоставляет собой фабрику, создающую новые подключения (тип Connection).
##### 2.1 Получение ConnectionFactory
Первым параметром передается хост хранилища, вторым порт, третьим и четвертым — имя пользователя и пароль (см. пункт 2.2).
`
    const code2 = `package main

import (
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/conn"
)

func main() {
	cf := conn.NewConnectionFactory("localhost", 4004, "admin", "password")
}`
    const markdown3 = `
##### 2.2 Методы ConnectionFactory
* **CreateConnection**() (*Connection, error) — Создает новое подключение к Хранилищу и возвращает объект (см. пункт 3) для взаимодействия с ним;
##### 2.3 Управление пользователями хранилища
По 
#### 3. Тип Connection
Connection инкапсулирует TCP соединение с Хранилищем и предоставляет методы для получения Storage и UserService.
##### 3.1 Получение Connection
См. пункт 2.2.
##### 3.2 Методы Connection
* **GetStorage**() *storage.Storage — Возвращает структуру Storage (см. пункт 5);
* **GetUserService**() *userService.UserService — Возвращает структуру UserService (см. пункт 4);
* **Close**() error  — Закрывает соединение.
#### 4. Тип UserService
##### 4.1 Получение UserService
См. пункт 3.2.
##### 4.2 Методы UserService
* **CreateUser**(username string, password string, isSuperuser bool) error — Создает нового пользователя Хранилища. Вызвать метод может только Суперпользователь;
* **DeleteUser**(username string) error — Удаляет пользователя Хранилища. Вызвать метод может только Суперпользователь;
* **GetAllUsers**() ([]entity.User, error) — Возвращает список всех entity.User (пользователей) (см. пункт 8). Вызвать метод может любой пользователь Хранилища;
* **GetUser**(username string) (entity.User, error) — Возвращает пользователя. Вызвать метод может любой пользователь Хранилища;
* **GrantAuthority**(username string, authority string, table string) error — Предоставляет привилегию доступа к таблице *table* пользователю *username*. 
Название привилегии *authority* может иметь одно из следующих значений: *"OWNER"*, *"READ"*, *"WRITE"*. 
Вызвать метод могут Суперпользователь и пользователь, обладающий привилегией *"OWNER"* для таблицы *table*;
* **GrantSuperuserAuthority**(username string) error — Делает пользователя *username* Суперпользователем. Вызвать метод может только Суперпользователь;
* **HasAnyAuthority**(username string, table string) (bool, error) — Проверяет наличие одной из привилегий у пользователя *username* в таблице *table*. Вызвать метод может любой пользователь Хранилища;
* **HasAuthority**(username string, authority string, table string) (bool, error) — Проверяет привилегии *authority* у пользователя *username* в таблице *table*. Название привилегии *authority* может иметь одно из следующих значений: *"OWNER"*, *"READ"*, *"WRITE"*. Вызвать метод может любой пользователь Хранилища;
* **IsSuperuser**(username string) (bool, error) — Проверяет, является ли пользователь *username* Суперпользователем. Вызвать метод может любой пользователь Хранилища;
* **RevokeAuthority**(username string, authority string, table string) error — Отзывает привилегию *authority* в таблице *table* у пользователя *username*. Вызвать метод могут Суперпользователь и пользователь, обладающий привилегией *"OWNER"* для таблицы *table*;
* **RevokeSuperuserAuthority**(username string) error — Отзывает статус Суперпользователя у пользователя *username*. Вызвать метод может только Суперпользователь.
#### 5. Интерфейс Storage
Предоставляет доступ к методам управления таблицами Table.
##### 5.1 Получение Storage
См. пункт 3.2.
##### 5.2 Методы Storage
* **CreateTable**(name string) (Table, error) — Создает новую таблицу и возвращает структуру Table (см. пункт 6). Вызвать метод может любой пользователь Хранилища;
* **FindTableByName**(name string) (Table, error) — Ищет таблицу в Хранилище и возвращает структуру Table, если таблица найдена. Вызвать метод может любой пользователь Хранилища;
* **GetAllTables**() ([]Table, error) — Возвращает список всех таблиц. Вызвать метод может любой пользователь Хранилища;
* **DeleteTable**(name string) error — Удаляет таблицу *name*. Вызвать метод могут Суперпользователь и пользователь, обладающий привилегией *"OWNER"* для таблицы *name*.
#### 6. Интерфейс Table
##### 6.1 Получение Table
См. пункт 5.2.
##### 6.2 Методы Table
* **Set**(k string, v string) error — Сохраняет значение *v* по ключу *k*. Вызвать метод могут Суперпользователь и пользователь, обладающий привилегией *"WRITE"* для таблицы, на которой вызывается метод;
* **Get**(k string) (string, error) — Возвращает значение по ключу *k*, если ключ существует. Вызвать метод могут Суперпользователь и пользователь, обладающий привилегией *"READ"* для таблицы, на которой вызывается метод;
* **Delete**(k string) error — Удаляет пару ключ-значение по ключу *k*. Вызвать метод могут Суперпользователь и пользователь, обладающий привилегией *"WRITE"* для таблицы, на которой вызывается метод;
* **ContainsKey**(k string) (bool, error) — Проверяет, существует ли пара ключ-значение по ключу *k*. Вызвать метод могут Суперпользователь и пользователь, обладающий любой привилегией для таблицы, на которой вызывается метод;
* **GetTableMetaData**() TableMetaData — Возвращает структуру TableMetaData;
#### 7. Тип TableMetaData
##### 7.1 Поля структуры
* **Name** string — имя таблицы
#### 8. Тип User
##### 8.1 Поля структуры
* **Username** string — Имя пользователя;
* **IsSuperuser** bool — Признак, является ли пользователь Суперпользователем;
* **Authorities** []Authority — Список привилегий (см. пункт 9).
#### 9. Тип Authority
##### 9.1 Поля структуры
* **TableName** string — Название таблицы;
* **AuthorityType** string — Тип привилегии. Может иметь одно из следующих значений: *"OWNER"*, *"READ"*, *"WRITE"*.
`

    return <>
        <Header/>
        <Container className="mt-4">
            <ReactMarkdown children={markdown1} remarkPlugins={[remarkGfm]}></ReactMarkdown>
            <MyCoolCodeBlock code={code1} language={"go"} showLineNumbers={1}></MyCoolCodeBlock>
            <ReactMarkdown children={markdown2} remarkPlugins={[remarkGfm]}></ReactMarkdown>
            <MyCoolCodeBlock code={code2} language={"go"} showLineNumbers={1}></MyCoolCodeBlock>
            <ReactMarkdown children={markdown3} remarkPlugins={[remarkGfm]}></ReactMarkdown>
        </Container>
    </>
}

export { Documentation };