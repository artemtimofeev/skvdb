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
	stmt, err := s.db.Prepare("INSERT INTO users (username, password_hash, balance) VALUES ($1, $2, $3)")
	if err != nil {
		return fmt.Errorf("%s: prepare statement: %w", op, err)
	}

	_, err = stmt.Exec(username, passwordHash, 300)
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

	stmt, err := s.db.Prepare("SELECT id, username, password_hash, balance FROM users WHERE username = $1")
	if err != nil {
		return dto.User{}, fmt.Errorf("%s: prepare statement: %w", op, err)
	}

	var user dto.User

	err = stmt.QueryRow(username).Scan(&user.Id, &user.Username, &user.PasswordHash, &user.Balance)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return dto.User{}, storage.ErrUsernameNotFound
		}

		return dto.User{}, fmt.Errorf("%s: execute statement: %w", op, err)
	}

	return user, nil
}

func (s *Storage) GetUserById(userId int) (dto.User, error) {
	const op = "storage.postgres.GetUser"

	stmt, err := s.db.Prepare("SELECT id, username, password_hash, balance FROM users WHERE id = $1")
	if err != nil {
		return dto.User{}, fmt.Errorf("%s: prepare statement: %w", op, err)
	}

	var user dto.User

	err = stmt.QueryRow(userId).Scan(&user.Id, &user.Username, &user.PasswordHash, &user.Balance)
	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return dto.User{}, storage.ErrUsernameNotFound
		}

		return dto.User{}, fmt.Errorf("%s: execute statement: %w", op, err)
	}

	return user, nil
}

func (s *Storage) UpdateUser(user dto.User) error {
	const op = "storage.postgres.UpdateUser"

	stmt, err := s.db.Prepare("UPDATE users SET username = $2, password_hash = $3, balance = $4 WHERE id = $1")
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}

	_, err = stmt.Exec(user.Id, user.Username, user.PasswordHash, user.Balance)
	if err != nil {
		return fmt.Errorf("%s: %w", op, err)
	}

	return nil
}
