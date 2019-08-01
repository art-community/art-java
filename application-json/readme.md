# ADK Модуль "application-json"

## Назначение модуля
Данный модуль можно охарактеризовать двумя выражениями:
* JSON парсер
* JSON сериализатор

Именно данные две функции выполняют основные классы модуля:

`JsonEntityReader` и `JsonEntityWriter` 

Первый выполняет преобразование `Json -> Value`.
Второй: `Value -> Json`.

Для парсинга JSON используется Jackson JsonParser.
Для генерации JSON используется Jackson JsonGenerator.

## Основная функция модуля:
Парсинг и генерация JSON в/из Value.

## Особености конфигурации модуля
Конфигурация позволяет переопределить Jackson ObjectMapper.

[Почитать про Jackson и не только](https://habr.com/company/luxoft/blog/280782/)

[Почитать только про Jackson](https://github.com/FasterXML/jackson)

