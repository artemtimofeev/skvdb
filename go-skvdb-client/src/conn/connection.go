package conn

import (
	"fmt"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/service/networkService"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/service/userService"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/storage"
)

type Connection struct {
	username       string
	password       string
	networkService *networkService.NetworkService

	storage     storage.Storage
	userService userService.UserService
}

func (conn *Connection) GetStorage() *storage.Storage {
	return &conn.storage
}

func (conn *Connection) GetUserService() *userService.UserService {
	return &conn.userService
}

func newConnection(host string, port int, username string, password string) (*Connection, error) {
	const op = "network.newConnection"
	ntwrkService, err := networkService.NewNetworkService(host, port)
	if err != nil {
		return nil, fmt.Errorf("%s : %w", op, err)
	}
	return &Connection{username: username, password: password, networkService: ntwrkService, storage: storage.NewStorage(ntwrkService, username, password), userService: userService.NewUserService(ntwrkService, username, password)}, nil
}
