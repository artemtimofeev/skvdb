package operation

import (
	"github.com/go-chi/render"
	"log/slog"
	"manager-backend/internal/lib/response"
	"net/http"
)

func NewGet(log *slog.Logger) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		//instanceId := chi.URLParam(r, "instanceId")
		render.JSON(w, r, response.OK())
	}
}
