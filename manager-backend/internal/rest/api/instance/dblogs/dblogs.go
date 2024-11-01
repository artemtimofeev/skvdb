package dblogs

import (
	"log/slog"
	"net/http"
)

func NewGet(log *slog.Logger) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		//instanceId := chi.URLParam(r, "instanceId")
	}
}
