package user

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
		//username := chi.URLParam(r, "username")
	}
}

func NewPost(log *slog.Logger) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		//instanceId := chi.URLParam(r, "instanceId")
	}
}
