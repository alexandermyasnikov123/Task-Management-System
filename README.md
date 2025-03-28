### Task Managing System

#### Техническое задание
---
Вам необходимо разработать простую систему управления задачами (Task Management System) с использованием Java, Spring.
Система должна обеспечивать создание, редактирование, удаление и просмотр задач. Каждая задача должна содержать заголовок, описание, статус (например, "в ожидании", "в процессе", "завершено"), приоритет (например, "высокий", "средний", "низкий") и комментарии, а также автора задачи и исполнителя.
Реализовать необходимо только API.

---
#### Как запустить локально на своём компьютере?

1. Убедитесь что у вас установлен и функционирует [Docker engine](https://docs.docker.com/engine/install/)
2. Загрузите и распакуйте в любое удобное место [архив](https://github.com/alexandermyasnikov123/Task-Management-System/archive/refs/heads/main.zip) с исходным кодом или склонируйте этот репозиторий командой `git clone https://github.com/alexandermyasnikov123/Task-Management-System`
3. Перейдите в папку с исходным кодом проекта и переименуйте файл [example.env](example.env) в `.env`
4. Откройте в любом удобном текстовом редакторе переименованный [.env](.env) и следуя инструкциям замените значения `<PLACEHOLDERS>` на фактические.</br>
Например вот так будет выглядеть переменная окружения, отвечающая за порт вашего сервиса `APPLICATION_PORT=8080`
5. Запуск приложения:
    - В контейнере без дополнительной настройки зависимых сервисов:
        - Запустите команду `docker compose up` в терминале Unix
        - Ожидайте появления строки со следующим содержимым `Started TaskManagingApplication in X.XXXX seconds (process running for X.XXX)`
    - Без использования контейнеров и дополнительной настройкой (***не рекомендуется***):
      - Установите и запустите [PostgreSQL](https://www.postgresql.org/download/) версии 17.4 и выше
      - Создайте базу данных, пользователя и пароль в соответствии с таковыми в упомянутом выше [.env](.env)
      - Выполните команду `chmod +x mvnw && ./mvnw clean install && java -jar target/task-managing-system.jar`
6. Ваш сервис запущен и готов принимать запросы по соответствующим ручкам. Поздравляю, можете ознакомиться с OpenApi документацией по endpoint'у `/api/v1/swagger`
