# Warehouse CRUD приложение на Kotlin + Spring Boot

## 📌 Описание проекта

Приложение реализует CRUD-функционал для товаров на складе.

## 🚀 Запуск проекта локально (Docker Compose)

1. Установите Docker и Docker Compose.
2. Выполните команду в корне проекта:

```bash
docker compose up -d
```

## 🛠️ Сборка и запуск без Docker

Для запуска без Docker вам понадобится PostgreSQL 16.
- Запустите PostgreSQL и создайте базу warehouse_db.
- Установите переменные в .env или переменные окружения:

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://warehouse-db:5432/test_database_name
SPRING_DATASOURCE_USERNAME=test_user_name
SPRING_DATASOURCE_PASSWORD=test_user_password
SPRING_DATASOURCE_DATABASE_NAME=test_database_name
```
- Выполните команды:

```bash
./gradlew build
./gradlew bootRun
```

## 🧪 Запуск тестов

```bash
./gradlew test
```

## Полезные ссылки

Приложение доступно по адресу:

```http request
http://localhost:8080/api/products
```

Swagger доступен по адресу:

```http request
http://localhost:8080/swagger-ui.html
```