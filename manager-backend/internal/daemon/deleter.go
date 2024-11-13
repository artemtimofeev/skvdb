package daemon

import (
	"log/slog"
	"manager-backend/internal/service/cloud/server"
	"manager-backend/internal/storage/postgres"
	"time"
)

func Deleter(log *slog.Logger, storage *postgres.Storage, ticker *time.Ticker, cloud server.Cloud) {
	for {
		select {
		case <-ticker.C:

		}
	}
}
