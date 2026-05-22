# Scooter Rental API

REST API для міського прокату електросамокатів

## Доменна модель
- `User` — користувач системи з роллю `CUSTOMER` або `ADMIN`
- `Scooter` — самокат з кодом, статусом, зарядом і ціною за хвилину
- `Rental` — бронювання самоката на часовий слот

### Основні інваріанти
- email має бути валідним;
- email користувача унікальний;
- код самоката унікальний;
- заряд самоката в межах `0..100`;
- ціна за хвилину більша за `0`;
- часовий діапазон бронювання коректний: `start < end`;
- бронювання не може починатися в минулому;
- не можна створити перетин слотів для одного самоката;
- бронювання можливе лише для самоката зі статусом `AVAILABLE`.

## Запуск
Потрібно:
- `Java 17+`
- `Maven 3.8+`

Запуск застосунку:

```bash
mvn spring-boot:run
```

Після запуску:
- API: `http://localhost:8080`
- H2 console: `http://localhost:8080/h2-console`

Локальна БД за замовчуванням:
- `jdbc:h2:file:./data/scooter-rental`
- user: `sa`

## Тестування
Запуск усіх тестів:

```bash
mvn test
```

## Автентифікація
Публічні ендпоінти:
- `POST /api/auth/register`
- `POST /api/auth/login`

Усі інші ендпоінти захищені JWT і без токена повертають `401 Unauthorized`.

Для тестування створюється дефолтний адміністратор:
- email: `admin@scooter.local`
- password: `admin12345`

JWT потрібно передавати в заголовку:

```text
Authorization: Bearer <token>
```

## Основні ендпоінти
Auth:
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/users/me`

Scooters:
- `POST /api/scooters` — тільки `ADMIN`
- `GET /api/scooters`
- `GET /api/scooters/{id}`
- `PUT /api/scooters/{id}` — тільки `ADMIN`
- `DELETE /api/scooters/{id}` — тільки `ADMIN`

Rentals:
- `POST /api/rentals`
- `GET /api/rentals`
- `GET /api/rentals/{id}`
- `PUT /api/rentals/{id}`
- `DELETE /api/rentals/{id}`

## Приклад запитів
Реєстрація:

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Ivan Rider",
    "email": "ivan@example.com",
    "password": "password1"
  }'
```

Створення самоката адміністратором:

```bash
curl -X POST http://localhost:8080/api/scooters \
  -H "Authorization: Bearer <admin-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "SCT-001",
    "model": "Ninebot Max",
    "status": "AVAILABLE",
    "batteryLevel": 90,
    "pricePerMinute": 2.50
  }'
```

Створення бронювання:

```bash
curl -X POST http://localhost:8080/api/rentals \
  -H "Authorization: Bearer <user-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "scooterId": "<scooter-id>",
    "startTime": "2026-05-01T10:00:00",
    "endTime": "2026-05-01T11:00:00"
  }'
```

## Структура пакетів
- `presentation` — контролери, request DTO, HTTP error handling
- `application` — commands, queries, handlers
- `domain` — моделі, value objects, factory, інтерфейси репозиторіїв
- `infrastructure` — JPA entities, Spring Data repositories, мапери, security

## Тести
- unit: доменні інваріанти та application-логіка без БД;
- integration: повний HTTP цикл через Spring + H2.
