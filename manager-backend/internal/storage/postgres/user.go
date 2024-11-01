package postgres

import (
	"database/sql"
	"errors"
	"fmt"
	"github.com/lib/pq"
	"manager-backend/internal/dto"
	"manager-backend/internal/storage"
)

func (s *Storage) CreateUser(username string, passwordHash string) error {
	const op = "storage.postgres.CreateUser"
	stmt, err := s.db.Prepare("INSERT INTO users (username, password_hash) VALUES ($1, $2)")
	if err != nil {
		return fmt.Errorf("%s: prepare statement: %w", op, err)
	}

	_, err = stmt.Exec(username, passwordHash)
	if err != nil {
		var pqErr *pq.Error
		if errors.As(err, &pqErr) && pqErr.Code == "23505" {
			return storage.ErrUsernameExists
		}
		return fmt.Errorf("%s: execute statement: %w", op, err)
	}

	return nil
}

func (s *Storage) GetUser(username string) (dto.User, error) {
	const op = "storage.postgres.GetUser"

	stmt, err := s.db.Prepare("SELECT id, username, password_hash FROM users WHERE username = $1")
	if err != nil {
		return dto.User{}, fmt.Errorf("%s: prepare statement: %w", op, err)
	}

	var user dto.User

	err = stmt.QueryRow(username).Scan(&user.Id, &user.Username, &user.PasswordHash)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return dto.User{}, storage.ErrUsernameNotFound
		}

		return dto.User{}, fmt.Errorf("%s: execute statement: %w", op, err)
	}

	return user, nil
}
