# Backend Development Notes

## 🔧 Что ещё нужно
- Вынести `enum` как `*Type`
- Разобраться с `PessimisticLock`, `OptimisticLock`, `ExclusiveLock`
- В тестах генерацию данных делать через `.copy(...)` и мок-функции
- Проверить корректность результата `Page`
- Посчитать количество SQL-запросов при вызове `delete` в Hibernate

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
