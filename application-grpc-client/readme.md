# ADK Модуль "application-grpc-client"

## Назначение модуля
Данный модуль предоставляет функционал protobuf клиента и protobuf прокси.
В некотором роде является оберткой над protobuf Sync и Async клиентами.
Ключевые компоненты:

1. `ProtobufAsyncProxy`
2. `ProtobufAsyncClient`
3. `ProtobufClientProxyBuilderImplementation`
4. `ProtobufClientProxyConfig`
5. `ProtobufSyncClient`
6. `ProtobufSyncProxy`
7. `ProtobufClientProxyBuilder`
8. 2 хендлера: `ProtobufAsyncClientCompletionHandler, ProtobufAsyncClientErrorHandler`

Прокси представляет собой "прослойку" между бизнес логикой приложения и Protobuf клиентами.
Он имеет два основных вызова: синхронный и асинхронный.
В обоих вариантах прокси получается объект `ProtobufClientProxyConfig`, который содержит всю необходимую информацию, полученную при конфигурации прокси через `ProtobufClientProxyBuilder`.
Исходя из полученного конфига, прокси выполняет ряд следующих операций:
1. Прогоняет интерцепторы на запрос
2. Вызывает Protobuf (Async) Client. В клиенте вызывается GRPC API "заглушки", сгенерированные в модуле application-protobuf-generated в виде 
`ProtobufServletBlockingStub` или (Async) `ProtobufServletFutureStub`
3. Обрабатывает полученный респонс (в случае с асинхронным клиентом обработка осуществляется через хендлеры (см. выше))
4. Далее происходит процесс двойного маппинга:
* `Массив байт (контент респонса) -> Value с помощью ProtobufEntityReader`
* `Value в POJO с помощью ValueToModelMapper`
6. Полученный POJO возвращается из прокси

Что из себя представляет `ProtobufClientProxyBuilder` проще всего увидеть на примере использования:
```
            ProtobufClientProxyBuilder.<ModuleKey, ApplicationConfiguration>protobufClientProxy()
                    .syncClient(new ProtobufSyncClient(ProtobufClientConfig.builder()
                            .host(getHost())
                            .port(getPort())
                            .path(getClientServletPath()).build()))
                    .serviceId(CONFIGURATOR_SERVICE_ID)
                    .methodId(GET_PROTOBUF_CONFIG)
                    .requestMapper(moduleKeyMapper.getFromModel())
                    .responseMapper(applicationConfigurationMapper.getToModel())
                    .prepare()
                    .execute(ModuleKey.builder().build());
``` 
На примере выше выполняется следующее "построение" параметров Protobuf прокси:
1. В качестве запроса считаем объект типа ModuleKey. Ответ игнориурем, но считаем, что он - ApplicationConfiguration
2. Запросы будем отправлять по адресу `getHost():getPort()/getClientServletPath()`
3. path - путь к сервлету вызываемого модуля. Каждый Protobuf server модуль имеет хост, порт, на котором поднимается сокет для
прослушивания запросов. Дополнительно, к паре хост:порт добавляется строкове наименования модуля в сети. Своего рода, сетевой Protobuf идентификатор модуля. По данному идентификаторму балансировщик, например, определяет, в какой модуль отправить запрос. Аналоги идентификатора - HTTP URL.  
4. В качестве requestMapper-а (выполняет преобразование `moduleKey -> Entity`) считаем объект `moduleKeyMapper.getFromModel()`
5. В качестве responseMapper-а (выполняет преобразование `Entity -> applicationConfiguration`) считаем объект `applicationConfigurationMapper.getToModel()`
6. При вызове запроса будет вызван сервис CONFIGURATOR_SERVICE_ID и метод GET_PROTOBUF_CONFIG. 
Данный функционал говорит о том, что при вызове сервлета модуля на серверной стороне будет вызыван конкретный метод у сервиса, и в метод будет передан moduleKey в качестве параметра. Благодаря данному принципу работы мы можем на одном сервлет поднят множество сервисов, и обрабатывать один запрос разной логикой. 
7. Запрос будет синхронным

Важный момент: `execute` возвращает объект `Optional`. Чтобы получить из него респонс имеется следующие конструкции:

* `response = optional.orElse(DEFAULT_RESPONSE)`
* `if (optional.isPresent()) { response = optional.get() }`
* `optional.ifPresent(response -> {})`
  

## Основная функция модуля:
Отправка запросов по Protobuf протоколу.

## Особености конфигурации модуля
Отдельно в конфигурации стоит отметить список интерцепторов:
```
    List<ClientInterceptor> getInterceptors();

```
Основная задача интерцептора - выполнить некую логику ДО или ПОСЛЕ запроса.

Соответственно, `ClientInterceptor` позволяет "перехватывать" protobuf вызов и оборачивать его в некую логику пред и пост обработки.

[Protobuf](https://developers.google.com/protocol-buffers/)

[GRPC](https://grpc.io/)

[Optional Шрёдингера](https://habr.com/post/346782/)