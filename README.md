# em-task-manager
Необходимо разработать простую систему управления задачами (Task Management System) с использованием Java, Spring.
Система должна обеспечивать создание, редактирование, удаление и просмотр задач.
Каждая задача должна содержать заголовок, описание, статус, приоритет и комментарии,
а также автора задачи и исполнителя. Реализовать необходимо только API."

### Установка

1. Установите Docker и Docker Compose: Убедитесь, что они установлены на вашей системе. Инструкции по установке доступны на официальном сайте Docker (https://docs.docker.com/get-docker/).
2. Клонируйте репозиторий:

   ``` bash
   git clone https://github.com/Daniil-java/task.git
   cd task
   ```

### Запуск

1. Запустите контейнеры: `docker-compose up`

2. Доступ к приложению: Приложение будет доступно по адресу http://localhost:8080.

3. Проверьте статус базы данных: PostgreSQL будет доступен на порту 5433. Убедитесь, что база данных "taskmanager" создана и доступна.

4. Остановка контейнеров: `docker-compose down`

### Требования

1. Java Development Kit (JDK): Версия 17 или выше. Убедитесь, что JDK установлен и настроен в вашей системе.

2. Maven: Для управления зависимостями и сборки проекта. Убедитесь, что у вас установлена последняя версия Maven (https://maven.apache.org/install.html).

3. Docker: Для контейнеризации и развертывания приложения. Убедитесь, что у вас установлена последняя версия Docker (https://docs.docker.com/get-docker/).

4. Docker Compose: Для управления многоконтейнерными приложениями. Убедитесь, что у вас установлена последняя версия Docker Compose (https://docs.docker.com/compose/install/).

5. PostgreSQL: Используется в качестве базы данных. Если вы не используете Docker для этого, убедитесь, что PostgreSQL установлен и настроен локально.

### Конфигурация

Основные настройки приложения находятся в файле application.properties. Ниже приведены ключевые параметры:

- Серверные настройки:

  `server.port=8080`


- Настройки приложения:

  `spring.application.name=taskmanager`


- Настройки источника данных:

``` yaml
  spring.datasource.url=jdbc:postgresql://localhost:5433/taskmanager
  spring.datasource.username=postgres
  spring.datasource.password=password
  spring.datasource.driver-class-name=org.postgresql.Driver
  ```


- Настройки JPA и Hibernate:
``` yaml
  spring.jpa.database=postgresql
  spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
  spring.jpa.properties.hibernate.format_sql=true
  ```

### Использование

Документация API доступна через Swagger UI. После запуска приложения перейдите по следующему адресу, чтобы ознакомиться с доступными конечными точками:

`Swagger UI: http://localhost:8080/swagger-ui/index.html`

Swagger UI предоставляет интерактивную документацию, где вы можете не только просмотреть описание API, но и протестировать его прямо из браузера.

