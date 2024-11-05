package daemon

import (
	"fmt"
	"log/slog"
	"manager-backend/internal/lib/logger"
	"manager-backend/internal/service/cloud/server"
	"manager-backend/internal/storage/postgres"
	"time"
)

func Biller(log *slog.Logger, storage *postgres.Storage, ticker *time.Ticker, cloud server.Cloud) {
	for {
		select {
		case <-ticker.C:
			instances, err := storage.GetAllRunningInstances()
			if err != nil {
				log.Error("Error getting running instances", logger.Err(err))
				continue
			}

			for _, instance := range instances {
				if instance.PaidTill.Before(time.Now().Add(time.Second * 10)) {
					startTime := instance.PaidTill
					endTime := instance.PaidTill.Add(time.Minute * 5)

					userId := instance.UserId
					user, err := storage.GetUserById(userId)
					if err != nil {
						log.Error("Error getting user", logger.Err(err))
						continue
					}

					if user.Balance < instance.Rate {
						err = cloud.DeleteInstance(instance.ServerId)
						if err != nil {
							log.Error("failed to delete instance", logger.Err(err))

							continue
						}

						err = storage.DeleteInstance(instance.Id)
						if err != nil {
							log.Error("failed to delete instance", logger.Err(err))

							continue
						}

						continue
					}

					instance.PaidTill = instance.PaidTill.Add(time.Minute * 5)
					user.Balance -= instance.Rate
					err = storage.UpdateUser(user)
					if err != nil {
						log.Error("Error updating user", logger.Err(err))
						continue
					}
					err = storage.AddTransaction("OUTFLOW", fmt.Sprintf("Payment for interval from %s to %s, instance id is %d", startTime.Format("2006-01-02 15:04:05"), endTime.Format("2006-01-02 15:04:05"), instance.Id), instance.Rate, userId)
					if err != nil {
						log.Error("Error adding transaction", logger.Err(err))
						continue
					}
					err = storage.UpdateInstance(instance)
					if err != nil {
						log.Error("Error updating instance", logger.Err(err))
					}
				}
			}
		}
	}
}
