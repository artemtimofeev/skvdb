package userService

import (
	"fmt"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/dto"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/entity"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/service/networkService"
	"strconv"
)

type UserService interface {
	CreateUser(username string, password string, isSuperuser bool) error
	DeleteUser(username string) error
	GetAllUsers() ([]entity.User, error)
	GetUser(username string) (entity.User, error)
	GrantAuthority(username string, authority string, table string) error
	GrantSuperuserAuthority(username string) error
	HasAnyAuthority(username string) (bool, error)
	HasAuthority(username string) (bool, error)
	IsSuperuser(username string) (bool, error)
	RevokeAuthority(username string, authority string, table string) error
	RevokeSuperuserAuthority(username string) error
}

type UserServiceImpl struct {
	networkService *networkService.NetworkService

	username string
	password string
}

func NewUserService(networkService *networkService.NetworkService, username string, password string) UserService {
	return &UserServiceImpl{networkService: networkService, username: username, password: password}
}

func (userService *UserServiceImpl) CreateUser(username string, password string, isSuperuser bool) error {
	const op = "service.userService.CreateUser"

	body := make(map[string]string)
	body["username"] = username
	body["password"] = password
	body["isSuperuser"] = strconv.FormatBool(isSuperuser)
	response, err := userService.networkService.Send(dto.Request{
		Username:   userService.username,
		Password:   userService.password,
		MethodName: "create_user",
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

func (userService *UserServiceImpl) DeleteUser(username string) error {
	const op = "service.userService.DeleteUser"

	body := make(map[string]string)
	body["username"] = username
	response, err := userService.networkService.Send(dto.Request{
		Username:   userService.username,
		Password:   userService.password,
		MethodName: "create_user",
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

func (userService *UserServiceImpl) GetAllUsers() ([]entity.User, error) {
	const op = "service.userService.GetAllUsers"

	response, err := userService.networkService.Send(dto.Request{
		Username:   userService.username,
		Password:   userService.password,
		MethodName: "get_all_users",
	})
	if err != nil {
		return nil, fmt.Errorf("%s: %w", op, err)
	}
	users := make([]entity.User, 0)
	if response.Result == "OK" {
		for username, _ := range response.Body {
			user, err := userService.GetUser(username)
			if err != nil {
				return nil, fmt.Errorf("%s: %w", op, err)
			}
			users = append(users, user)
		}
		return users, nil
	}
	return nil, fmt.Errorf("%s", response.Error)
}

func (userService *UserServiceImpl) GetUser(username string) (entity.User, error) {
	const op = "service.userService.GetUser"

	isSuperuser, err := userService.IsSuperuser(username)
	if err != nil {
		return entity.User{}, fmt.Errorf("%s: %w", op, err)
	}

	body := make(map[string]string)
	body["username"] = username
	response, err := userService.networkService.Send(dto.Request{
		Username:   userService.username,
		Password:   userService.password,
		MethodName: "get_user_tables",
		Body:       body,
	})
	if err != nil {
		return entity.User{}, fmt.Errorf("%s: %w", op, err)
	}
	if response.Result == "OK" {
		authorities := make([]entity.Authority, 0)
		for table, authorityType := range response.Body {
			authority := entity.Authority{AuthorityType: authorityType, TableName: table}
			authorities = append(authorities, authority)
		}
		return entity.User{Authorities: authorities, Username: username, IsSuperuser: isSuperuser}, nil
	}
	return entity.User{}, fmt.Errorf("%s", response.Error)
}

func (userService *UserServiceImpl) GrantAuthority(username string, authority string, table string) error {
	const op = "service.userService.GrantAuthority"

	body := make(map[string]string)
	body["username"] = username
	body["authority"] = authority
	body["table"] = table
	response, err := userService.networkService.Send(dto.Request{
		Username:   userService.username,
		Password:   userService.password,
		MethodName: "grant_authority",
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

func (userService *UserServiceImpl) GrantSuperuserAuthority(username string) error {
	const op = "service.userService.GrantSuperuserAuthority"

	body := make(map[string]string)
	body["username"] = username
	response, err := userService.networkService.Send(dto.Request{
		Username:   userService.username,
		Password:   userService.password,
		MethodName: "grant_superuser_authority",
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

func (userService *UserServiceImpl) HasAnyAuthority(username string) (bool, error) {
	const op = "service.userService.HasAnyAuthority"

	body := make(map[string]string)
	body["username"] = username
	response, err := userService.networkService.Send(dto.Request{
		Username:   userService.username,
		Password:   userService.password,
		MethodName: "has_any_authority",
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

func (userService *UserServiceImpl) HasAuthority(username string) (bool, error) {
	const op = "service.userService.HasAuthority"

	body := make(map[string]string)
	body["username"] = username
	response, err := userService.networkService.Send(dto.Request{
		Username:   userService.username,
		Password:   userService.password,
		MethodName: "has_authority",
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

func (userService *UserServiceImpl) IsSuperuser(username string) (bool, error) {
	const op = "service.userService.IsSuperuser"

	body := make(map[string]string)
	body["username"] = username
	response, err := userService.networkService.Send(dto.Request{
		Username:   userService.username,
		Password:   userService.password,
		MethodName: "is_superuser",
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

func (userService *UserServiceImpl) RevokeAuthority(username string, authority string, table string) error {
	const op = "service.userService.RevokeAuthority"

	body := make(map[string]string)
	body["username"] = username
	body["authority"] = authority
	body["table"] = table
	response, err := userService.networkService.Send(dto.Request{
		Username:   userService.username,
		Password:   userService.password,
		MethodName: "revoke_authority",
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

func (userService *UserServiceImpl) RevokeSuperuserAuthority(username string) error {
	const op = "service.userService.RevokeSuperuserAuthority"

	body := make(map[string]string)
	body["username"] = username
	response, err := userService.networkService.Send(dto.Request{
		Username:   userService.username,
		Password:   userService.password,
		MethodName: "revoke_superuser_authority",
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
