package user

import (
	"errors"
	"fmt"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/conn"
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/render"
	"github.com/go-playground/validator/v10"
	"io"
	"log/slog"
	"manager-backend/internal/lib/logger"
	"manager-backend/internal/lib/response"
	"manager-backend/internal/storage/postgres"
	"net/http"
	"strconv"
)

type User struct {
	Username    string `json:"name"`
	IsSuperuser bool   `json:"isSuperuser"`
	Permissions string `json:"permissions"`
}

type GetResponse struct {
	response.Response
	Users []User `json:"users"`
}

func NewGet(log *slog.Logger, st *postgres.Storage) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		instanceIdStr := chi.URLParam(r, "instanceId")
		strg := *st
		instanceId, err := strconv.Atoi(instanceIdStr)
		if err != nil {
			log.Error("failed to get instance", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get instance"))

			return
		}
		instance, err := strg.GetInstance(instanceId)
		if err != nil {
			log.Error("failed to get instance", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get instance"))

			return
		}
		if instance.Status != "RUNNING" {
			log.Info(fmt.Sprintf("instance %d is not running", instanceId))

			render.JSON(w, r, response.Error(fmt.Sprintf("instance %d is not running", instanceId)))

			return
		}

		cf := conn.NewConnectionFactory(instance.Ip, instance.Port, "admin", "password")
		connection, err := cf.CreateConnection()
		defer connection.Close()
		if err != nil {
			log.Error("failed to get connection", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get connection"))

			return
		}
		userService := *connection.GetUserService()
		users, err := userService.GetAllUsers()
		if err != nil {
			log.Error("failed to get all users", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get all users"))

			return
		}
		usersResult := make([]User, 0)
		for _, user := range users {
			authorities := user.Authorities
			permissionsMap := make(map[string]string)
			for _, authority := range authorities {
				val, ok := permissionsMap[authority.TableName]
				if ok {
					permissionsMap[authority.TableName] = val + "," + authority.AuthorityType
				} else {
					permissionsMap[authority.TableName] = authority.AuthorityType
				}
			}
			permissions := ""
			for tableName, permission := range permissionsMap {
				permissions += fmt.Sprintf("Table %s: %s\n", tableName, permission)
			}
			usersResult = append(usersResult, User{Username: user.Username, IsSuperuser: user.IsSuperuser, Permissions: permissions})
		}

		render.JSON(w, r, GetResponse{
			Response: response.OK(),
			Users:    usersResult,
		})
	}
}

func NewDelete(log *slog.Logger, st *postgres.Storage) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		instanceIdStr := chi.URLParam(r, "instanceId")
		username := chi.URLParam(r, "username")

		strg := *st
		instanceId, err := strconv.Atoi(instanceIdStr)
		if err != nil {
			log.Error("failed to get instance", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get instance"))

			return
		}
		instance, err := strg.GetInstance(instanceId)
		if err != nil {
			log.Error("failed to get instance", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get instance"))

			return
		}
		if instance.Status != "RUNNING" {
			log.Info(fmt.Sprintf("instance %d is not running", instanceId))

			render.JSON(w, r, response.Error(fmt.Sprintf("instance %d is not running", instanceId)))

			return
		}
		cf := conn.NewConnectionFactory(instance.Ip, instance.Port, "admin", "password")
		connection, err := cf.CreateConnection()
		defer connection.Close()
		if err != nil {
			log.Error("failed to get connection", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get connection"))

			return
		}
		userService := *connection.GetUserService()
		err = userService.DeleteUser(username)
		if err != nil {
			log.Error("failed to delete user", logger.Err(err))

			render.JSON(w, r, response.Error("failed to delete user"))

			return
		}

		render.JSON(w, r, response.OK())
	}
}

type PostRequest struct {
	Username    string `json:"username" validate:"required,alphanum"`
	Password    string `json:"password" validate:"required"`
	IsSuperuser bool   `json:"isSuperuser"`
}

func NewPost(log *slog.Logger, st *postgres.Storage) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		instanceIdStr := chi.URLParam(r, "instanceId")

		var req PostRequest

		err := render.DecodeJSON(r.Body, &req)

		if errors.Is(err, io.EOF) {
			log.Error("request body is empty")

			render.JSON(w, r, response.Error("empty request"))

			return
		}
		if err != nil {
			log.Error("failed to decode request body", logger.Err(err))

			render.JSON(w, r, response.Error("failed to decode request"))

			return
		}

		log.Info("request body decoded", slog.Any("request", req))

		if err := validator.New().Struct(req); err != nil {
			validateErr := err.(validator.ValidationErrors)

			log.Error("invalid request", logger.Err(err))

			render.JSON(w, r, response.ValidationError(validateErr))

			return
		}

		strg := *st
		instanceId, err := strconv.Atoi(instanceIdStr)
		if err != nil {
			log.Error("failed to get instance", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get instance"))

			return
		}
		instance, err := strg.GetInstance(instanceId)
		if err != nil {
			log.Error("failed to get instance", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get instance"))

			return
		}
		if instance.Status != "RUNNING" {
			log.Info(fmt.Sprintf("instance %d is not running", instanceId))

			render.JSON(w, r, response.Error(fmt.Sprintf("instance %d is not running", instanceId)))

			return
		}
		cf := conn.NewConnectionFactory(instance.Ip, instance.Port, "admin", "password")
		connection, err := cf.CreateConnection()
		defer connection.Close()
		if err != nil {
			log.Error("failed to get connection", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get connection"))

			return
		}
		userService := *connection.GetUserService()
		err = userService.CreateUser(req.Username, req.Password, req.IsSuperuser)
		if err != nil {
			log.Error("failed to create user", logger.Err(err))

			render.JSON(w, r, response.Error(fmt.Sprint(err)))

			return
		}

		render.JSON(w, r, response.OK())
	}
}
