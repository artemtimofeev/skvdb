package server

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
)

type Instances struct {
	Instances []Instance `json:"instances"`
}

type Instance struct {
	Id                string             `json:"id"`
	Status            string             `json:"status"`
	NetworkInterfaces []NetworkInterface `json:"networkInterfaces"`
}

type NetworkInterface struct {
	PrimaryV4Address struct {
		OneToOneNat struct {
			Address string `json:"address"`
		} `json:"oneToOneNat"`
	} `json:"primaryV4Address"`
}

func (C *Cloud) GetInstances() (Instances, error) {
	const op = "service.cloud.server.GetInstances"

	req, err := http.NewRequest("GET", fmt.Sprintf("https://compute.api.cloud.yandex.net/compute/v1/instances?folderId=%s", "b1gru2r0l5r64dmbui4d"), nil)
	if err != nil {
		return Instances{}, fmt.Errorf("%s: %w", op, err)
	}

	Token, err := C.GetToken()
	if err != nil {
		return Instances{}, fmt.Errorf("%s: %w", op, err)
	}

	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", fmt.Sprintf("Bearer %s", Token.IamToken))

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return Instances{}, fmt.Errorf("%s: %w", op, err)
	}

	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return Instances{}, fmt.Errorf("%s: %w", op, err)
	}

	var instances Instances

	err = json.Unmarshal(body, &instances)
	if err != nil {
		return Instances{}, fmt.Errorf("%s: %w", op, err)
	}

	return instances, nil
}
