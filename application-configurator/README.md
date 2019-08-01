Данный проект предназначен для управления конфигурациями модулей системы на базе ADK (application development kit).

Абстрактный интерфейс модуля выглядит следующим образом:

```
configurator {
    load(configuration)
    get(applicationId/clusterId/moduleId/serviceId, version)
    apply(applicationId)
    apply(applicationId, clusterId)
    apply(applicationId, clusterId, moduleId...)
    rollback(applicationId)
    rollback(applicationId, clusterId)
    rollback(applicationId, clusterId, moduleId...)
}

configuration = applicationId -> clusterId -> moduleId -> serviceId
```

Модуль реализует следующие операции:

1. Загрузка конфигурации (по HTTP в формате YAML)
2. Получение конфигурации по разным путям (айди приложения/айди кластера/ айд модуля/ айд сервиса
3. Активации конфигурации для приложения, кластера, модулей
4. Откат конфигурации для приложения, кластера, модулей

Модуль должен поддерживать следующие аспекты:

1. Хранение конфигурации в БД (rocksDB)
2. Версионирование конфигураций