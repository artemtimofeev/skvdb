package instance

import (
	"errors"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/render"
	"github.com/go-playground/validator/v10"
	"io"
	"log/slog"
	"manager-backend/internal/lib/logger"
	"manager-backend/internal/lib/response"
	"manager-backend/internal/service/cloud/server"
	"manager-backend/internal/storage/postgres"
	"net/http"
)

type CreateInstanceRequest struct {
	InstanceName string `json:"instanceName" validate:"required,alphanum"`
}

func NewPost(log *slog.Logger, cloud server.Cloud, storage *postgres.Storage) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		const op = "rest.api.login.NewPost"

		stor := *storage

		log := log.With(
			slog.String("op", op),
			slog.String("request_id", middleware.GetReqID(r.Context())),
		)

		username := r.Header.Get("Authorization")
		if username == "" {
			log.Info("Unauthorized")

			render.JSON(w, r, response.Error("Unauthorized"))

			return
		}

		instancesNotDeleted, err := stor.GetAllNotDeletedInstancesByUsername(username)
		if err != nil {
			log.Error("failed to get not deleted instances", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get not deleted instances"))

			return
		}
		if len(instancesNotDeleted) >= 2 {
			log.Info("can't create more than 2 instances in one account", logger.Err(err))

			render.JSON(w, r, response.Error("can't create more than 2 instances in one account"))

			return
		}

		var req CreateInstanceRequest

		err = render.DecodeJSON(r.Body, &req)

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

		instanceId, err := cloud.CreateInstance()
		if errors.Is(err, server.ErrQuotaExceeded) {
			log.Error("quota exceeded", logger.Err(err))

			render.JSON(w, r, response.Error("Yandex Compute Cloud Quota exceeded (max 8 vps)"))

			return
		}
		if err != nil {
			log.Error("failed to create instance", logger.Err(err))

			render.JSON(w, r, response.Error("failed to create instance"))

			return
		}

		err = stor.CreateInstance(username, req.InstanceName, instanceId)
		if err != nil {
			log.Error("failed to create instance", logger.Err(err))

			render.JSON(w, r, response.Error("failed to create instance"))

			return
		}

		render.JSON(w, r, response.OK())
	}
}
