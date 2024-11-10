package dto

type Request struct {
	Username   string            `json:"username"`
	Password   string            `json:"password"`
	MethodName string            `json:"methodName"`
	Body       map[string]string `json:"body"`
}
