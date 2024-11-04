package instance

import (
	"errors"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/render"
	"log/slog"
	"manager-backend/internal/dto"
	"manager-backend/internal/lib/logger"
	"manager-backend/internal/lib/response"
	"manager-backend/internal/storage"
	"net/http"
)

type InstanceGetter interface {
	GetAllInstancesByUsername(username string) ([]dto.Instance, error)
}

type Response struct {
	response.Response
	Instances []dto.Instance `json:"instances"`
}

func NewGet(log *slog.Logger, instanceGetter InstanceGetter) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		const op = "rest.api.instance.NewGet"

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

		instances, err := instanceGetter.GetAllInstancesByUsername(username)
		if errors.Is(err, storage.ErrUsernameNotFound) {
			log.Info("username not found", slog.String("username", username))

			render.JSON(w, r, response.Error("username not found"))

			return
		}
		if err != nil {
			log.Error("failed to get instances", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get instances"))

			return
		}

		render.JSON(w, r, Response{
			Response:  response.OK(),
			Instances: instances,
		})
	}
}
