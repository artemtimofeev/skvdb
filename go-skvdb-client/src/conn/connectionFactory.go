package conn

import (
	"fmt"
)

type ConnectionFactory struct {
	host     string
	port     int
	username string
	password string
}

func NewConnectionFactory(host string, port int, username string, password string) *ConnectionFactory {
	return &ConnectionFactory{host: host, port: port, username: username, password: password}
}

func (factory *ConnectionFactory) CreateConnection() (*Connection, error) {
	const op = "network.ConnectionFactory.CreateConnection"
	connection, err := newConnection(factory.host, factory.port, factory.username, factory.password)
	if err != nil {
		return nil, fmt.Errorf("%s: %w", op, err)
	}
	return connection, nil
}
