package permission

import (
	"log/slog"
	"net/http"
)

func NewPost(log *slog.Logger) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		//instanceId := chi.URLParam(r, "instanceId")
	}
}
