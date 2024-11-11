package table

import (
	"fmt"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/conn"
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/render"
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
		skvdbStorage.
	}
}

func NewPost(log *slog.Logger) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		//instanceId := chi.URLParam(r, "instanceId")
	}
}
