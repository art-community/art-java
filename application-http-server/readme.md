# ADK Модуль "application-http-server"

## Назначение модуля
Данный модуль предоставляет функционал HTTP сервера. В качестве контейнера сервлетов используется Embedded Tomcat.
Ключевые компоненты:

1. `HttpServer`
2. `HttpServerModule`
3. `HttpServiceConfigBuilder`
4. `HttpServerModuleConfiguration`

Предоставляемый функционал модуля можно разделить на две части:

1. Гибкий и удобный билдер HTTP сервера (`HttpServiceConfigBuilder`)
2. Обертка над Tomcat для обработки HTTP запросов и вызовов по ним ADK сервисов

## Подробнее про конфигурирование

Проще всего показать на примере:
```
public class ConfiguratorServiceSpec implements HttpServiceSpecification { - спецификация ADK сервиса 
    private final String serviceId = CONFIGURATOR_SERVICE_ID; - идентификатор сервиса. Является ключем в реестре сервисов, по которому можно получить текущий объект спеки

    private final HttpServiceConfig httpServiceConfig = httpService() - конфигурация HTTP сервиса
    Следующую секцию конфига стоит читать так:
    1. Мы привязываем метод с идентификатором "UPLOAD_CONFIG" на сервлет, который прослушивает путь "/upload".
    2. В качестве запроса мы ожидаем JSON (так как consumes - APPLICATION_JSON).
    3. Запрос пройдет валидацию (так как VALIDATABLE).
    4. В качестве маппера запроса будет applicationConfigurationMapper
    5. Данные для запросы ожидаем получить из тела запроса (альтернативы: QURY и PATH параметры URL)
    6. Ответ будет маппится с помощью uploadResponseMapper и на выходе представлять собой также JSON
    Аналогично для GET_JSON_CONFIG и APPLY_MODULE_CONFIG
                 
             
            .post(UPLOAD_CONFIG) 
            .consumes(APPLICATION_JSON)
            .fromBody()
            .withReq(applicationConfigurationMapper.getToModel(), VALIDATABLE)
            .produces(APPLICATION_JSON)
            .withResp(uploadResponseMapper.getFromModel())
            .listen("/upload")

            .post(GET_JSON_CONFIG)
            .fromBody()
            .withReq(moduleKeyMapper.getToModel(), VALIDATABLE)
            .produces(APPLICATION_JSON)
            .withResp(applicationConfigurationMapper.getFromModel())
            .listen("/get")

            .post(APPLY_MODULE_CONFIG)
            .consumes(APPLICATION_JSON)
            .fromBody()
            .withReq(moduleKeyMapper.getToModel(), VALIDATABLE)
            .listen("/apply")
    В конце конфигурирования мы говорим сервсиу - "слушай по адресу HTTP_PATH".
    Тем самым наш сервис будет обрабатывать запросы по адресу http://(хост:порт)/serve_путь/listen_путь.
    Хост и порт берутся из HttpServerModuleConfiguration.
            .serve(HTTP_PATH);

    private final List<String> serviceTypes = fixedArrayOf(HTTP_SERVICE_TYPE);


    Далее начинается вторая часть "магии" модуля.
    Когда нам прилетит запрос и пройдет соответствующие процедуры идентификации и маппинга, будет вызван один из следующих методов (в соответствие с methodId).
    После выполнение бизнес логики сервиса (ниже вызывается уже сам ADK сервис), полученный респонс пройдет обратную процедуру маппинга и будет отправлен в виде JSON.
    Если респонс отсутствует, то ничего не будет отправлено. 
  
    @Override
    public <P, R> R executeMethod(String methodId, P req) {
        switch (methodId) {
            case UPLOAD_CONFIG:
                return cast(uploadConfiguration((ApplicationConfiguration) req));
            case GET_JSON_CONFIG:
            case GET_PROTOBUF_CONFIG:
                return cast(getProjectConfiguration((ModuleKey) req));
            case APPLY_MODULE_CONFIG:
                applyModuleConfiguration((ModuleKey) req);
                return null;
            default:
                throw new UnknownServiceMethodException(serviceId, methodId);
        }
    }
}
```

## Основная функция модуля:
Приём запросов по HTTP протоколу.
Является платформой для SOAP сервера. (Модуль application-soap-server (он ещё не доделан...))

## Особености конфигурации модуля
Отдельно в конфигурации стоит отметить два списка интерцепторов и наиболее часто задаваемые параметры (порт и хост):
```
    int getPort();

    String getHost();

    List<HttpServerInterceptor> getRequestInterceptors();

    List<HttpServerInterceptor> getResponseInterceptors();


```
Порт используется в качестве порта для основного "слушателя" томкатика. Крайне важно задавать порты грамотно, чтобы не было пересечений.
Хост - чаще всего задаётся (0.0.0.0). Не могу сейчас придумать случаев, когда понадобится другое значение.  

Основная задача интерцептора - выполнить некую логику ДО или ПОСЛЕ запроса.

Соответственно, `RequestInterceptors` отвечают за логику ДО запроса (логирование, валидация), а
`ResponseInterceptors` отвечают за логику ПОСЛЕ запроса (логирование)

## А детали?
Модуль своего рода один большой сервлет, который сам поднимает свой контейнер.
Чтобы детально изучить принцип обработки запроса от Томкатика до сервиса, нужно просмотреть цепочку классов
`HttpServer -> HttpServiceServlet -> HttpServletCommand -> HttpServerRequestHandler -> HttpServiceSpecification`.
Именно такой путь проходит запрос от "прилета" на порт до обработки сервисом.
Ответ, разумеется, аналогично в обратном направлении.

## Что почитать
[Сервлеты](http://java-course.ru/student/book1/servlet/)

[Томкат](https://tomcat.apache.org/tomcat-9.0-doc/index.html)

