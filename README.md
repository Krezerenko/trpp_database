# trpp_database
Проект по ТРПП 4 семестра обучения.

## Установка
Перед сборкой проекта необходимо создать файл .env и задать в нем инициалы для входа:
- POSTGRES_USER=имя_пользователя_базы_данных
- POSTGRES_PASSWORD=пароль_пользователя_базы_данных
- POSTGRES_DB=название_базы_данных
- SPRING_USER=имя_пользователя_spring
- SPRING_PASSWORD=пароль_пользователя_spring

Также убедитесь что докер демон запущен.

Для сброки проекта требуется перейти в папку с проектом и вызвать команду:
```shell
./gradlew build
```

## Запуск
Для запуска проекта запустите задачу dockerStart:
```shell
./gradlew dockerStart
```
Если нужно просматривать логи в консоли, запустите команду:
```shell
docker compose up
```

## Использование
Сервер отвечает на различные REST API запросы:
- GET localhost:8080/api/users - Возвращает данные всех пользователей в базе
- GET localhost:8080/api/users/{id} - Возвращает данные пользователя по id
- POST localhost:8080/api/users/register - Добавляет пользователя с переданными данными
- POST localhost:8080/api/users/login/name - Авторизует пользователя по имени
- POST localhost:8080/api/users/login/email - Авторизует пользователя по почте
