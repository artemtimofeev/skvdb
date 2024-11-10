package entity

type User struct {
	Username    string      `json:"username"`
	IsSuperuser bool        `json:"isSuperuser"`
	Authorities []Authority `json:"authorities"`
}
