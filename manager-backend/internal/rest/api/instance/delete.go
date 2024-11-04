package instance

import (
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/render"
	"log/slog"
	"manager-backend/internal/lib/logger"
	"manager-backend/internal/lib/response"
	"manager-backend/internal/service/cloud/server"
	"manager-backend/internal/storage/postgres"
	"net/http"
)

func NewDelete(log *slog.Logger, cloud server.Cloud, storage *postgres.Storage) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		const op = "rest.api.instance.NewDelete"

		instanceId := chi.URLParam(r, "instanceId")

		log := log.With(
			slog.String("op", op),
			slog.String("request_id", middleware.GetReqID(r.Context())),
		)

		instance, err := storage.GetInstance(instanceId)
		if err != nil {
			log.Error("failed to get instance", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get instance"))

			return
		}

		err = cloud.DeleteInstance(instance.ServerId)
		if err != nil {
			log.Error("failed to delete instance", logger.Err(err))

			render.JSON(w, r, response.Error("failed to delete instance"))

			return
		}

		err = storage.DeleteInstance(instanceId)
		if err != nil {
			log.Error("failed to delete instance", logger.Err(err))

			render.JSON(w, r, response.Error("failed to delete instance"))

			return
		}

		render.JSON(w, r, response.OK())
	}
}
