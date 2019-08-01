# ADK Модуль "application-protobuf-server"

## Назначение модуля
Данный модуль предоставляет функционал Protobuf сервера. 
Ключевые компоненты:

1. `ProtobufServer`
2. `ProtobufServerModule`
3. `ProtobufServerModuleConfiguration`
4. `ProtobufServiceSpecification`

Предоставляемый функционал модуля можно разделить на две части:

1. Гибкий и удобный билдер Protobuf сервера (`protobufService`)
2. Обертка над GRPC для обработки Protobuf запросов и вызовов по ним ADK сервисов

## Подробнее про конфигурирование

Проще всего показать на примере:
```
public class ConfiguratorServiceSpec implements HttpServiceSpecification { - спецификация ADK сервиса 
    private final String serviceId = CONFIGURATOR_SERVICE_ID; - идентификатор сервиса. Является ключем в реестре сервисов, по которому можно получить текущий объект спеки

     private final protobufService protobufService = protobufService() - конфигурация Protobuf сервиса
    
    Следующую секцию конфига стоит читать так:
    1. Мы объявляем Protobuf метод с идентификатором "GET_PROTOBUF_CONFIG".
    2. В качестве маппера запроса будет moduleKeyMapper
    3. Ответ будет маппится с помощью applicationConfigurationMapper
            
            .method(GET_PROTOBUF_CONFIG, protobufMethod()
                    .requestMapper(moduleKeyMapper.getToModel())
                    .responseMapper(applicationConfigurationMapper.getFromModel())
                    .build())
            .build();

    Тем самым наш сервис будет обрабатывать запросы по адресу grpc://(хост:порт)/path/executeService().
    Хост, порт и сервлет берутся из ProtobufServerConfiguration.

    private final List<String> serviceTypes = fixedArrayOf(PROTOBUF_SERVICE_TYPE);

    Далее начинается вторая часть "магии" модуля.
    Когда нам прилетит запрос и пройдет соответствующие процедуры идентификации и маппинга, будет вызван один из следующих методов (в соответствие с methodId).
    После выполнение бизнес логики сервиса (ниже вызывается уже сам ADK сервис), полученный респонс пройдет обратную процедуру маппинга и будет отправлен в виде Protobuf.
    Если респонс отсутствует, то ничего не будет отправлено. 
  
    @Override
    public <P, R> R executeMethod(String methodId, P req) {
        switch (methodId) {
            case GET_PROTOBUF_CONFIG:
                return cast(getConfiguration((ModuleKey) req));
            default:
                throw new UnknownServiceMethodException(serviceId, methodId);
        }
    }
}
```

## Основная функция модуля:
Приём запросов по Protobuf протоколу.

## Особености конфигурации модуля
Отдельно в конфигурации стоит отметить два списка интерцепторов и наиболее часто задаваемые параметры (порт и путь сервлета):
```
    int getPort();

    String getServletPath();

    List<ServerInterceptor> getInterceptors();

```
Порт используется в качестве порта для основного "слушателя" GRPC. Крайне важно задавать порты грамотно, чтобы не было пересечений.

Путь сервлета используется для определения модуля во внутренней сети. 
Проще говоря, "адрес" сервера задаётся тройкой хостп:порт:сервлет
Сервлеты должны быть уникальны в рамках одной сети. Для этого нужно грамотно подбирать их название.
Пример: `ru.rti.crm.protobuf.servlet.CrmBalanceServlet`

Основная задача интерцептора - выполнить некую логику ДО или ПОСЛЕ запроса.

Соответственно, `Interceptors` отвечают за логику ДО запроса (логирование, валидация) И ПОСЛЕ запроса (логирование)

## А детали?
Модуль своего рода один большой сервлет, который сам поднимает свой контейнер.
Чтобы детально изучить принцип обработки запроса от Томкатика до сервиса, нужно просмотреть цепочку классов
`ProtobufServer-> ProtobufServletContainer -> HttpServiceSpecification`.
Именно такой путь проходит запрос от "прилета" на порт до обработки сервисом.
Ответ, разумеется, аналогично в обратном направлении.

## Что почитать
[Protobuf](https://developers.google.com/protocol-buffers/)

[GRPC](https://grpc.io/)