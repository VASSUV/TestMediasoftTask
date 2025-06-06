# Backend Development Notes

## 🔧 Что ещё нужно
- параметры включения шедулеров сделать не по enable, а по enum
- Вынести проекции в отдельный пакет, и переименовать
- Вынести `enum` как `*Type`
- Попробовать параметризованные тесты
- Разобраться с `PessimisticLock`, `OptimisticLock`, `ExclusiveLock`
- В тестах генерацию данных делать через `.copy(...)` и мок-функции
- Проверить корректность результата `Page`
- Посчитать количество SQL-запросов при вызове `delete` в Hibernate
- Spring batch framework
- Какие требования к Entity? (id, final, no constructors)
- query DSL
- DataJpaTest
- Audit, Hibernate Envers

---

## 📚 Что почитать
- Евгений Борисов — «Spring-потрошитель»
- Hibernate Sessions и транзакции
- `@PersistenceContext` — механизм и применение
- JPA vs Hibernate
- `EntityManager` и `Session` — различия
- Основы JDBC
- `TransactionPropagation` — типы и примеры
- Уровни изоляции транзакций
- Кэширование в Hibernate (1st, 2nd, 3rd level)
- Разница `mock` vs `spy` (Mockito/MockK)
- SpEL (Spring Expression Language) — для `@Value` и `@ConfigurationProperties`
- Hibernate `@Version` — аудит записей
- Захват блокировок `databasechangeloglock` в Liquibase
- Конфигурация `cron` для `@Scheduled`
- Dispatcher servlet, Container Servlet, TomCat server  
- filter interceptors
- четкая картина пути запроса от начала до ответа
- Как устроен стартер, 
- @ShedulerLock
- Session scope bean
- executeBatch() - можно обработать результат и обработать исключения, и понять запись в которой была ошибка
- добавить тест на проверку количества запросов в бд, на операцию изменения заказа

---

## 💡 Что можно добавить

### Теория
- Работа Hibernate под капотом: dirty checking, flush, lifecycle
- Паттерны: `UnitOfWork`, Repository, DAO
- Проблемы `LazyInitializationException` и их решение
- Анализ `show_sql`, `format_sql` и логгирование

### Практика
- Профилировка запросов через VisualVM
- Логгирование SQL-запросов + время выполнения
- Интеграционные тесты на `@Transactional` и rollback
- AOP для логгирования времени методов
- AOP-логгирование вызовов репозиториев и сервисов
- Настройка H2 + Flyway в тестах
- rollbackFor для транзакций
- isolation = Isolation.REPEATABLE_READ для транзакций
- поставить @CircuitBreaker(name = "currencyService", fallbackMethod = "fallbackCurrency")
- Использование expireAfterAccess для сессии. Сейчас валюта хранится в сессии без явной очистки после неактивности:
  Рекомендация: Если валюта должна сбрасываться на дефолтную после 1 минуты неактивности, лучше явно использовать механизм очистки:
  •	Добавить явный метод для очистки сессии по таймеру или использовать Servlet-API для управления сессиями:

Пример реализации простого механизма очистки через Servlet:
---

## ✅ Что сделано
- [x] Перенёс конфигурации из `.properties` в `.yaml`
- [x] Установлены: DbViewer, VisualVM, Docker, Postman
- [x] SQL-миграции перенесены в Liquibase
- [x] Разложил миграции по папкам
- [x] Реализованы `rollback`-миграции
- [x] Добавлены проверки существования таблиц в миграциях
- [x] Используются `Precondition` для колонок и таблиц
- [x] Присутствуют таблицы `databasechangelog`, `databasechangeloglock`
- [x] Добавлен `TestProfile` в YAML
- [x] Общая модель `ErrorDetails` для ошибок
- [x] Реализован `DefaultExceptionHandler`
- [x] Использование HTTP-кодов через аннотации (`@ResponseStatus`)
- [x] Вынесены конфиги и проперти-файлы выше
- [x] Разделение на DBO / Entity / Persist слои
- [x] Репозитории структурированы
- [x] Контроллер без бизнес-логики, исключения на сервис
- [x] Удалён `ResponseEntity` (требуется дополнительная проверка)
- [x] Переименование моделей
- [x] `@Transactional(readOnly = true)` используется
- [x] Kotlin-специфики учтены
- [x] Интерфейсы и реализации разделены
- [x] `Pageable` в контроллерах
- [x] `Update` и `Create` возвращают `UUID`
- [x] Планировщик через JDBC
- [x] Оптимизирован доступ к БД
- [x] многокритериальный поиск
- [x] полиморфная десериализация
