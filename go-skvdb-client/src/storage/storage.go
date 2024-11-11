package storage

import (
	"errors"
	"fmt"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/dto"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/service/networkService"
)

var (
	ErrTableAlreadyExists = errors.New("table already exists")
	ErrTableNotFound      = errors.New("table not found")
	ErrWrongRequest       = errors.New("wrong request")
)

type Storage interface {
	CreateTable(name string) (Table, error)
	FindTableByName(name string) (Table, error)
	GetAllTables() ([]Table, error)
}

type StorageImpl struct {
	networkService *networkService.NetworkService

	username string
	password string
}

func NewStorage(ntwrkService *networkService.NetworkService, username string, password string) Storage {
	return &StorageImpl{networkService: ntwrkService, username: username, password: password}
}

func (storage *StorageImpl) CreateTable(name string) (Table, error) {
	const op = "storage.createTable"

	body := make(map[string]string)
	body["table"] = name
	response, err := storage.networkService.Send(dto.Request{
		Username:   storage.username,
		Password:   storage.password,
		MethodName: "create_table",
		Body:       body,
	})
	if err != nil {
		return nil, fmt.Errorf("%s: %w", op, err)
	}
	if response.Result == "OK" {
		return newTableImpl(storage.networkService, storage.username, storage.password, name), nil
	}
	return nil, fmt.Errorf("%s", response.Error)
}

func (storage *StorageImpl) FindTableByName(name string) (Table, error) {
	const op = "storage.createTable"

	body := make(map[string]string)
	body["table"] = name
	response, err := storage.networkService.Send(dto.Request{
		Username:   storage.username,
		Password:   storage.password,
		MethodName: "find_table_by_name",
		Body:       body,
	})
	if err != nil {
		return nil, fmt.Errorf("%s: %w", op, err)
	}
	if response.Result == "OK" {
		return newTableImpl(storage.networkService, storage.username, storage.password, name), nil
	}
	return nil, fmt.Errorf("%s", response.Error)
}

func (storage *StorageImpl) GetAllTables() ([]Table, error) {
	const op = "storage.getAllTables"

	response, err := storage.networkService.Send(dto.Request{
		Username:   storage.username,
		Password:   storage.password,
		MethodName: "get_all_tables",
	})
	if err != nil {
		return nil, fmt.Errorf("%s: %w", op, err)
	}
	tables := make([]Table, 0)
	if response.Result == "OK" {
		for table, _ := range response.Body {
			tables = append(tables, newTableImpl(storage.networkService, storage.username, storage.password, table))
		}
		return tables, nil
	}
	return nil, fmt.Errorf("%s", response.Error)
}

func (storage *StorageImpl) DeleteTable(name string) error {
	const op = "storage.deleteTable"

	body := make(map[string]string)
	body["table"] = name
	response, err := storage.networkService.Send(dto.Request{
		Username:   storage.username,
		Password:   storage.password,
		MethodName: "delete_table",
		Body:       body,
	})
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}
	if response.Result == "OK" {
		return nil
	}
	return fmt.Errorf("%s", response.Error)
}
