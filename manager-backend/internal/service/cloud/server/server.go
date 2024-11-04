package server

import (
	"bytes"
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"
)

type CreateServerResponse struct {
	Id       string `json:"id"`
	Metadata struct {
		InstanceId string `json:"instanceId"`
	} `json:"metadata"`
	Code int `json:"code"`
}

type CreateServerRequest struct {
	FolderId              string `json:"folderId"`
	NetworkInterfaceSpecs struct {
		Index                string `json:"index"`
		SubnetId             string `json:"subnetId"`
		PrimaryV4AddressSpec struct {
			Address         string `json:"address"`
			OneToOneNatSpec struct {
				IpVersion string `json:"ipVersion"`
			} `json:"oneToOneNatSpec"`
			DnsRecordSpecs []string `json:"dnsRecordSpecs"`
		} `json:"primaryV4AddressSpec"`
		SecurityGroupIds []string `json:"securityGroupIds"`
	} `json:"networkInterfaceSpecs"`
	Description        string   `json:"description"`
	IsSerialPortEnable bool     `json:"isSerialPortEnable"`
	HasUnifiedAgent    bool     `json:"hasUnifiedAgent"`
	Labels             struct{} `json:"labels"`
	ResourcesSpec      struct {
		Cores        int `json:"cores"`
		Memory       int `json:"memory"`
		CoreFraction int `json:"coreFraction"`
	} `json:"resourcesSpec"`
	PlatformId       string `json:"platformId"`
	SchedulingPolicy struct {
		Preemptible bool `json:"preemptible"`
	} `json:"schedulingPolicy"`
	ZoneId          string   `json:"zoneId"`
	PlacementPolicy struct{} `json:"placementPolicy"`
	FilesystemSpecs []string `json:"filesystemSpecs"`
	MetadataOptions struct {
		GceHttpEndpoint   string `json:"gceHttpEndpoint"`
		AwsV1HttpEndpoint string `json:"awsV1HttpEndpoint"`
		GceHttpToken      string `json:"gceHttpToken"`
		AwsV1HttpToken    string `json:"awsV1HttpToken"`
	} `json:"metadataOptions"`
	OsLoginEnabled bool `json:"osLoginEnabled"`
	BootDiskSpec   struct {
		AutoDelete bool `json:"autoDelete"`
		DiskSpec   struct {
			Size    int    `json:"size"`
			TypeId  string `json:"typeId"`
			ImageId string `json:"imageId"`
		} `json:"diskSpec"`
	} `json:"bootDiskSpec"`
	SecondaryDiskSpecs []string `json:"secondaryDiskSpecs"`
	Metadata           struct {
		UserData            string `json:"user-data"`
		SshKeys             string `json:"ssh-keys"`
		SerialPortEnable    string `json:"serial-port-enable"`
		InstallUnifiedAgent string `json:"install-unified-agent"`
	} `json:"metadata"`
}

var (
	ErrQuotaExceeded        = errors.New("quota exceeded")
	ErrAuthenticationFailed = errors.New("authentication failed")
)

