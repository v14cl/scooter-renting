# Лабораторна робота 5: Модульний моноліт

## Аналіз лабораторної 5

### Як я визначив межі контекстів

Я залишив основну бізнес-логіку в `core`. До Core належать users, scooters,
rentals, auth/security, command handlers, query handlers, domain models,
factories, repositories, read repositories, persistence adapters і controllers
основного REST API. 

Core відповідає за бізнес-операції. Створення користувача, керування скутерами, створення та оновлення оренд,
перевірку прав доступу і доменні інваріанти.

Окремо я виділив `analytics`. Це read-only consumer, який не змінює Core і не
бере участі в основних бізнес-транзакціях. Analytics будує dashboard projection
на основі Core events: кількість зареєстрованих користувачів, створених
скутерів, створених/активних/завершених/видалених оренд, rentals by day,
scooters by status і last activity time.

Analytics має іншу відповідальність, інші моделі і
інші вимоги до consistency.

Я розглядав альтернативу виділити Users, Rentals і Scooters як окремі модулі, але це створило б надто багато зв'язків
Core і Analytics краще показує різницю між основним
доменом і контекстом без надмірного дроблення.

### ACL

Public contract Core тепер лежить у `core.api.event`.

Технічний контракт Event Bus виніс у `shared.event` `IntegrationEvent`,
`EventPublisher` і `EventHandler`. Core публікує events через `EventPublisher`,
а Analytics підписується через `EventHandler`.

Analytics не імпортує `core.domain`, `core.infrastructure`, `core.application`
або persistence entities. Замість цього він має layer у
`analytics.acl`. Translators приймають Core public events і перетворюють їх у
внутрішні analytics models:

- `UserRegistrationActivity`;
- `ScooterActivity`;
- `RentalActivity`.

### Strong consistency та eventual consistency

Strong consistency використовується всередині Core. Наприклад, створення Rental
проходить через `RentalFactory`, перевіряє період оренди, доступність скутера і
overlap rules, а потім зберігається через Core repository. Перевірка прав доступу
для update/delete rental також залишається в Core command handlers. Domain
invariants перевіряються до збереження, а repository save і domain model
належать одному модулю.

Eventual consistency використовується між Core і Analytics. Після успішної
Core-операції handler публікує event: `UserRegisteredEvent`,
`ScooterCreatedEvent`, `RentalCreatedEvent`, `RentalUpdatedEvent` або
`RentalDeletedEvent`. Event Bus доставляє події асинхронно. Analytics subscribers
отримують events, ACL перекладає їх у internal activities, і projection
оновлюється окремо.

### Еволюція від lab1 до lab5

Lab1 була базовою реалізацією use cases. Логіка була більш лінійною, а розділення
відповідальностей було мінімальним.


- відповідальності були розділені недостатньо чітко;
- бізнес-логіка могла змішуватися з persistence або HTTP-логікою;
- складніше було зрозуміти, де саме знаходяться бізнес-правила;
- при збільшенні кількості use cases service-класи могли швидко розростатися;

У Lab2 з'явилися layers. Domain було відокремлено від ORM, з'явилися value
objects, factories, repositories і mappers. Бізнес-логіка стала ближче до domain,
а persistence перестав бути центром моделі.

domain став незалежним від Spring, JPA, HTTP та бази даних;
- бізнес-правила стало легше знаходити, бо вони розміщені ближче до domain models;
- value objects почали захищати прості інваріанти, наприклад email, rental period або price per minute;
- factories стали відповідати за створення валідних domain objects;
- domain unit tests можна запускати без БД і без Spring context;
- стало простіше потенційно замінити persistence layer, бо domain не залежить від ORM;

але з’явилася потреба створювати мапери між ORM entities і domain models і мені навігація по проєкту стала сильно складнішою

У Lab3 було впроваджено CQS. Write operations стали commands, read operations
стали queries, а read models були відокремлені від domain models. Це зробило API
зрозумілішим і зменшило ризик випадково використовувати domain objects як DTO.

- стало чітко видно, які операції змінюють стан, а які тільки читають дані;
- command handlers стали маленькими й відповідальними за одну операцію;
- query хендлери стали відповідальними лише за читання;


але для кожної операції потрібно створювати окремий command/query і handler і з’явилося більше boilerplate коду;

У Lab4 були виділені побічні операції. З'явилися sync та async communication,
integration events, event handlers і audit/activity logging. Це дозволило
прибрати частину побічної логіки з основних use cases.

- побічна логіка перестала засмічувати основні хендлери команд;
- audit отримав окремий контракт і власну відповідальність;
- з’явилися integration events, які описують факти, що вже відбулися;
- command handler більше не повинен знати конкретних підписників;

но тут мені стало складніше тестувати асинхронні процеси і дебажити їх

У Lab5 система стала modular monolith. З'явилися bounded contexts `core` і
`analytics`, public contracts, shared event contracts, ACL і окрема analytics
projection. Міжмодульна комунікація йде через events, а не через internal classes.

- система перестала бути одним великим контекстом;
- `core` і `analytics` отримали різні відповідальності;
- у майбутньому Analytics буде простіше винести в окремий сервіс, бо межі вже визначені.

але тепер потрібно стежити, щоб модулі не імпортували зовнішні classes один одного

### Висновок

Найціннішими рішеннями в цьому проєкті я вважаю відокремлення домену від інфраструктури, впровадження CQS, використання подій для побічних операцій, 
public contracts між модулями та ACL для захисту одного модуля від моделей іншого.

Відокремлення domain від infrastructure допомогло не змішувати бізнес-правила з JPA, базою даних і технічними деталями. 
Завдяки цьому доменні моделі стали відповідати саме за правила системи, а не за те, як дані зберігаються в БД. 
CQS зробив структуру application layer зрозумілішою: команди відповідають за зміну стану, а запити — тільки за читання. 
Це допомогло краще розділити логіку запису і логіку отримання даних.

Також треба було набагато раніше продумати межі модулів. 
Я б швидше виніс public events в окреме місце, бо події, які використовуються між модулями, не повинні виглядати як внутрішні класи application layer. 
Також я б одразу уважніше розділяв DTO, domain models і persistence entities. 
Це зменшило б кількість рефакторингу на наступних етапах, коли проєкт переходив від простої шарової архітектури до модульного моноліту.

Головний компроміс у цьому проєкті — це баланс між простотою та гнучкістю.
Для маленького проєкту це може виглядати надлишково. Але перевага в тому, що систему стає легше тестувати, змінювати та розширювати. 
У цьому проєкті моноліт дав практичний плюс: Analytics можна розвивати окремо від Core. При цьому основні бізнес-операції не залежать від аналітики, а доменні інваріанти Core залишаються захищеними.