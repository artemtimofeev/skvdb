package dto

import "time"

type Instance struct {
	Id       int       `json:"id"`
	UserId   int       `json:"user_id"`
	ServerId string    `json:"server_id"`
	Name     string    `json:"name"`
	Ip       string    `json:"ip"`
	Port     int       `json:"port"`
	Status   string    `json:"status"`
	Rate     int       `json:"rate"`
	PaidTill time.Time `json:"paid_till"`
}
