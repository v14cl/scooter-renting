# Лабораторна робота 3

Операції запису в поточному API:

- реєстрація користувача `POST /api/auth/register`;
- створення, оновлення та видалення користувача `POST/PUT/DELETE /api/users`;
- створення, оновлення та видалення скутера `POST/PUT/DELETE /api/scooters`;
- створення, оновлення та видалення оренди `POST/PUT/DELETE /api/rentals`.

`POST /api/auth/login` не змінює стан бази даних, він перевіряє email, password і видає JWT
Тому цей endpoint залишив як окремий auth юзкейс, але він не вважається write operation для доменних моделей оренди, скутерів або користувачів.

Операції читання:
- отримати поточного користувача, користувача за id та список користувачів;
- отримати скутер за id та список скутерів;
- отримати список скутерів із фільтром за `status`;
- отримати оренду за id та список видимих оренд з урахуванням ролі actor.

До лабораторної 3 частина коду вже відповідала CQS: 
у проєкті вже були command records, окремі command handlers, read repositories, views, а також infrastructure adapters для читання.

## Недоліки, які потрібно було виправити:

- деякі query handlers приймали сирі `UUID` або `Actor` замість окремих query objects, я подумав це погано бо handler отримував просто набір окремих параметрів, а не повноцінний об’єкт запит, що буде важче масштабувати
- create/update command handlers повертали read models, але команда має змінювати стан системи, а не одразу формувати результат для читання
- деякі command records містили static `of(...)` методи з parsing та валідацією, треба було прибрати звідти цю логіку

У лабораторній роботі 3 я зробив application layer більш чітко поділеним на command та query side.

Commands залишилися окремими records для операцій запису:

- `RegisterUserCommand`, `LoginCommand`;
- `CreateUserCommand`, `UpdateUserCommand`, `DeleteUserCommand`;
- `CreateScooterCommand`, `UpdateScooterCommand`, `DeleteScooterCommand`;
- `CreateRentalCommand`, `UpdateRentalCommand`, `DeleteRentalCommand`.

Із command records прибрав parsing через `of(...)`. 
Тепер command лише зберігає намір і дані, але не виконує доменну або application validation. 
Command handlers більше не повертають read models. 
Create handlers повертають тільки `UUID` створеної сутності, а update/delete handlers повертають `void`.

Окрім auth login, оскільки це use case видачі токена, а не круд команда доменної сутності.

Також з’явилися окремі query records:

- `GetCurrentUserQuery`, `GetUserByIdQuery`, `ListUsersQuery`;
- `GetScooterByIdQuery`, `ListScootersQuery`;
- `GetRentalByIdQuery`, `ListRentalsQuery`.

Query handlers тепер приймають query objects і повертають тільки read models/views.
Наприклад, `ListScootersQueryHandler` отримує `ListScootersQuery(status)` і читає `ScooterView` через `ScooterReadRepository`.

Read side може обходити domain model, оскільки він не змінює стан системи й не перевіряє write-інваріанти.

Read models відокремлені від domain models. `UserView` перенесено в query side поруч із user queries. `ScooterView` і `RentalView` залишилися read models application layer. Вони не є JPA entities і не розкривають ORM-структуру. Наприклад, `UserView` не містить `passwordHash`, а `RentalView` повертає зручні для клієнта поля: `renterId`, `scooterId`, `startTime`, `endTime`.

Контролери стали тоншими:

- HTTP request DTO мапиться в command або query;
- write endpoint викликає command handler;
- create endpoint повертає `CreatedIdResponse`;
- update/delete endpoints повертають `204 No Content`;
- read endpoint викликає query handler і повертає view.

У результаті операції запису та читання більше не змішуються в одному service/use case.

Розділення на command side та query side зробило структуру проєкту зрозумілішою.

Операції запису проходять через domain model, factories та write repository interfaces. Завдяки цьому інваріанти залишаються на write side:

- унікальність email/code;
- коректність rental period;
- доступність скутера;
- заборона перетину періодів оренди;
- перевірка власника оренди.

## Критерії оцінювання

### Команди

Команди в application layer є окремими record-класами і описують намір змінити стан:

- `CreateUserCommand`, `UpdateUserCommand`, `DeleteUserCommand`;
- `CreateScooterCommand`, `UpdateScooterCommand`, `DeleteScooterCommand`;
- `CreateRentalCommand`, `UpdateRentalCommand`, `DeleteRentalCommand`;
- `RegisterUserCommand`.

Після рефакторингу command handlers не повертають read models. 
Create handlers повертають тільки `UUID` створеної сутності, а update/delete handlers повертають `void`.
Запис проходить через домен: `UserFactory`, `ScooterFactory`, `RentalFactory`, domain models та write repository interfaces.

Є виняток `LoginCommand`: він повертає `AuthResult`, бо login у цьому проєкті не змінює стан БД, а є auth use case для перевірки credentials і видачі JWT.

### Запити

Для операцій читання додані окремі query records:

- `GetCurrentUserQuery`, `GetUserByIdQuery`, `ListUsersQuery`;
- `GetScooterByIdQuery`, `ListScootersQuery`;
- `GetRentalByIdQuery`, `ListRentalsQuery`.

Query handlers не змінюють стан системи. Вони працюють через read repositories і повертають DTO/read models:

