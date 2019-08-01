# ADK Модуль "application-http"

## Назначение модуля
Данный модуль является хранилищем основных классов и констант для работы HTTP сервера и клиента.
Модуль хранит общую конфигурацию для модулей "application-http-server" и "application-http-client".
Отличительными классами модуля можно назвать `HttpContentMapper` и его вложенные интерфейсы: `HttpEntityToContentMapper` и `HttpContentToValueMapper`.

Основное предназначение данных классов: преобразования двух видов:
* `Массив байт -> Value`
* `Value -> Массив байт`

При конфигурировании "application-http-server" или "application-http-client" нужно указывать классы-преобразователи для всех требуемых `MimeType`-ов. Пример конфигурирования мапперов можно посмотреть в "application-configurator.ConfiguratorHttpContentMapping" 

## Основная функция модуля:
Хранение общих компонентов для "application-http-server" и "application-http-client" модулей

## Особености конфигурации модуля
Конфигурация модуля имеет следующий вид:
```
    boolean isEnableTracing(); - включение/выключение трассировки запросов
    (требует наличие LoggingModule)

    boolean isEnableMetricsMonitoring(); - включение/выключение мониторинга HTTP метрик 
    (требует наличие MetricsModule) 

    Map<MimeType, HttpContentMapper> getContentMappers(); - HTTP мапперы (см. выше)

    Logbook getLogbook();
```
Logbook - библиотека для HTTP логирования.

[Почитать про Logbook](https://github.com/zalando/logbook)

