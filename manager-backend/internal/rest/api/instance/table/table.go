package table

import (
	"log/slog"
	"net/http"
)

func NewGet(log *slog.Logger) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		//instanceId := chi.URLParam(r, "instanceId")
	}
}

func NewDelete(log *slog.Logger) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		//instanceId := chi.URLParam(r, "instanceId")
		//tableName := chi.URLParam(r, "tableName")
	}
}

func NewPost(log *slog.Logger) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		//instanceId := chi.URLParam(r, "instanceId")
	}
}
