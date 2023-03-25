## Подготовка к запуску

* Для запуска необходима Java 17
* Настройки БД осуществляются в `server/src/main/resources/application.yml`. Секция `spring.datasource`.
* Вместо MySQL можно использовать H2, указав `url: jdbc:h2:file:./test`

## Запуск клиента:

`gradlew :client:run -q --console=plain`

## Запуск сервера:

`gradlew :server:bootRun -q --console=plain`

### Альтернативный вариант:

Запуск через IDEA посредством запуска главного класса приложения

### Windows

При запуске из винды возможны проблемы с кодировкой русских символов. В cmd решается выполнением `chcp 65001` перед запуском. Либо можно выполнить сборку и запустить бинарник командой `java -jar файл`

## Сборка

### Сборка клиента

`gradlew :client:shadowJar`

Программа появится в client/build/libs

### Сборка сервера

`gradlew :server:bootJar`

Программа появится в server/build/libs
