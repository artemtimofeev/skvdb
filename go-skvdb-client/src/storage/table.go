package storage

import (
	"fmt"
	"go-skvdb-client/src/dto"
	"go-skvdb-client/src/service/networkService"
	"strconv"
)

type Table interface {
	Set(k string, v string) error
	Get(k string) (string, error)
	Delete(k string) error
	ContainsKey(k string) (bool, error)
	GetTableMetaData() TableMetaData
}

type TableImpl struct {
	username string
	password string

	networkService *networkService.NetworkService
	TableMetaData  TableMetaData
}

func newTableImpl(networkService *networkService.NetworkService, username string, password string, tableName string) *TableImpl {
	return &TableImpl{networkService: networkService, username: username, password: password, TableMetaData: TableMetaData{Name: tableName}}
}

func (t *TableImpl) Set(k string, v string) error {
	const op = "storage.table.Set"

	body := make(map[string]string)
	body["key"] = k
	body["value"] = v
	body["table"] = t.TableMetaData.Name
	response, err := t.networkService.Send(dto.Request{
		Username:   t.username,
		Password:   t.password,
		MethodName: "set",
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

func (t *TableImpl) Get(k string) (string, error) {
	const op = "storage.table.Get"

	body := make(map[string]string)
	body["key"] = k
	body["table"] = t.TableMetaData.Name
	response, err := t.networkService.Send(dto.Request{
		Username:   t.username,
		Password:   t.password,
		MethodName: "get",
		Body:       body,
	})
	if err != nil {
		return "", fmt.Errorf("%s: %w", op, err)
	}
	if response.Result == "OK" {
		return response.Body["value"], nil
	}
	return "", fmt.Errorf("%s", response.Error)
}

func (t *TableImpl) Delete(k string) error {
	const op = "storage.table.Delete"

	body := make(map[string]string)
	body["key"] = k
	body["table"] = t.TableMetaData.Name
	response, err := t.networkService.Send(dto.Request{
		Username:   t.username,
		Password:   t.password,
		MethodName: "delete",
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

func (t *TableImpl) ContainsKey(k string) (bool, error) {
	const op = "storage.table.ContainsKey"

	body := make(map[string]string)
	body["key"] = k
	body["table"] = t.TableMetaData.Name
	response, err := t.networkService.Send(dto.Request{
		Username:   t.username,
		Password:   t.password,
		MethodName: "contains_key",
		Body:       body,
	})
	if err != nil {
		return false, fmt.Errorf("%s: %w", op, err)
	}
	if response.Result == "OK" {
		return strconv.ParseBool(response.Body["result"])
	}
	return false, fmt.Errorf("%s", response.Error)
}

func (t *TableImpl) GetTableMetaData() TableMetaData {
	return t.TableMetaData
}
