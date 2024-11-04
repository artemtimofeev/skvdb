package daemon

import (
	"fmt"
	"log/slog"
	"manager-backend/internal/lib/logger"
	"manager-backend/internal/service/cloud/server"
	"manager-backend/internal/storage/postgres"
	"time"
)

func Initializer(log *slog.Logger, storage *postgres.Storage, ticker *time.Ticker, cloud server.Cloud) {
	for {
		select {
		case <-ticker.C:
			instances, err := storage.GetAllInitializingInstances()
			if err != nil {
				log.Error("Error getting initializing instances", logger.Err(err))
				continue
			}
			serverInstances, err := cloud.GetInstances()
			serverInstancesSlice := serverInstances.Instances
			if err != nil {
				log.Error("Error getting instances", logger.Err(err))
				continue
			}
			for _, instance := range instances {
				id := instance.ServerId
				for _, serverInstance := range serverInstancesSlice {
					if serverInstance.Id == id && serverInstance.Status == "RUNNING" {
						if serverInstance.NetworkInterfaces == nil {
							log.Error(fmt.Sprintf("Error getting instance %s network interfaces", serverInstance.Id))
							continue
						}
						if len(serverInstance.NetworkInterfaces) == 0 {
							log.Error(fmt.Sprintf("Error getting instance %s network interfaces", serverInstance.Id))
							continue
						}
						ip := serverInstance.NetworkInterfaces[0].PrimaryV4Address.OneToOneNat.Address
						if ip == "" {
							log.Error(fmt.Sprintf("Error getting instance %s ip", serverInstance.Id))
							continue
						}

						instance.Status = "RUNNING"
						instance.Ip = ip
						instance.Port = "4040"

						err := storage.UpdateInstance(instance)
						if err != nil {
							log.Error("Error update instance", logger.Err(err))
							continue
						}
					}
				}
			}
		}
	}
}
