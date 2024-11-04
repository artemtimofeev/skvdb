package server

import "time"

type Cloud struct {
	YandexPassportOauthToken string
	IAmToken                 Token
}

type Token struct {
	IamToken  string
	ExpiresAt time.Time
}

func NewCloud(YandexPassportOauthToken string) *Cloud {
	cloud := Cloud{}
	cloud.YandexPassportOauthToken = YandexPassportOauthToken
	IAmToken, err := cloud.UpdateToken()
	if err != nil {
		panic(err)
	}
	cloud.IAmToken = IAmToken
	return &cloud
}
