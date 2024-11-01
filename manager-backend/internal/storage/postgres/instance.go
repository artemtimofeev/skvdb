package postgres

import (
	"database/sql"
	"errors"
	"fmt"
	"manager-backend/internal/dto"
	"manager-backend/internal/storage"
)

func (s *Storage) GetAllInstances(username string) ([]dto.Instance, error) {
	const op = "storage.postgres.GetUser"

	stmt, err := s.db.Prepare("SELECT id FROM users WHERE username = $1")
	if err != nil {
		return nil, fmt.Errorf("%s: prepare statement: %w", op, err)
	}

	var userId int

	err = stmt.QueryRow(username).Scan(&userId)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, storage.ErrUsernameNotFound
		}

		return nil, fmt.Errorf("%s: execute statement: %w", op, err)
	}

	stmt, err = s.db.Prepare("SELECT id, user_id, instance_name, ip, port, status, rate FROM instances WHERE user_id = $1")
	if err != nil {
		return nil, fmt.Errorf("%s: prepare statement: %w", op, err)
	}

	rows, err := stmt.Query(userId)
	if err != nil {
		return nil, fmt.Errorf("%s: execute statement: %w", op, err)
	}

	instances := make([]dto.Instance, 0)

	for rows.Next() {
		var instance dto.Instance
		if err := rows.Scan(&instance.Id, &instance.UserId, &instance.Name, &instance.Ip, &instance.Port, &instance.Status, &instance.Rate); err != nil {
			return nil, fmt.Errorf("%s: read row: %w", op, err)
		}
		instances = append(instances, instance)
	}

	return instances, nil
}
