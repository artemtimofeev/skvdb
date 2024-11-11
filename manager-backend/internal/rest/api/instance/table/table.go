package table

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

type Table struct {
	Name string `json:"name"`
}

type GetResponse struct {
	response.Response
	Tables []Table `json:"tables"`
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

		skvdbStorage := *connection.GetStorage()
		tables, err := skvdbStorage.GetAllTables()
		if err != nil {
			log.Error("failed to get tables", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get tables"))

			return
		}

		tablesResult := make([]Table, 0)
		for _, table := range tables {
			tablesResult = append(tablesResult, Table{Name: table.GetTableMetaData().Name})
		}

		render.JSON(w, r, GetResponse{
			Response: response.OK(),
			Tables:   tablesResult,
		})
	}
}

func NewDelete(log *slog.Logger, st *postgres.Storage) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		instanceIdStr := chi.URLParam(r, "instanceId")
		tableName := chi.URLParam(r, "tableName")

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

		skvdbStorage := *connection.GetStorage()
		err = skvdbStorage.DeleteTable(tableName)
		if err != nil {
			log.Error("failed to delete table", logger.Err(err))

			render.JSON(w, r, response.Error("failed to delete table"))

			return
		}

		render.JSON(w, r, response.OK())
	}
}

type PostRequest struct {
	TableName string `json:"tableName" validate:"required,alphanum"`
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

		skvdbStorage := *connection.GetStorage()
		_, err = skvdbStorage.CreateTable(req.TableName)
		if err != nil {
			log.Error("failed to create table", logger.Err(err))

			render.JSON(w, r, response.Error("failed to create table"))

			return
		}

		render.JSON(w, r, response.OK())
	}
}
