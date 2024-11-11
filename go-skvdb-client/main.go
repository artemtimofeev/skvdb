package go_skvdb_client

import (
	"fmt"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/conn"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/storage"
)

func tets2() {
	cf1 := conn.NewConnectionFactory("localhost", 4004, "admin", "password")
	connection1, err := cf1.CreateConnection()
	if err != nil {
		panic(err)
	}
	st := *connection1.GetStorage()
	_, err = st.CreateTable("123")
	if err != nil {
		panic(err)
	}
	userService := *connection1.GetUserService()
	err = userService.CreateUser("123", "test", false)
	if err != nil {
		panic(err)
	}
	err = userService.GrantAuthority("123", "OWNER", "123")
	if err != nil {
		panic(err)
	}
	res, err := userService.HasAuthority("123", "OWNER", "123")
	if err != nil {
		panic(err)
	}
	fmt.Println(res)
}

func test(st *storage.Storage) {
	// STORAGE METHODS
	storage := *st
	_, err := storage.CreateTable("test")
	if err != nil {
		panic(err)
	}

	table, err := storage.FindTableByName("test")
	if err != nil {
		panic(err)
	}

	tables, err := storage.GetAllTables()
	if err != nil {
		panic(err)
	}
	for _, table := range tables {
		fmt.Println(table.GetTableMetaData().Name)
	}

	// TABLE METHODS
	err = table.Set("1", "testgo")
	if err != nil {
		panic(err)
	}
	value, err := table.Get("1")
	if err != nil {
		panic(err)
	}
	fmt.Println(value)

	result, err := table.ContainsKey("1")
	if err != nil {
		panic(err)
	}
	fmt.Println(result)
	result, err = table.ContainsKey("2")
	if err != nil {
		panic(err)
	}
	fmt.Println(result)

	err = table.Delete("1")
	if err != nil {
		panic(err)
	}
	err = table.Delete("2")
	if err != nil {
		panic(err)
	}

	result, err = table.ContainsKey("1")
	if err != nil {
		panic(err)
	}
	fmt.Println(result)
	result, err = table.ContainsKey("2")
	if err != nil {
		panic(err)
	}
	fmt.Println(result)
}
