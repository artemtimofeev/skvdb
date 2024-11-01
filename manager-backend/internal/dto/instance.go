package dto

type Instance struct {
	Id     int    `json:"id"`
	UserId int    `json:"user_id"`
	Name   string `json:"name"`
	Ip     string `json:"ip"`
	Port   string `json:"port"`
	Status string `json:"status"`
	Rate   string `json:"rate"`
}
