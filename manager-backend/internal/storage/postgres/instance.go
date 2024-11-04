package postgres

import (
	"fmt"
	"manager-backend/internal/dto"
)

func (s *Storage) GetAllInstancesByUsername(username string) ([]dto.Instance, error) {
	const op = "storage.postgres.GetAllInstancesByUsername"

	user, err := s.GetUser(username)
	if err != nil {
		return nil, fmt.Errorf("%s: %w", op, err)
	}

	stmt, err := s.db.Prepare("SELECT id, user_id, server_id, instance_name, ip, port, status, rate FROM instances WHERE user_id = $1")
	if err != nil {
		return nil, fmt.Errorf("%s: prepare statement: %w", op, err)
	}

	rows, err := stmt.Query(user.Id)
	if err != nil {
		return nil, fmt.Errorf("%s: execute statement: %w", op, err)
	}

	instances := make([]dto.Instance, 0)

	for rows.Next() {
		var instance dto.Instance
		if err := rows.Scan(&instance.Id, &instance.UserId, &instance.ServerId, &instance.Name, &instance.Ip, &instance.Port, &instance.Status, &instance.Rate); err != nil {
			return nil, fmt.Errorf("%s: read row: %w", op, err)
		}
		instances = append(instances, instance)
	}

	return instances, nil
}

func (s *Storage) GetAllInitializingInstances() ([]dto.Instance, error) {
	const op = "storage.postgres.GetAllInstancesByUsername"

	stmt, err := s.db.Prepare("SELECT id, user_id, server_id, instance_name, ip, port, status, rate FROM instances WHERE status = $1")
	if err != nil {
		return nil, fmt.Errorf("%s: prepare statement: %w", op, err)
	}

	rows, err := stmt.Query("INITIALIZING")
	if err != nil {
		return nil, fmt.Errorf("%s: execute statement: %w", op, err)
	}

	instances := make([]dto.Instance, 0)

	for rows.Next() {
		var instance dto.Instance
		if err := rows.Scan(&instance.Id, &instance.UserId, &instance.ServerId, &instance.Name, &instance.Ip, &instance.Port, &instance.Status, &instance.Rate); err != nil {
			return nil, fmt.Errorf("%s: read row: %w", op, err)
		}
		instances = append(instances, instance)
	}

	return instances, nil
}

func (s *Storage) CreateInstance(username string, instanceName string, serverId string) error {
	const op = "storage.postgres.CreateInstance"

	user, err := s.GetUser(username)
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}

	stmt, err := s.db.Prepare("INSERT INTO instances (user_id, server_id, instance_name, ip, port, status, rate) VALUES ($1, $2, $3, $4, $5, $6, $7)")
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}

	_, err = stmt.Exec(user.Id, serverId, instanceName, "", "", "INITIALIZING", 30)
	if err != nil {
		return fmt.Errorf("%s: execute statement: %w", op, err)
	}
	return nil
}

func (s *Storage) UpdateInstance(instance dto.Instance) error {
	const op = "storage.postgres.UpdateInstance"

	stmt, err := s.db.Prepare("UPDATE instances SET user_id = $2, server_id = $3, instance_name = $4, ip = $5, port = $6, status = $7, rate = $8 WHERE id = $1")
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}

	_, err = stmt.Exec(instance.Id, instance.UserId, instance.ServerId, instance.Name, instance.Ip, instance.Port, instance.Status, instance.Rate)
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}

	return nil

}

func (s *Storage) DeleteInstance(instanceId string) error {
	const op = "storage.postgres.DeleteInstance"

	stmt, err := s.db.Prepare("UPDATE instances SET status = 'DELETED', ip = '', port = '' WHERE id = $1")
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}

	_, err = stmt.Exec(instanceId)
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}

	return nil
}

func (s *Storage) GetInstance(instanceId string) (dto.Instance, error) {
	const op = "storage.postgres.GetInstance"

	stmt, err := s.db.Prepare("SELECT id, user_id, server_id, instance_name, ip, port, status, rate FROM instances WHERE id = $1")
	if err != nil {
		return dto.Instance{}, fmt.Errorf("%s: %w", op, err)
	}

	instance := dto.Instance{}

	err = stmt.QueryRow(instanceId).Scan(&instance.Id, &instance.UserId, &instance.ServerId, &instance.Name, &instance.Ip, &instance.Port, &instance.Status, &instance.Rate)
	if err != nil {
		return dto.Instance{}, fmt.Errorf("%s: %w", op, err)
	}

	return instance, nil
}
