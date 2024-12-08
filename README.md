# Инструкция по запуску TODO-приложения и автотестов

## Запуск приложения через Docker

1. Загрузите Docker-образ:
   ```bash
   docker load < todo-app.tar

2. Запустите приложение:

   ```bash
   docker run -p 8080:4242 todo-app

После запуска приложение будет доступно по адресу: http://localhost:8080.



# Запуск автотестов через Gradle и Allure

1. Выполните автотесты с помощью Gradle:
   ```bash
   ./gradlew clean test

2. Создайте отчет в Allure:

   ```bash
   allure serve build/allure-results
