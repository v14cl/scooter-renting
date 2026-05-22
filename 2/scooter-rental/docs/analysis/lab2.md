# Лабораторна робота 2

## З лабораторної роботи 1 в мене вже було
 - Пакети `presentation`, `application`, `domain` та `infrastructure`.
- ORM-сутності вже були винесені в `infrastructure/persistence/entity`
- Spring Data репозиторії знаходились в `infrastructure/persistence/jpa`. 
- Також вже були доменні інтерфейси репозиторіїв, command/query handlers в application і інтеграційні тести HTTP ендпоінтів

## У лабораторній роботі 2
Проект має 4 шари:

- `presentation` —  тут HTTP контролери, request DTO, `presentation/exception/GlobalExceptionHandler`;
- `application` — comman і query хендлери, commands, read models;
- `domain` — доменні моделі, value objects, factories, repository interfaces, domain exceptions;
- `infrastructure` — JPA entities, Spring Data репозиторії, persistence адаптери, мапери, security адаптери, Spring конфігурації.

ORM в мене знаходиться тільки в infrastructure: `UserEntity`, `ScooterEntity`, `RentalEntity`
DTO залишились у presentation. Юз кейси знаходяться в application як command/query хендлери. 
Бізнес інваріанти знаходяться в domain: прості правила в моделях і value objects, складні правила у фабриках через domain repository interfaces.

## Domain Model
Обрано **Rich Domain Model**. Це я пояснив в `docs/adr/0001-domain-model-approach.md`.

Доменні моделі:

- `User` — identity, full name, email, password hash, role, поведінка `isAdmin()`;
- `Scooter` — identity, code, model, status, battery level, price per minute, поведінка `canBeRented()`;
- `Rental` — identity, renter, scooter, rental period, поведінка `belongsTo()`.

Value Objects:

- `Email` — нормалізує email і перевіряє формат;
- `RentalPeriod` — перевіряє `startTime < endTime` і забороняє створення оренди в минулому;
- `PricePerMinute` — перевіряє, що ціна більша за нуль, і нормалізує scale до 2.

Factories:

- `UserFactory` — створює/оновлює користувача та перевіряє унікальність email;
- `ScooterFactory` — створює/оновлює самокат та перевіряє унікальність code;
- `RentalFactory` — створює/оновлює оренду, перевіряє існування самоката, доступність самоката та перетин періодів оренди.

Фабрики приймають залежності через конструктор і залежать тільки від domain interfaces.

## DIP та Persistence
Repository interfaces знаходяться в domain:

- `UserRepository`;
- `ScooterRepository`;
- `RentalRepository`;
- `PasswordHasher`;
- `TokenProvider`.

Їх реалізації знаходяться в infrastructure:

- `UserRepositoryAdapter`, `ScooterRepositoryAdapter`, `RentalRepositoryAdapter`;
- `PasswordHasherAdapter`, `JwtTokenProvider`;
- read adapters `UserReadRepositoryAdapter`, `ScooterReadRepositoryAdapter`, `RentalReadRepositoryAdapter`.

Мапери `UserEntityMapper`, `ScooterEntityMapper`, `RentalEntityMapper` перетворюють ORM entity в domain model і назад. Application та presentation не працюють напряму з JPA entity.

## Переваги
Домен став незалежним від Spring, JPA, Hibernate, HTTP та DTO. Контролери стали тоншими: вони приймають request DTO, створюють application command і викликають handler. Бізнес-правила простіше тестувати без Spring context і без бази даних. Завдяки DIP заміна БД або способу persistence в основному потребує змін тільки в infrastructure layer.

## Недоліки
Стало більше класів: окремі entity, domain models, mappers, factories, adapters, commands і views. Потрібно підтримувати маппінг між ORM і domain. Також треба уважно стежити за залежностями, щоб domain не почав імпортувати framework-класи.

## Тести
Тести розділені на групи:

