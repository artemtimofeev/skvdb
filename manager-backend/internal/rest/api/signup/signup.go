package signup

import (
	"errors"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/render"
	"github.com/go-playground/validator/v10"
	"io"
	"log/slog"
	"manager-backend/internal/lib/hash"
	"manager-backend/internal/lib/logger"
	"manager-backend/internal/lib/response"
	"manager-backend/internal/storage"
	"manager-backend/internal/storage/postgres"
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

type UserCreator interface {
	CreateUser(username string, password string) error
}

func NewPost(log *slog.Logger, userCreator UserCreator, strg *postgres.Storage) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		const op = "rest.api.signup.NewPost"

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

		err = userCreator.CreateUser(req.Username, hash.Get(req.Password))
		if errors.Is(err, storage.ErrUsernameExists) {
			log.Info("username already exists", slog.String("username", req.Username))

			render.JSON(w, r, response.Error("username already exists"))

			return
		}
		if err != nil {
			log.Error("failed to create user", logger.Err(err))

			render.JSON(w, r, response.Error("failed to create user"))

			return
		}

		log.Info("user created", slog.String("username", req.Username))

		user, err := strg.GetUser(req.Username)
		if errors.Is(err, storage.ErrUsernameNotFound) {
			log.Info("username not found", slog.String("username", req.Username))

			render.JSON(w, r, response.Error("username not found"))

			return
		}
		err = strg.AddTransaction("INFLOW", "Registration bonus", 3000, user.Id)
		if err != nil {
			log.Info("error adding registration bonus", slog.String("username", req.Username))

			render.JSON(w, r, response.Error("error adding registration bonus"))

			return
		}

		// TODO: implement token authentication
		responseOK(w, r, req.Username)
	}
}

func responseOK(w http.ResponseWriter, r *http.Request, token string) {
	render.JSON(w, r, Response{
		Response: response.OK(),
		Token:    token,
	})
}
