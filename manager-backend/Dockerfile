FROM golang:1.23 AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN go build manager-backend/cmd/manager-backend

FROM alpine:latest
WORKDIR /root/
COPY --from=builder /app/manager-backend .
EXPOSE 80
CMD ["./manager-backend"]