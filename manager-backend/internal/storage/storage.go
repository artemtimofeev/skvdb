package storage

import "errors"

var (
	ErrUsernameExists   = errors.New("username already exists")
	ErrUsernameNotFound = errors.New("username not found")
)