func (C *Cloud) CreateInstance() (string, error) {
	const op = "service.cloud.server.CreateInstance"

	data := CreateServerRequest{
		FolderId: "b1gru2r0l5r64dmbui4d",
		NetworkInterfaceSpecs: struct {
			Index                string `json:"index"`
			SubnetId             string `json:"subnetId"`
			PrimaryV4AddressSpec struct {
				Address         string `json:"address"`
				OneToOneNatSpec struct {
					IpVersion string `json:"ipVersion"`
				} `json:"oneToOneNatSpec"`
				DnsRecordSpecs []string `json:"dnsRecordSpecs"`
			} `json:"primaryV4AddressSpec"`
			SecurityGroupIds []string `json:"securityGroupIds"`
		}{Index: "0", SubnetId: "e2led9i4itidg8rrs31v", PrimaryV4AddressSpec: struct {
			Address         string `json:"address"`
			OneToOneNatSpec struct {
				IpVersion string `json:"ipVersion"`
			} `json:"oneToOneNatSpec"`
			DnsRecordSpecs []string `json:"dnsRecordSpecs"`
		}{
			Address: "",
			OneToOneNatSpec: struct {
				IpVersion string `json:"ipVersion"`
			}{IpVersion: "IPV4"},
			DnsRecordSpecs: make([]string, 0),
		}, SecurityGroupIds: []string{"enpd071r17pgqtl606ku"}},
		Description:        "",
		IsSerialPortEnable: false,
		HasUnifiedAgent:    false,
		Labels:             struct{}{},
		ResourcesSpec: struct {
			Cores        int `json:"cores"`
			Memory       int `json:"memory"`
			CoreFraction int `json:"coreFraction"`
		}{Cores: 2, Memory: 2147483648, CoreFraction: 20},
		PlatformId: "standard-v3",
		SchedulingPolicy: struct {
			Preemptible bool `json:"preemptible"`
		}{Preemptible: false},
		ZoneId:          "ru-central1-b",
		PlacementPolicy: struct{}{},
		FilesystemSpecs: make([]string, 0),
		MetadataOptions: struct {
			GceHttpEndpoint   string `json:"gceHttpEndpoint"`
			AwsV1HttpEndpoint string `json:"awsV1HttpEndpoint"`
			GceHttpToken      string `json:"gceHttpToken"`
			AwsV1HttpToken    string `json:"awsV1HttpToken"`
		}{GceHttpEndpoint: "METADATA_OPTION_UNSPECIFIED", AwsV1HttpEndpoint: "METADATA_OPTION_UNSPECIFIED", GceHttpToken: "METADATA_OPTION_UNSPECIFIED", AwsV1HttpToken: "METADATA_OPTION_UNSPECIFIED"},
		OsLoginEnabled: false,
		BootDiskSpec: struct {
			AutoDelete bool `json:"autoDelete"`
			DiskSpec   struct {
				Size    int    `json:"size"`
				TypeId  string `json:"typeId"`
				ImageId string `json:"imageId"`
			} `json:"diskSpec"`
		}{AutoDelete: true, DiskSpec: struct {
			Size    int    `json:"size"`
			TypeId  string `json:"typeId"`
			ImageId string `json:"imageId"`
		}{Size: 21474836480, TypeId: "network-hdd", ImageId: "fd8tvc3529h2cpjvpkr5"}},
		SecondaryDiskSpecs: make([]string, 0),
		Metadata: struct {
			UserData            string `json:"user-data"`
			SshKeys             string `json:"ssh-keys"`
			SerialPortEnable    string `json:"serial-port-enable"`
			InstallUnifiedAgent string `json:"install-unified-agent"`
		}{UserData: "#cloud-config\ndatasource:\n Ec2:\n  strict_id: false\nssh_pwauth: no\nusers:\n- name: admin\n  sudo: ALL=(ALL) NOPASSWD:ALL\n  shell: /bin/bash\n  ssh_authorized_keys:\n  - ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCIsca91fTmqGI8C3BmNXVJyRV/XlcVjwAe/OMVFgosd79I9M2y1+Jgf00ICDXR6OQxBbS6SOeNfwqpkoX6Tv2xSMJuh30Xw41W2qs9na223p2LX+CXjuNSpbz4xk7gY1JqgVQQ9Ys/+wUMU+WhXStSwCjyZj29TddfccxU0tYaI3ppiZDAiVD4jCkFta5G7i/rlTc+sq30gpP4voXTzA8xgaqFq6u6+ppUof5mWg5+xAW9RZ/pLqHMb5z69gTReUEYXubWQu06m1rAaUR5A43xjU6gPvwp27GX0f1PtHkEY5JxykBfVb4v2xEcIaV6iRSGQSTnT8SWdBiT1Q6g7xb1 rsa-key-20241016", SshKeys: "admin:ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCIsca91fTmqGI8C3BmNXVJyRV/XlcVjwAe/OMVFgosd79I9M2y1+Jgf00ICDXR6OQxBbS6SOeNfwqpkoX6Tv2xSMJuh30Xw41W2qs9na223p2LX+CXjuNSpbz4xk7gY1JqgVQQ9Ys/+wUMU+WhXStSwCjyZj29TddfccxU0tYaI3ppiZDAiVD4jCkFta5G7i/rlTc+sq30gpP4voXTzA8xgaqFq6u6+ppUof5mWg5+xAW9RZ/pLqHMb5z69gTReUEYXubWQu06m1rAaUR5A43xjU6gPvwp27GX0f1PtHkEY5JxykBfVb4v2xEcIaV6iRSGQSTnT8SWdBiT1Q6g7xb1 rsa-key-20241016", SerialPortEnable: "0", InstallUnifiedAgent: "0"},
	}
	jsonData, err := json.Marshal(data)
	if err != nil {
		return "", fmt.Errorf("%s: create new server: %w", op, err)
	}

	Token, err := C.GetToken()
	if err != nil {
		return "", fmt.Errorf("%s: create new server: %w", op, err)
	}

	req, err := http.NewRequest("POST", "https://compute.api.cloud.yandex.net/compute/v1/instances", bytes.NewBuffer(jsonData))
	if err != nil {
		return "", fmt.Errorf("%s: create new server: %w", op, err)
	}

	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", fmt.Sprintf("Bearer %s", Token.IamToken))

	client := &http.Client{}
	response, err := client.Do(req)
	if err != nil {
		return "", fmt.Errorf("%s: create new server: %w", op, err)
	}
	defer response.Body.Close()

	body, err := ioutil.ReadAll(response.Body)
	if err != nil {
		return "", fmt.Errorf("%s: create new server: %w", op, err)
	}

	var resp CreateServerResponse
	err = json.Unmarshal(body, &resp)
	if err != nil {
		return "", fmt.Errorf("%s: create new server: %w", op, err)
	}
	if resp.Code == 8 {
		return "", ErrQuotaExceeded
	}
	if resp.Code == 16 {
		return "", ErrAuthenticationFailed
	}

	return resp.Metadata.InstanceId, nil
}

func (C *Cloud) DeleteInstance(id string) error {
	const op = "service.cloud.server.DeleteInstance"

	Token, err := C.GetToken()
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}

	req, err := http.NewRequest("DELETE", fmt.Sprintf("https://compute.api.cloud.yandex.net/compute/v1/instances/%s", id), nil)
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}

	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", fmt.Sprintf("Bearer %s", Token.IamToken))

	client := &http.Client{}
	_, err = client.Do(req)
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}

	return nil
}
