# Лабораторна робота 4

Тут я додав аудит
Реалізація знаходиться в infrastructure: `LoggingAuditRecorder` пише audit через logger і `InMemoryAuditStore`.

Також з'явилися integration івенти і in-process async event bus.

## Синхронна комунікація

Синхронний варіант реалізовано для:

- `RegisterUserCommandHandler`;
- `CreateScooterCommandHandler`.

Після успішного збереження entity handler напряму викликає `AuditRecorder.record(...)`. 
Це direct call через interface, тому command handler не знає конкретну реалізацію audit component. 
Залежність передається через constructor і відповідає DIP.

Для sync audit я обрав resilience-first поведінку: якщо audit падає, основна бізнес-операція не відкочується. 
У production використовується `SafeAuditRecorder`, який ловить помилку audit, логирує її і не пробрасує exception назад у command handler. 
Тому API може повернути успішну відповідь, навіть якщо допоміжний audit component тимчасово недоступний.

Асинхронний варіант реалізовано для:

- `CreateRentalCommandHandler` через `RentalCreatedEvent`;
- `UpdateRentalCommandHandler` через `RentalUpdatedEvent`;
- `DeleteRentalCommandHandler` через `RentalDeletedEvent`.

Command handler після успішної основної операції публікує immutable integration event через `EventPublisher`. 
Він не знає, хто буде обробляти event. `AsyncInMemoryEventBus` знаходить matching subscribers і запускає їх через `Executor`. `AuditIntegrationEventHandler` є окремим subscriber і викликає audit component вже поза основним use case.

## Порівняння

Synchronous communication використовувати простіше. API чекає виконання audit component, тому response time дорівнює часу основної операції плюс часу audit. 
Якщо audit повільний, endpoint також стає повільнішим. При збоях потрібно явно вирішити, чи ламати основну операцію. 

Asynchronous communication слабше зв'язує компоненти. Command handler знає тільки про `EventPublisher`, а не про конкретний subscriber. 
API публікує event і завершує основний юзкейс, тому audit може з'явитися пізніше. 
Це дає eventual consistency бо основна операція вже виконана, а побічний ефект може бути оброблений окремо. 
Реалізація складніша, бо потрібні events, event bus, subscribers і тести доставки

У sync сценаріях збій audit component не скасовує реєстрацію користувача або створення самоката. Це зроблено через `SafeAuditRecorder`.

В async сценаріях помилка subscriber не впливає на command handler. `AsyncInMemoryEventBus` логирує помилку конкретного handler і не пробрасує її в publisher. 
Інші subscribers можуть продовжити роботу.

## Мій вибір

Для audit, notifications і analytics у scooter-rental я вибрав asynchronous communication. 
Ці операції не повинні сповільнювати бронювання або адміністративні дії, і async дозволяє додавати нових subscribers без зміни command handlers.
Synchronous communication я б залишив для операцій, результат яких потрібен прямо зараз у тому самому юзкейсі як наприклад, валідація платежу перед підтвердженням початку оренди або перевірка доступу

**Розділення компонентів та архітектурна чистота**: 

Допоміжні компоненти винесені в окремі модулі в пакетах `application/audit` та `application/event`. 
Вони не вбудовані намертво в бізнес-логіку. Handlers взаємодіють з ними виключно через абстракції, що дозволяє легко замінювати реалізацію наприклад, перейти з `InMemory` на зовнішню чергу повідомлень без зміни коду бізнес логіки.

**Чіткі контракти та незмінність даних**
Для всіх взаємодій визначено контракти (інтерфейси `AuditRecorder`, `EventPublisher`). Повідомлення (`AuditMessage`) та події (`IntegrationEvent`) реалізовані як Java Records, що гарантує їхню імутабельність. 
Це запобігає побічним ефектам, коли обробник події міг би випадково змінити дані події для інших підписників.

**Синхронна комунікація та стійкість**: На прикладі `RegisterUserCommandHandler` показано синхронний підхід. Для забезпечення надійності використано декоратор `SafeAuditRecorder`. Він гарантує, що навіть якщо сервіс аудиту тимчасово недоступний або працює зі збоями, реєстрація користувача буде успішно завершена.

**Асинхронна комунікація та Event-Driven підхід**: Реалізував через `AsyncInMemoryEventBus`. У сценаріях оренди (`CreateRental`) основна операція лише публікує подію та одразу повертає відповідь користувачу. Сама ж обробка події відбувається в іншому потоці виконання.

**Якість подій**: Всі події іменовані в минулому часі (`RentalCreatedEvent`, `UserRegisteredEvent`), що підкреслює факт того, що дія вже відбулася. Події є самодостатніми та містять необхідний контекст: ідентифікатори об'єктів, дані про актора та точний час.

**Тестування**: Код повністю покритий тестами. `RegisterUserCommandHandlerAuditTest` перевіряє не лише успішний аудит, а й сценарій збою аудиту (через `SafeAuditRecorder`). `RentalEventPublishingTest` підтверджує, що події публікуються тільки після успішного збереження даних у базу та не публікуються при помилках валідації.

## Висновок

У цій лабораторній обидва підходи показані на різних сценаріях без дублювання аудиту для однієї і тієї ж операції.
Synchronous communication простіша і легше тестується через моки або фейковий `AuditRecorder`.
Asynchronous communication складніша, але зменшує зв'язаність, покращує час відповіді апі і дозволяє незалежно розширювати систему.


