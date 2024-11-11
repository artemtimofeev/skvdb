package main

import (
	"fmt"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/conn"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/storage"
)

func main() {
	cf1 := conn.NewConnectionFactory("158.160.7.136", 4004, "admin", "password")
	connection1, err := cf1.CreateConnection()
	if err != nil {
		panic(err)
	}

	str := *connection1.GetStorage()
	str.CreateTable("test")
	str.CreateTable("test2")
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
