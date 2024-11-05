package billing

import (
	"errors"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/render"
	"log/slog"
	"manager-backend/internal/dto"
	"manager-backend/internal/lib/logger"
	"manager-backend/internal/lib/response"
	"manager-backend/internal/storage"
	"manager-backend/internal/storage/postgres"
	"net/http"
)

type Response struct {
	response.Response
	Transactions []dto.Transaction `json:"transactions"`
}

func NewGet(log *slog.Logger, strg *postgres.Storage) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		const op = "rest.api.balance.NewGet"

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

		user, err := strg.GetUser(username)
		if errors.Is(err, storage.ErrUsernameNotFound) {
			log.Info("username not found", slog.String("username", username))

			render.JSON(w, r, response.Error("username not found"))

			return
		}
		if err != nil {
			log.Error("failed to get user", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get user"))

			return
		}

		transactions, err := strg.GetAllTransaction(user.Id)
		if err != nil {
			log.Error("failed to get all transactions", logger.Err(err))

			render.JSON(w, r, response.Error("failed to get all transactions"))

			return
		}

		render.JSON(w, r, Response{
			Response:     response.OK(),
			Transactions: transactions,
		})
	}
}
