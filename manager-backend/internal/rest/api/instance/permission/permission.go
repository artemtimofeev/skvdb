package permission

import (
	"errors"
	"fmt"
	"github.com/artemtimofeev/skvdb/go-skvdb-client/src/conn"
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/render"
	"io"
	"log/slog"
	"manager-backend/internal/lib/logger"
	"manager-backend/internal/lib/response"
	"manager-backend/internal/storage/postgres"
	"net/http"
	"strconv"
)

type Request struct {
	TableName string `json:"tableName"`
	Username  string `json:"username"`
	IsOwner   bool   `json:"isOwner"`
	IsReader  bool   `json:"isReader"`
	IsWriter  bool   `json:"isWriter"`
}

func NewPost(log *slog.Logger, st *postgres.Storage) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		instanceIdStr := chi.URLParam(r, "instanceId")

		var req Request

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
		if req.IsOwner {
			err := userService.GrantAuthority(req.Username, "OWNER", req.TableName)
			if err != nil {
				log.Error("failed to change authority", logger.Err(err))

				render.JSON(w, r, response.Error("failed to change authority"))

				return
			}
		} else {
			err := userService.RevokeAuthority(req.Username, "OWNER", req.TableName)
			if err != nil {
				log.Error("failed to change authority", logger.Err(err))

				render.JSON(w, r, response.Error("failed to change authority"))

				return
			}
		}
		if req.IsReader {
			err := userService.GrantAuthority(req.Username, "READ", req.TableName)
			if err != nil {
				log.Error("failed to change authority", logger.Err(err))

				render.JSON(w, r, response.Error("failed to change authority"))

				return
			}
		} else {
			err := userService.RevokeAuthority(req.Username, "READ", req.TableName)
			if err != nil {
				log.Error("failed to change authority", logger.Err(err))

				render.JSON(w, r, response.Error("failed to change authority"))

				return
			}
		}
		if req.IsWriter {
			err := userService.GrantAuthority(req.Username, "WRITE", req.TableName)
			if err != nil {
				log.Error("failed to change authority", logger.Err(err))

				render.JSON(w, r, response.Error("failed to change authority"))

				return
			}
		} else {
			err := userService.RevokeAuthority(req.Username, "WRITE", req.TableName)
			if err != nil {
				log.Error("failed to change authority", logger.Err(err))

				render.JSON(w, r, response.Error("failed to change authority"))

				return
			}
		}

		render.JSON(w, r, response.OK())
	}
}
