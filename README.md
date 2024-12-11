# ExcelWorker

## Описание
Это приложение предназначено для анализа успеваемости студентов на основе их оценок. Оно предоставляет графический интерфейс для загрузки данных из Excel-файлов, отображения их в таблице, построения столбчатых диаграмм для анализа и генерации отчетов в Excel.

### Функционал:
- Загрузка списка студентов и их оценок из Excel-файлов.
- Отображение данных в таблице.
- Столбчатая диаграмма для статистики оценок.
- Генерация отчетов, сохраненных в Excel-файле.
- Логирование событий приложения с использованием log4j.

## Требования
- Java 17 и выше.
- JavaFX SDK 17.0.13.

## Структура проекта
- StudentAnalysisApp - основной класс приложения, который отвечает за взаимодействие с пользователем, загрузку данных, генерацию отчетов и отображение статистики.
- StudentParser - утилита для парсинга данных из Excel-файла.
- ResultWriter - утилита для записи отчета в Excel.
- Student - модель данных, представляющая студента с его именем и оценкой.

## Установка и запуск

### 1. Сборка проекта:
Для сборки проекта используйте Gradle. В терминале выполните команду:
gradle build
### 2. Запуск приложения:
После сборки, для запуска приложения выполните команду:
java --module-path .\libs\javafx-sdk-17.0.13\lib\ --add-modules javafx.controls,javafx.fxml -jar .\build\libs\excel-1.0-SNAPSHOT.jar
### 3. Генерация отчета:
После загрузки данных, можно сгенерировать отчет, который будет сохранен в файл analysis_results.xlsx.
