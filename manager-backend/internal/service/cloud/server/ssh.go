package server

import (
	"fmt"
	"golang.org/x/crypto/ssh"
	"os"
)

func InstallSkvdb(ip string) error {
	const op = "service.cloud.server.InstallSkvdb"

	username := "admin"
	keyPath := "C:\\Users\\temat\\.ssh\\openssh_private"
	host := ip + ":22"

	key, err := os.ReadFile(keyPath)
	if err != nil {
		return fmt.Errorf(" %s Unable to read private key: %w", op, err)
	}

	signer, err := ssh.ParsePrivateKey(key)
	if err != nil {
		return fmt.Errorf("%s Unable to parse private key: %w", op, err)
	}

	config := &ssh.ClientConfig{
		User: username,
		Auth: []ssh.AuthMethod{
			ssh.PublicKeys(signer),
		},
		HostKeyCallback: ssh.InsecureIgnoreHostKey(),
	}

	client, err := ssh.Dial("tcp", host, config)
	if err != nil {
		return fmt.Errorf("%s Failed to dial: %w", op, err)
	}
	defer client.Close()

	err = runCommand("sudo snap install docker", client)
	if err != nil {
		return fmt.Errorf("%s Failed to install docker: %w", op, err)
	}

	err = runCommand("sudo docker pull artemtimofeev/skvdb:latest", client)
	if err != nil {
		return fmt.Errorf("%s Failed to pull: %w", op, err)
	}

	err = runCommand("sudo docker run -d -p 4004:4004 artemtimofeev/skvdb:latest", client)
	if err != nil {
		return fmt.Errorf("%s Failed to run: %w", op, err)
	}

	return nil
}

func runCommand(cmd string, client *ssh.Client) error {
	const op = "service.cloud.server.runCommand"

	session, err := client.NewSession()
	if err != nil {
		return fmt.Errorf("%s Failed to create session: %w", op, err)
	}
	defer session.Close()

	_, err = session.CombinedOutput(cmd)
	if err != nil {
		return fmt.Errorf("%s Failed to run command %s: %w", op, cmd, err)
	}

	return nil
}
