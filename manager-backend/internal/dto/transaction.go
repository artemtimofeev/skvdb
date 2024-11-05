package dto

type Transaction struct {
	Id              int    `json:"transactionId"`
	TransactionType string `json:"type"`
	Description     string `json:"description"`
	Amount          int    `json:"amount"`
	UserId          int    `json:"user_id"`
}
