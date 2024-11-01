package server

import (
	"encoding/json"
	"fmt"
)

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
		diskSpec   struct {
			Size    int    `json:"size"`
			TypeId  string `json:"typeId"`
			ImageId string `json:"imageId"`
		}
	} `json:"bootDiskSpec"`
}

func Create() {
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
			diskSpec   struct {
				Size    int    `json:"size"`
				TypeId  string `json:"typeId"`
				ImageId string `json:"imageId"`
			}
		}{AutoDelete: true, diskSpec: struct {
			Size    int    `json:"size"`
			TypeId  string `json:"typeId"`
			ImageId string `json:"imageId"`
		}{Size: 21474836480, TypeId: "network-hdd", ImageId: "fd8tvc3529h2cpjvpkr5"}},
	}
	jsonData, err := json.Marshal(data)
	if err != nil {
		panic(err)
	}
	fmt.Println(string(jsonData))
}
