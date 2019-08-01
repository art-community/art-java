# ADK Модуль "application-http-client"

## Назначение модуля
Данный модуль предоставляет функционал HTTP клиента и HTTP прокси.
В некотором роде является оберткой над Apache HTTP Sync и Async клиентами.
Ключевые компоненты:

1. `HttpClientProxy`
2. `HttpClientProxyBuilder`
3. `HttpClientProxyConfig`
4. 3 хендлера: `HttpAsyncClientCancellationHandler, HttpAsyncClientExceptionHandler, HttpAsyncClientResponseHandler`

Первый представляет собой "прослойку" между бизнес логикой приложения и HTTP клиентами.
Прокси имеет два основных вызова: синхронный и асинхронный.
В обоих вариантах прокси получается объект `HttpClientProxyConfig`, который содержит всю необходимую информацию, полученную при конфигурации прокси через `HttpClientProxyBuilder`.
Исходя и полученного конфига, прокси выполняет ряд следующих операций:
1. Прогоняет интерцепторы на запрос
2. Вызывает Apache HTTP (Async) Client
3. Обрабатывает полученный респонс (в случае с асинхронным клиентом обработка осуществляется через хендлеры (см. выше))
4. В процессе обработки запросы прокси на основе `MimeType` определяет тип данных в ответе и сопоставляет с ним объект `HttpContentMapper`-а
5. Далее происходит процесс двойного маппинга:
* `Массив байт (контент респонса) -> Value с помощью HttpContentMapper`
* `Value в POJO с помощью ValueToModelMapper`
6. Полученный POJO возвращается из прокси

Что из себя представляет `HttpClientProxyBuilder` проще всего увидеть на примере использования:
```
httpClientProxy<String, Object>(request.url)
                .post()
                .produces(APPLICATION_JSON_UTF8)
                .requestMapper(mapper)
                .withBody(bodyString)
                .prepare()
                .async()
                .executeAsync()
``` 
На примере выше выполняется следующее "построение" параметров HTTP прокси:
1. В качестве запроса считаем объект bodyString типа String. Ответ игнориурем, но считаем, что он - Object
2. Запросы будем отправлять по адресу `request.url`
3. Тип HTTP запроса - POST
4. Тип данных HTTP запроса - application/json;charset=utf-8
5. В качестве requestMapper-а (выполняет преобразование `bodyString -> Entity`) считаем объект `mapper`
6. Запрос будет асинхронным

В момент вызова `executeAsync` будет асинхронно отправлен запрос по адресу `request.url` с JSON телом запроса, сформировавшемся из bodyString с помощью requestMapper-а.
Стоит заметить, что для построения прокси не указывается `HttpContentMapper`. Он берется из конфигурации `HttpClientModule`-я,
  
  Важный момент: `execute` возвращает объект `Optional`. Чтобы получить из него респонс имеется следующие конструкции:
  
  * `response = optional.orElse(DEFAULT_RESPONSE)`
  * `if (optional.isPresent()) { response = optional.get() }`
  * `optional.ifPresent(response -> {})`
    
  
## Основная функция модуля:
Отправка запросов по HTTP протоколу.
Является платформой для SOAP клиента. (Модуль application-soap-client)

## Особености конфигурации модуля
Отдельно в конфигурации стоит отметить два списка интерцепторов:
```
    List<HttpClientInterceptor> getRequestInterceptors();

    List<HttpClientInterceptor> getResponseInterceptors();

```
Основная задача интерцептора - выполнить некую логику ДО или ПОСЛЕ запроса.

Соответственно, `RequestInterceptors` отвечают за логику ДО запроса (логирование, валидация), а
`ResponseInterceptors` отвечают за логику ПОСЛЕ запроса (логирование)

[Пример работы с Apache HTTP Client](http://www.vogella.com/tutorials/ApacheHttpClient/article.html)

[Optional Шрёдингера](https://habr.com/post/346782/)