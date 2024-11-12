FROM golang:1.23 AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN go build -o myapp .

# Создаем финальный образ
FROM alpine:latest
WORKDIR /root/
COPY --from=builder /app/myapp .
EXPOSE 80
CMD ["./myapp"]