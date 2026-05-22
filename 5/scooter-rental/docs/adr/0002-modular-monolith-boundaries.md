# ADR 0002: Monolith

Я виділив два модулі:

- `core` - основний контекст scooter-rental;
- `analytics` - read-only контекст для статистики.

Core містить користувачів, скутери, оренди, auth/security, command/query handlers,
domain models, value objects, factories, repositories, persistence adapters і
основні REST controllers.

Analytics містить власні read models, projection repository, event handlers, ACL
translators і endpoint `/api/analytics/dashboard`.

Технічні контракти Event Bus винесені в `shared.event`. Public contract Core
винесений у `core.api.event`.

## Межі

Core відповідає за бізнес-операції та інваріанти:

- реєстрація користувача;
- створення, оновлення і видалення скутерів;
- створення, оновлення і видалення оренд;
- перевірка прав доступу;
- збереження domain state.

Analytics відповідає тільки за статистику:

- кількість зареєстрованих користувачів;
- кількість створених скутерів;
- кількість створених, активних, завершених і видалених оренд;
- rentals by day;
- scooters by status;
- last activity time.

Analytics не змінює Core і не імпортує Core domain, repositories, factories,
command/query handlers або persistence entities.

## Наслідок

Плюси:

- краща ізоляція контекстів;
- Core не залежить від Analytics;
- Analytics можна простіше винести в окремий сервіс у майбутньому;
- public events стали явним контрактом;
- ACL захищає Analytics від чужої внутрішньої моделі.

Мінуси:

- потрібно підтримувати public contracts;
- консистентність івентів складніша за прямий синхронний виклик;
- треба буде думати про ідемпотентність і дублікати івентів.