- domain unit tests: `EmailTest`, `RentalPeriodTest`, `PricePerMinuteTest`, `ScooterTest`, `RentalFactoryTest`;
- application unit test: `UpdateRentalCommandHandlerTest`;
- integration tests: `AuthIntegrationTest`, `UserIntegrationTest`, `ScooterIntegrationTest`, `RentalIntegrationTest`.

Domain та application unit tests не піднімають Spring context і не використовують БД. Integration tests проходять повний шлях HTTP request -> controller -> application -> domain -> infrastructure -> DB -> response.


## Переваги розділення на шари
- **Чітке розділення відповідальностей**: кожен шар відповідає лише за свою область.
- **Легкість тестування**: доменні тести не залежать від БД чи фреймворку, application-тести ізольовані, інтеграційні — повний цикл.
- **Гнучкість**: легко змінити БД або фреймворк, не торкаючись домену.
- **Масштабованість**: простіше додавати нові фічі, не ламаючи існуючу архітектуру.

## Недоліки та ускладнення
- **Більше коду**: через мапери
- **Складніша навігація**: логіка розподілена між шарами, потрібно більше часу на розуміння зв’язків.
- **Маппінг**: додаткові витрати на підтримку маперів між шарами.

## Заміна БД або фреймворку
Завдяки суворому дотриманню DIP та виділенню інтерфейсів у domain, заміна БД або фреймворку можлива без змін у бізнес-логіці. Достатньо реалізувати новий адаптер у infrastructure.

## Обраний підхід до доменної моделі
Було обрано підхід Anemic Domain Model (обґрунтування — див. ADR docs/adr/0001-anemic-domain-model.md): доменні моделі містять лише дані та інваріанти, а вся поведінка винесена у фабрики та сервіси. Це спрощує тестування та підтримку, особливо для невеликих проєктів.


## Виконання критеріїв оцінювання

### 1. Залежності (Dependency Rule)
У проекті повністю відсутні зворотні залежності. Пакет `domain` не містить імпортів Спрінга, JPA, HTTP. 
Правил залежностей я повністю дотримався:
`Presentation → Application → Domain ← Infrastructure`.
Інфраструктурні реалізації як `UserRepositoryAdapter` знаходяться в шарі `infrastructure` і реалізують інтерфейси з `domain`.

### 2. Доменні моделі (Domain Models vs DB Models)
Доменні моделі (`User`, `Scooter`, `Rental`) повністю відокремив від ORM-entity (`UserEntity`, `ScooterEntity`, `RentalEntity`). 
- **Domain Models** реалізовані як `record` або класи з приватними конструкторами, що гарантують валідність стану в будь-який момент часу.
- Вони містять бізнес-логіку (наприклад, `Scooter.canBeRented()`, `User.isAdmin()`).
- Всі інваріанти перевіряються в конструкторах моделей або через Value Objects.

### 3. Domain Factory
Створені фабрики (`UserFactory`, `ScooterFactory`, `RentalFactory`), які:
- Перевіряють складні інваріанти (наприклад, унікальність коду самоката або email користувача).
- Приймають залежності виключно через інтерфейси доменного шару.
- Впроваджуються через конструктор, через що мені легше тестувати їх через мокіто навіть без спрінгу

### 4. Доменні помилки (Error Handling)
Впроваджено ієрархію доменних помилок на основі `DomainException`. Помилки (`ValidationException`, `ConflictException`, `NotFoundException`) нічого не знають про HTTP коди або фреймворк.
Мапінг помилок у статус-коди (400, 404, 409) виніс в `presentation/exception/GlobalExceptionHandler.java` для SRP

### 5. Структура проекту
Проект чітко розділений на 4 пакети

### 6. Тести домену
Доменні тести (`EmailTest`, `RentalPeriodTest`, `ScooterTest`, `RentalFactoryTest`) є чистими unit-тестами.  Без `@SpringBootTest`, і не піднімають бд.