- `UserView`;
- `ScooterView`;
- `RentalView`.

JPA entities у відповідях не повертаються. Наприклад, `UserView` не містить `passwordHash`, а `ScooterView` і `RentalView` формуються на read side.

### Handlers

Handlers залишилися маленькими і мають одну відповідальність:

- command handler обробляє одну команду запису;
- query handler обробляє один запит читання.

Command handlers делегують бізнес-правила домену. Наприклад:

- `CreateScooterCommandHandler` створює скутер через `ScooterFactory`;
- `CreateRentalCommandHandler` створює оренду через `RentalFactory`;
- `UpdateRentalCommandHandler` перевіряє належність оренди користувачу через domain behavior `belongsTo`.

Query handlers не використовують factories і не викликають методи, які змінюють стан.

### Контролери

Контролери містять тільки HTTP mapping:

- приймають request DTO або path/query params;
- створюють command або query;
- викликають відповідний handler;
- формують HTTP response.

Контролери не працюють напряму з JPA repositories, ORM entities або domain factories.
Write endpoints повертають `CreatedIdResponse` або `204 No Content`, а read endpoints повертають read models.

### Тестування

Для команд і запитів використано різний підхід до тестування:

- command side тестується unit-тестами без Spring context, HTTP і БД;
- query side тестується integration-тестами через HTTP endpoints і test database.

Наприклад, `CreateScooterCommandHandlerTest` перевіряє command handler через fake repository.
Integration tests для `UserIntegrationTest`, `ScooterIntegrationTest`, `RentalIntegrationTest` перевіряють read endpoints, структуру JSON, фільтрацію `GET /api/scooters?status=...` і те, що внутрішні поля на кшталт `passwordHash` не повертаються.

## Аналіз: Порівняння лабораторної 2 і лабораторної 3

## Плюси CQS у цьому проєкті

### Запис став чистішим

Тепер операції читання не відповідають за формування read response.
Наприклад, створення скутера:

- controller створює `CreateScooterCommand`;
- `CreateScooterCommandHandler` створює domain model через `ScooterFactory`;
- handler зберігає скутер через `ScooterRepository`;
- handler повертає тільки `UUID`.

Тепер команда змінює стан, а не читає його назад.

### Доменні інваріанти залишилися на стороні запису

CQS не прибрав доменну модель.
Навпаки, запис се ще проходить через domain:

- `UserFactory` перевіряє email і унікальність користувача;
- `ScooterFactory` перевіряє code, model, status, battery level, price та унікальність code;
- `RentalFactory` перевіряє rental period, існування скутера, доступність скутера і overlap;
- `Rental.belongsTo(...)` використовується для перевірки власника оренди.

Це важливо, бо це захищає систему від невалідного стану.

### Читання можна розвивати окремо

Read side тепер може повертати дані у формі, зручній для клієнта, не змінюючи domain model.

Наприклад:

- `UserView` не містить `passwordHash`;
- `ScooterView` повертає `status` як string;
- `RentalView` повертає `renterId`, `scooterId`, `startTime`, `endTime`;
- `ListScootersQuery` вже підтримує фільтр `status`.

Якщо пізніше потрібно буде зробити `RentalDetailsView` з `scooterCode`, `scooterModel`, `renterEmail`, це можна буде зробити на query side без зміни domain model `Rental`.

### 4. Тести стали логічніше розділені

Command tests перевіряють поведінку запису без Spring context і без БД.
Наприклад, `CreateScooterCommandHandlerTest` перевіряє:

- створення скутера з валідними даними;
- помилку при невалідній ціні;
- помилку при дублюванні code;
- факт виклику save у fake repository.

Query tests перевіряють HTTP результат:

- структура JSON;
- фільтрація;
- відсутність зайвих internal fields;
- доступність endpoint;
- поведінка read repository mapping.

Це краще, ніж один тест, який одночасно перевіряє і бізнес-правила, і JSON response, і persistence.

### 5. Легше додавати нові use cases

Якщо потрібно додати нову write operation, наприклад `ChangeScooterStatusCommand`, можна додати:

- `ChangeScooterStatusCommand`;
- `ChangeScooterStatusCommandHandler`;
- endpoint у controller.

Не потрібно роздувати один великий `ScooterService`.

Якщо потрібно додати новий read сценарій, наприклад `GetAvailableScootersQuery`, можна додати:

- `GetAvailableScootersQuery`;
- `GetAvailableScootersQueryHandler`;
- метод у `ScooterReadRepository`;
- можливо окремий `AvailableScooterView`.

Це зменшує ризик зачепити існуючі сценарії.

### 6. Контролери стали простішими

Контролери тепер не приймають архітектурних рішень.
Вони тільки:

- отримують HTTP request;
- створюють command або query;
- викликають handler;
- повертають HTTP response.

У них немає JPA repositories, factories або ORM entities.

## Мінуси CQS у цьому проєкті

### 1. Стало дуже багато нового коду

На мою думку, для маленького CRUD проєкту це найбільший мінус.

Наприклад, для простого `GET /api/users/{id}` тепер роблю:

- `GetUserByIdQuery`;
- `GetUserByIdQueryHandler`;
- `UserReadRepository.findViewById(...)`;
- controller method;
- integration test.

Це більше boilerplate коду