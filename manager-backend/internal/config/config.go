package config

import (
	"github.com/ilyakaznacheev/cleanenv"
	"log"
	"os"
	"time"
)

type Config struct {
	Env                      string `yaml:"env" env-required:"true"`
	PathToPrivateKey         string `yaml:"path_to_private_key" env-required:"true"`
	YandexPassportOAuthToken string `env:"YANDEX_PASSPORT_OAUTH_TOKEN" env-required:"true"`
	HTTPServer               `yaml:"http_server"`
	Database                 `yaml:"database"`
}

type HTTPServer struct {
	Address     string        `yaml:"address" env-default:"localhost:8080"`
	Timeout     time.Duration `yaml:"timeout" env-default:"4s"`
	IdleTimeout time.Duration `yaml:"idle_timeout" env-default:"60s"`
}

type Database struct {
	Address  string `yaml:"address" env-required:"true"`
	Port     int    `yaml:"port" env-required:"true"`
	User     string `yaml:"user" env-required:"true"`
	Password string `env:"DB_PASSWORD" env-required:"true"`
}

func MustLoad() *Config {
	configPath := os.Getenv("CONFIG_PATH")
	if configPath == "" {
		log.Fatal("CONFIG_PATH environment variable not set")
	}
	if _, err := os.Stat(configPath); os.IsNotExist(err) {
		log.Fatalf("CONFIG_PATH does not exist: %s", configPath)
	}

	var config Config

	if err := cleanenv.ReadConfig(configPath, &config); err != nil {
		log.Fatalf("Error reading config: %s", err)
	}

	return &config
}
