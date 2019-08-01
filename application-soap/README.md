# ADK Модуль "application-soap-client"

## Назначение модуля
Данный модуль предоставляет функционал HTTP SOAP клиента и HTTP SOAP прокси.
В некотором роде является оберткой над `application-http-client`.
Ключевые компоненты:

1. `SoapClientProxy`
2. `SoapClientProxyBuilder`
3. `SoapClientProxyConfig`

Первый представляет собой "прослойку" между бизнес логикой приложения и HTTP SOAP клиентами.
Прокси имеет два основных вызова: синхронный и асинхронный.
В обоих вариантах прокси получается объект `SoapClientProxyConfig`, который содержит всю необходимую информацию, полученную при конфигурации прокси через `SoapClientProxyBuilder`.
Исходя и полученного конфига, прокси выполняет ряд следующих операций:
1. Задает интерцепторы для `httpClientProxy`
2. Конфигурирует и вызывает `httpClientProxy`:
```
httpClientProxy
                .syncClient(proxyConfig.getSyncHttpClient())
                .version(proxyConfig.getHttpVersion())
                .requestCharset(proxyConfig.getRequestCharset())
                .withHeader(ACCEPT, proxyConfig.getConsumesMimeType().getMimeToContentTypeMapper().getMimeType().toString())
                .withHeader(CONTENT_TYPE, proxyConfig.getProducesMimeType().getMimeToContentTypeMapper().getMimeType().toString())
                .responseMapper(soapResponseToModel(proxyConfig))
                .config(proxyConfig.getRequestConfig())
                .consumes(proxyConfig.getConsumesMimeType().getMimeToContentTypeMapper())
                .post()
                .requestEncoding(proxyConfig.getRequestBodyEncoding())
                .produces(proxyConfig.getProducesMimeType().getMimeToContentTypeMapper())
                .requestMapper(soapRequestFromModel(proxyConfig))
                .withBody(request)
                .prepare()
                .execute();
```


Что из себя представляет `SoapClientProxyBuilder` проще всего увидеть на примере использования:
```
SoapClientProxyBuilder.<GetSaldoRequest, GetSaldoResponse>soapClientProxy(crmAsrAdapterModule().getBillingInfoServiceUrl())
                    .operationId(GET_SALDO_OPERATION)
                    .operationNamespace(BILLING_SCHEME_PREFIX, BILLING_SCHEME_URL)
                    .requestMapper(getSaldoSoapRequestFromModelMapper(crmAsrAdapterModule().getSystemId(), crmAsrAdapterModule().getAffiliateId()))
                    .responseMapper(getSaldoSoapResponseToModelMapper)
                    .prepare()
                    .execute(request);
``` 
На примере выше выполняется следующее "построение" параметров SOAP прокси:
1. За SOAP операцию считается значение `operationId`. В нашем случае это - `GET_SALDO_OPERATION`
2. За XML Namespace SOAP операции считается значение `operationNamespace`
3. Запросы будем отправлять по адресу `crmAsrAdapterModule().getBillingInfoServiceUrl()`
5. В качестве requestMapper-а (выполняет преобразование `request -> Entity`) считаем объект `getSaldoSoapResponseToModelMapper`
6. Запрос будет синхронным

В момент вызова `execute` будет отправлен запрос по адресу `getBillingInfoServiceUrl()` с XML телом запроса, сформировавшемся из request с помощью requestMapper-а.
Стоит заметить, что для построения прокси не указывается ряд параметров, которые требует `httpClientProxy`.
Засчёт того, что SOAP запросы имеют конкретный тип данных (XML) и тип HTTP запроса (POST), эти параметры задаются на уровне `SoapClientProxy`.

Важный момент: `execute` возвращает объект `Optional`. Чтобы получить из него респонс имеется следующие конструкции:

* `response = optional.orElse(DEFAULT_RESPONSE)`
* `if (optional.isPresent()) { response = optional.get() }`
* `optional.ifPresent(response -> {})`
  
Отличительными параметрами у `SoapClientProxyBuilder` можно отметить:
```
envelopeNamespace(String prefix, String namespace) - отвечает за XML Namespace обязательного тега Envelope 

bodyNamespace(String prefix, String namespace) - отвечает за XML Namespace обязательного тега Body

operationNamespace(String prefix, String namespace) - отвечает за XML Namespace обязательного тега операции (из operationId)
```
  
## Основная функция модуля:
Отправка запросов по HTTP SOAP протоколу.
Является платформой для SOAP клиента. (Модуль application-soap-client)

## Особености конфигурации модуля
Отдельно в конфигурации стоит отметить следующие параметры:
```
    String getEnvelopePrefix();
    String getEnvelopeNamespace();
    String getBodyPrefix();
    String getBodyNamespace();
```
Они представляют собой XML Namespace-ы по умолчанию, в случае если они не будут заданы в `SoapClientProxyBuilder`

[Пример работы с Apache HTTP Client](http://www.vogella.com/tutorials/ApacheHttpClient/article.html)

[Optional Шрёдингера](https://habr.com/post/346782/)

![XML Namespaces](xml.png)