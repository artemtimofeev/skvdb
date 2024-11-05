package main

import (
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/chi/v5/middleware"
	"github.com/go-chi/cors"
	"log/slog"
	"manager-backend/internal/config"
	"manager-backend/internal/daemon"
	"manager-backend/internal/lib/logger"
	"manager-backend/internal/rest/api/balance"
	"manager-backend/internal/rest/api/billing"
	"manager-backend/internal/rest/api/instance"
	"manager-backend/internal/rest/api/instance/dblogs"
	"manager-backend/internal/rest/api/instance/operation"
	"manager-backend/internal/rest/api/instance/permission"
	"manager-backend/internal/rest/api/instance/table"
	"manager-backend/internal/rest/api/instance/user"
	"manager-backend/internal/rest/api/login"
	"manager-backend/internal/rest/api/signup"
	"manager-backend/internal/service/cloud/server"
	"manager-backend/internal/storage/postgres"
	"net/http"
	"os"
	"time"
)

const (
	envLocal = "local"
	envDev   = "dev"
	envProd  = "prod"
)

func main() {

	cfg := config.MustLoad()

	log := setupLogger(cfg.Env)

	cloud := *server.NewCloud(cfg.YandexPassportOAuthToken)

	log.Info("starting kvdb backend")

	storage, err := postgres.New()
	if err != nil {
		log.Error("failed to init storage", logger.Err(err))
		os.Exit(1)
	}

	ticker := time.NewTicker(10 * time.Second)
	defer ticker.Stop()
	go daemon.Initializer(log, storage, ticker, cloud)

	ticker2 := time.NewTicker(1 * time.Second)
	defer ticker.Stop()
	go daemon.Biller(log, storage, ticker2, cloud)

	r := chi.NewRouter()
	r.Use(middleware.Logger)
	r.Use(cors.Handler(cors.Options{
		AllowedOrigins:   []string{"*"}, // Allow all origins
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"*"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: true,
		MaxAge:           300, // Maximum value not ignored by any browser
	}))

	r.Post("/api/signup", signup.NewPost(log, storage, storage))

	r.Post("/api/login", login.NewPost(log, storage))

	r.Get("/api/instance/{instanceId}/dblogs", dblogs.NewGet(log))

	r.Get("/api/instance/{instanceId}/operation", operation.NewGet(log))

	r.Delete("/api/instance/{instanceId}", instance.NewDelete(log, cloud, storage))

	r.Get("/api/instance/{instanceId}/table", table.NewGet(log))

	r.Post("/api/instance/{instanceId}/table", table.NewPost(log))

	r.Delete("/api/instance/{instanceId}/table/{tableName}", table.NewDelete(log))

	r.Post("/api/instance/{instanceId}/permission", permission.NewPost(log))

	r.Post("/api/instance/{instanceId}/user", user.NewPost(log))

	r.Delete("/api/instance/{instanceId}/user/{username}", user.NewDelete(log))

	r.Get("/api/instance/{instanceId}/user", user.NewGet(log))

	r.Post("/api/instance", instance.NewPost(log, cloud, storage))

	r.Get("/api/instance", instance.NewGet(log, storage))

	r.Get("/api/balance", balance.NewGet(log, storage))

	r.Get("/api/billing", billing.NewGet(log, storage))

	http.ListenAndServe(":3001", r)
}

func setupLogger(env string) *slog.Logger {
	var log *slog.Logger

	switch env {
	case envLocal:
		log = slog.New(slog.NewTextHandler(os.Stdout, &slog.HandlerOptions{Level: slog.LevelDebug}))
	}

	return log
}
