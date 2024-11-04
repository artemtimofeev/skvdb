package server

import (
	"golang.org/x/crypto/ssh"
	"io/ioutil"
	"log"
	"os"
)

func Connect() {
	username := "admin"
	keyPath := "C:\\Users\\temat\\.ssh\\openssh_private" // Путь к вашему приватному ключу
	host := "51.250.31.81:22"                            // Замените на IP-адрес или доменное имя вашего сервера

	// Чтение приватного ключа
	key, err := ioutil.ReadFile(keyPath)
	if err != nil {
		log.Fatalf("Unable to read private key: %s", err)
	}

	// Создание конфигурации SSH
	signer, err := ssh.ParsePrivateKey(key)
	if err != nil {
		log.Fatalf("Unable to parse private key: %s", err)
	}

	config := &ssh.ClientConfig{
		User: username,
		Auth: []ssh.AuthMethod{
			ssh.PublicKeys(signer),
		},
		HostKeyCallback: ssh.InsecureIgnoreHostKey(), // Не рекомендуется для продакшн-окружения
	}

	// Подключение к серверу
	client, err := ssh.Dial("tcp", host, config)
	if err != nil {
		log.Fatalf("Failed to dial: %s", err)
	}
	defer client.Close()

	// Выполнение команды на удаленном сервере
	session, err := client.NewSession()
	if err != nil {
		log.Fatalf("Failed to create session: %s", err)
	}
	defer session.Close()

	// Чтение вывода команды
	output, err := session.CombinedOutput("ls -l") // Замените на вашу команду
	if err != nil {
		log.Fatalf("Failed to run: %s", err)
	}

	// Вывод результата
	os.Stdout.Write(output)
}
