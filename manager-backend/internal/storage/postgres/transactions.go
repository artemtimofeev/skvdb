package postgres

import (
	"fmt"
	"manager-backend/internal/dto"
)

func (s *Storage) AddTransaction(transactionType string, description string, amount int, userId int) error {
	const op = "storage.postgres.AddTransaction"

	stmt, err := s.db.Prepare("INSERT INTO transactions (transaction_type, description, amount, user_id) VALUES ($1, $2, $3, $4)")
	if err != nil {
		return fmt.Errorf("%s: prepare statement: %w", op, err)
	}

	_, err = stmt.Exec(transactionType, description, amount, userId)
	if err != nil {
		return fmt.Errorf("%s: execute statement: %w", op, err)
	}

	return nil
}

func (s *Storage) GetAllTransaction(userId int) ([]dto.Transaction, error) {
	const op = "storage.postgres.GetAllTransaction"

	stmt, err := s.db.Prepare("SELECT id, transaction_type, description, amount, user_id FROM transactions WHERE user_id = $1")
	if err != nil {
		return nil, fmt.Errorf("%s: prepare statement: %w", op, err)
	}

	rows, err := stmt.Query(userId)
	if err != nil {
		return nil, fmt.Errorf("%s: execute statement: %w", op, err)
	}

	transactions := make([]dto.Transaction, 0)

	for rows.Next() {
		var transaction dto.Transaction
		if err := rows.Scan(&transaction.Id, &transaction.TransactionType, &transaction.Description, &transaction.Amount, &transaction.UserId); err != nil {
			return nil, fmt.Errorf("%s: read row: %w", op, err)
		}
		transactions = append(transactions, transaction)
	}

	return transactions, nil
}
