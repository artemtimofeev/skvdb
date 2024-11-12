package postgres

import (
	"database/sql"
	"fmt"
	_ "github.com/lib/pq"
	"manager-backend/internal/config"
)

type Storage struct {
	db *sql.DB
}

func New(database config.Database) (*Storage, error) {
	const op = "storage.postgres.New"

	connStr := fmt.Sprintf("user=%s dbname=postgres password=%s sslmode=disable port=%d host=%s", database.User, database.Password, database.Port, database.Address)

	db, err := sql.Open("postgres", connStr)
	if err != nil {
		return nil, fmt.Errorf("%s : %w", op, err)
	}

	createUsersTable, err := db.Prepare(`
	CREATE TABLE IF NOT EXISTS users (
	    id SERIAL PRIMARY KEY ,
	    username VARCHAR(255) UNIQUE NOT NULL,
	    password_hash VARCHAR(255) NOT NULL,
		balance INT NOT NULL);
	`)
	if err != nil {
		return nil, fmt.Errorf("%s : %w", op, err)
	}

	_, err = createUsersTable.Exec()
	if err != nil {
		return nil, fmt.Errorf("%s : %w", op, err)
	}

	createInstancesTable, err := db.Prepare(`
	CREATE TABLE IF NOT EXISTS instances (
	    id SERIAL PRIMARY KEY ,
	    user_id INT,
	    server_id VARCHAR(255) NOT NULL,
	    instance_name VARCHAR(255) NOT NULL,
	    ip VARCHAR(255),
	    port INT,
	    status VARCHAR(255) NOT NULL,
	    rate INT NOT NULL,
	    paid_till TIMESTAMPTZ,
	    FOREIGN KEY (user_id) REFERENCES users(id));
	`)
	if err != nil {
		return nil, fmt.Errorf("%s : %w", op, err)
	}

	_, err = createInstancesTable.Exec()
	if err != nil {
		return nil, fmt.Errorf("%s : %w", op, err)
	}

	createTransactionsTable, err := db.Prepare(`
	CREATE TABLE IF NOT EXISTS transactions (
	    id SERIAL PRIMARY KEY,
	    transaction_type VARCHAR(255) NOT NULL,
	    description VARCHAR(255) NOT NULL,
	    amount INT NOT NULL,
	    user_id INT,
	    FOREIGN KEY (user_id) REFERENCES users(id));
	`)
	if err != nil {
		return nil, fmt.Errorf("%s : %w", op, err)
	}

	_, err = createTransactionsTable.Exec()
	if err != nil {
		return nil, fmt.Errorf("%s : %w", op, err)
	}

	createTransactionsIndex, err := db.Prepare(`
	CREATE INDEX IF NOT EXISTS transactions_user_id ON transactions (user_id);
	`)

	if err != nil {
		return nil, fmt.Errorf("%s : %w", op, err)
	}

	_, err = createTransactionsIndex.Exec()
	if err != nil {
		return nil, fmt.Errorf("%s : %w", op, err)
	}

	createOperationsTable, err := db.Prepare(`
	CREATE TABLE IF NOT EXISTS operations (
	    id SERIAL PRIMARY KEY,
	    operation VARCHAR(255) NOT NULL,
	    op_timestamp VARCHAR(255) NOT NULL,
	    instance_id INT,
	    FOREIGN KEY (instance_id) REFERENCES instances(id));
	`)
	if err != nil {
		return nil, fmt.Errorf("%s : %w", op, err)
	}

	_, err = createOperationsTable.Exec()
	if err != nil {
		return nil, fmt.Errorf("%s : %w", op, err)
	}

	return &Storage{db: db}, nil
}

func (s *Storage) Save(kek string) error {
	const op = "storage.postgres.Save"

	createOperationsTable, err := s.db.Prepare(`
	`)
	if err != nil {
		return fmt.Errorf("%s : %w", op, err)
	}

	_, err = createOperationsTable.Exec()
	if err != nil {
		return fmt.Errorf("%s : %w", op, err)
	}
	return nil
}
