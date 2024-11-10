package dto

type Response struct {
	Result string            `json:"requestResult"`
	Body   map[string]string `json:"body"`
	Error  string            `json:"errorMessage"`
}
