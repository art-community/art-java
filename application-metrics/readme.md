# ADK Модуль "application-metrics"

## Назначение модуля
Данный модуль представляет собой агрегатор различных Metrics API.
В его состав входят:
* [Prometheus](https://habr.com/company/selectel/blog/275803/)
* [Dropwizard](https://www.dropwizard.io/1.3.5/docs/manual/index.html)
* [Micrometer](https://micrometer.io/docs/registry/prometheus)

## Ключевые компоненты:
`MetricServiceSpecification` - спецификация сервиса метрик. Позволяет выставить HTTP сервис, который выдаёт метрики из общих MetricsRegistry.

## Основная функция модуля:
Концептуальная обертка в виде модуля над сборщиком метрик

## Особености конфигурации модуля
    Set<MeterBinder> getMeterBinders(); - источник метрик. Для работы с MeterBinder рекомендую ознакомиться с документацией Micrometer-а

    String getManagementHttpPath(); - URL context_path сервиса метрик. Используется в качестве параметра для ..serve()

    PrometheusMeterRegistry getPrometheusMeterRegistry(); - реестр метрик для прометеуса. Как правило не переопределяется