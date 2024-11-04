package server

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"time"
)

type GetIAmTokenRequest struct {
	YandexPassportOauthToken string `json:"yandexPassportOauthToken"`
}

type GetIAmTokenResponse struct {
	IamToken  string `json:"iamToken"`
	ExpiresAt string `json:"expiresAt"`
}

func (C *Cloud) GetToken() (Token, error) {
	const op = "service.cloud.token.GetToken"

	if time.Now().After(C.IAmToken.ExpiresAt) {
		_, err := C.UpdateToken()
		if err != nil {
			return Token{}, err
		}
	}

	return C.IAmToken, nil
}

func (C *Cloud) UpdateToken() (Token, error) {
	const op = "service.cloud.token.UpdateToken"

	getIAmTokenRequest := GetIAmTokenRequest{
		YandexPassportOauthToken: C.YandexPassportOauthToken,
	}

	jsonData, err := json.Marshal(getIAmTokenRequest)
	if err != nil {
		return Token{}, fmt.Errorf("%s: %w", op, err)
	}

	req, err := http.NewRequest("POST", "https://iam.api.cloud.yandex.net/iam/v1/tokens", bytes.NewBuffer(jsonData))
	if err != nil {
		return Token{}, fmt.Errorf("%s: %w", op, err)
	}

	client := &http.Client{}
	response, err := client.Do(req)
	if err != nil {
		return Token{}, fmt.Errorf("%s: %w", op, err)
	}
	defer response.Body.Close()

	body, err := ioutil.ReadAll(response.Body)
	if err != nil {
		return Token{}, fmt.Errorf("%s: %w", op, err)
	}

	var getIAmTokenResponse GetIAmTokenResponse

	err = json.Unmarshal(body, &getIAmTokenResponse)
	if err != nil {
		return Token{}, fmt.Errorf("%s: %w", op, err)
	}

	expiresAt, err := time.Parse(time.RFC3339Nano, getIAmTokenResponse.ExpiresAt)
	if err != nil {
		return Token{}, fmt.Errorf("%s: %w", op, err)
	}

	token := Token{IamToken: getIAmTokenResponse.IamToken, ExpiresAt: expiresAt}
	C.IAmToken = token

	return token, nil
}
