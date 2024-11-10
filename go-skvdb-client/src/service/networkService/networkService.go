package networkService

import (
	"bufio"
	"encoding/json"
	"fmt"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/dto"
	"net"
	"strings"
)

type NetworkService struct {
	host string
	port int

	conn   *net.Conn
	reader *bufio.Reader
	writer *bufio.Writer
}

func NewNetworkService(host string, port int) (*NetworkService, error) {
	const op = "network.NewNetworkService"
	conn, err := net.Dial("tcp", fmt.Sprintf("%s:%d", host, port))
	if err != nil {
		return nil, fmt.Errorf("%s: %w", op, err)
	}
	reader := bufio.NewReader(conn)
	writer := bufio.NewWriter(conn)
	return &NetworkService{host: host, port: port, conn: &conn, reader: reader, writer: writer}, nil
}

func (N *NetworkService) Close() error {
	const op = "network.NetworkService.Close"
	conn := *N.conn
	err := conn.Close()
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}
	return nil
}

func (N *NetworkService) Send(request dto.Request) (dto.Response, error) {
	const op = "network.NetworkService.Send"

	json, err := json.Marshal(request)
	if err != nil {
		return dto.Response{}, fmt.Errorf("%s: %w", op, err)
	}
	_, err = N.writer.WriteString(string(json) + "end\n")
	if err != nil {
		return dto.Response{}, fmt.Errorf("%s: %w", op, err)
	}
	err = N.writer.Flush()
	if err != nil {
		return dto.Response{}, fmt.Errorf("%s: %w", op, err)
	}

	return N.Receive()
}

func (N *NetworkService) Receive() (dto.Response, error) {
	const op = "network.NetworkService.Receive"

	body := ""

	for {
		input, err := N.reader.ReadString('\n')
		if err != nil {
			return dto.Response{}, fmt.Errorf("%s: %w", op, err)
		}

		input = strings.TrimSuffix(input, "\n")

		body += input

		if strings.HasSuffix(input, "}end") {
			break
		}
	}
	body = body[:len(body)-3]

	var response dto.Response
	err := json.Unmarshal([]byte(body), &response)
	if err != nil {
		return dto.Response{}, fmt.Errorf("%s: %w", op, err)
	}

	return response, nil
}
