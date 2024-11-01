package login

import (
	"errors"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/render"
	"github.com/go-playground/validator/v10"
	"io"
	"log/slog"
	"manager-backend/internal/dto"
	"manager-backend/internal/lib/hash"
	"manager-backend/internal/lib/logger"
	"manager-backend/internal/lib/response"
	"manager-backend/internal/storage"
	"net/http"
)

type Request struct {
	Username string `json:"username" validate:"required,alphanum"`
	Password string `json:"password" validate:"required"`
}

type Response struct {
	response.Response
	Token string `json:"token"`
}

type UserGetter interface {
	GetUser(username string) (dto.User, error)
}

func NewPost(log *slog.Logger, userGetter UserGetter) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		const op = "rest.api.login.NewPost"

		log := log.With(
			slog.String("op", op),
			slog.String("request_id", middleware.GetReqID(r.Context())),
		)

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

		log.Info("request body decoded", slog.Any("request", req))

		if err := validator.New().Struct(req); err != nil {
			validateErr := err.(validator.ValidationErrors)

			log.Error("invalid request", logger.Err(err))

			render.JSON(w, r, response.ValidationError(validateErr))

			return
		}

		user, err := userGetter.GetUser(req.Username)
		if errors.Is(err, storage.ErrUsernameNotFound) {
			log.Info("username not found", slog.String("username", req.Username))

			render.JSON(w, r, response.Error("username not found"))

			return
		}
		if err != nil {
			log.Error("failed to check username & password", logger.Err(err))

			render.JSON(w, r, response.Error("failed to check username & password"))

			return
		}

		if user.PasswordHash != hash.Get(req.Password) {
			log.Error("invalid password", slog.String("username", req.Username))
			render.JSON(w, r, response.Error("invalid password"))

			return
		}

		log.Info("user logged in", slog.String("username", req.Username))

		// TODO: token authentication
		responseOK(w, r, req.Username)
	}
}

func responseOK(w http.ResponseWriter, r *http.Request, token string) {
	render.JSON(w, r, Response{
		Response: response.OK(),
		Token:    token,
	})
}
