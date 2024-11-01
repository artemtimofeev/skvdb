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
	GetAllInstances(username string) ([]dto.Instance, error)
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

		instances, err := instanceGetter.GetAllInstances(username)
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

		responseOK(w, r, instances)
	}
}

func responseOK(w http.ResponseWriter, r *http.Request, instances []dto.Instance) {
	render.JSON(w, r, Response{
		Response:  response.OK(),
		Instances: instances,
	})
}

func NewDelete(log *slog.Logger) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		//instanceId := chi.URLParam(r, "instanceId")
	}
}

func NewPost(log *slog.Logger) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {

	}
}
