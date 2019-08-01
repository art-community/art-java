# АDK Модуль "application-remote-scheduler"

## Назначение модуля

Шедулер предназначен для вызова определенного метода у сервиса модуля с какими-либо параметрами. 
Для взамиодействия с другими модулями используются protobuf запросы, соответственно, если у вашего модуля есть методы 
сервиса, которые необходимо вызывать через шедулер, необходимо определить их protobuf спецификацию.

`application-remote-scheduler` реализует основную основную логику работы с отложенными событиями. 
Взаимодействие с базой данных осуществляется с помощью `application-remote-scheduler-db-adapter-api`. 
Данный модуль содержит в себе интерфейс сервиса для работы с базой данных, 
а так же спецификацию данного сервиса. То есть для полноценной работы шедуллера необходимо 
реализовать два отдельных модуля: модуль с реализацией интерфейса
из `application-remote-scheduler-db-adapter-api`,  а так же модуль, модуль, 
объединяющий `application-remote-scheduler`, а так же созданный модуль для работы с БД. 
Такой подход обеспечивает независимость работы шедулера от конкретной базы данных.
Обобщающий модуль должен в методе загрузки иметь следующее:

```
context()
        .loadModule(new SchedulerModule())

serviceModule().getServiceRegistry()
        .registerService(new RemoteSchedulerServiceSpec())
        .registerService(new SchedulerDbAdapterServiceSpec(SCHEDULER_DB_ADAPTER_SERVICE_ID, new SchedulerDbAdapterServiceImpl()));
```
 
`SCHEDULER_DB_ADAPTER_SERVICE_ID` - id сервиса с реализацией адаптера к базе данных
`SchedulerDbAdapterServiceImpl()` - класс сервиса, реализующий интерфейс `SchedulerDbAdapterService` 
из `application-remote-scheduler-db-adapter-api`.


## Обрабатываемые задачи

Шедулер способен обрабатывать 3 типа задач (классы находятся в `application-remote-scheduler-api`)
* DeferredTask
* PeriodicTask
* InfinityProcess

Для того, что бы добавить задачу в шедуллер, можно воспользоваться двумя способами:
Через http запрос через Postman (или его аналоги), или же из кода через protobuf запрос.
Так же DeferredTask и PeriodicTask можно добавлять запросов в базу данных, но обновление из базы данных производится 
с периодичностью, заданной в конфигурации (см. Конфигурация модуля)

#### DeferredTask

Данный тип задачи имеет следующий жизненный цикл:

_todo Блок-схема_

То есть данный тип необходимо применять для разового выполнения в определенное время. Может иметь 3 статуса: 
* `NEW` - Новая задача, ожидающая времени своего выполнения;
* `COMPLETED` - удачное выполнение задачи;
* `FAILED` - ошибочное выполнение задачи (в процессе выполнения получено исключение).

__Http запрос__

```
{
    "executableServletPath": "ru.adk.protobuf.servlet.ExampleModuleServlet",
	"executableServiceId": "EXAMPLE_SERVICE",
	"executableMethodId": "EXAMPLE_METHOD",
	"executionDateTime": "01.01.2019 12:30:30",
	"executableRequest": {
		"intParameter" : 12345,
		"stringParameter" : "some string"
	}
}
```
Так же в `executableRequest` можно сеттить примитивные типы.

__Profobuf запрос__

```
Entity request = Entity.entityBuilder()
                .intField("intParameter", 12345)
                .stringField("stringParameter", "some string")
                .build();
                
DeferredTaskRequest deferredTaskRequest = DeferredTaskRequest.builder()
        .executableServiceId("EXAMPLE_SERVICE")
        .executableMethodId("EXAMPLE_METHOD")
        .executableServletPath("ru.adk.protobuf.servlet.ExampleModuleServlet")
        .executionDateTime(LocalDateTime.now())
        .executableRequest(request)
        .build();

ProtobufClientProxyBuilder.<DeferredTaskRequest, String>protobufClientProxy()
        .syncClient(new ProtobufSyncClient(ProtobufClientConfig.builder()
                .path(SCHEDULER_SERVLET_PATH)
                .host(BALANCER_HOST)
                .port(BALANCER_HOST)
                .build()))
        .serviceId(REMOTE_SCHEDULER_SERVICE_ID)
        .methodId(ADD_DEFERRED_TASK)
        .requestMapper(deferredTaskRequestFromModelMapper)
        .responseMapper(stringMapper.getToModel())
        .prepare()
        .execute(deferredTaskRequest);
    
```

_todo сделать proxy для удобного запроса_

При старте шедуллера, в него загрузятся все задачи, имеющие статус `NEW`.

#### PeriodicTask

Данный тип задачи имеет следующий жизненный цикл:

_todo Блок-схема_

Такой тип стоит использовать, когда необходимо повторить вызов какого либо сервиса определенное количество раз с определенным интервалом. В зависимости от параметра finishAfterCompletion завершает свою работу после первого «успешного» вызова сервиса. То есть данный механизм позволяет решать задачу «гарантированной доставки»
Может иметь 3 статуса:
* `NEW` - Новая задача, ожидающая времени своего первого выполнения;
* `PROCESSING` - первое выполнение уже прошло, но еще не дошло до последнего;
* `FINISHED` - процесс завершен;
* `CANCELLED` - процесс отменен.


__Http запрос:__

```
{
	"executableServletPath": "ru.adk.protobuf.servlet.ExampleModuleServlet",
	"executableServiceId": "EXAMPLE_SERVICE",
	"executableMethodId": "EXAMPLE_METHOD",
	"executionDateTime": "01.01.2019 12:30:30",
	"maxExecutionCount" : 6,
	"executionPeriodSeconds" : 10,
	"finishAfterCompletion" : false,
	"executableRequest": {
		"intParameter" : 12345,
		"stringParameter" : "some string"
	}
}
```

__Profobuf запрос:__

```
Entity request = Entity.entityBuilder()
                .intField("intParameter", 12345)
                .stringField("stringParameter", "some string")
                .build();

PeriodicTaskRequest periodicTaskRequest = PeriodicTaskRequest.builder()
        .executableServiceId("EXAMPLE_SERVICE")
        .executableMethodId(BALANCE_METHOD_ID)
        .executableServletPath("EXAMPLE_METHOD")
        .maxExecutionCount(3)
        .executionDateTime(LocalDateTime.now())
        .executionPeriodSeconds(10)
        .finishAfterCompletion(false)
        .executableRequest(new Primitive(TEST_OBJID, LONG))
        .build();


ProtobufClientProxyBuilder.<PeriodicTaskRequest, String>protobufClientProxy()
        .syncClient(new ProtobufSyncClient(ProtobufClientConfig.builder()
                .path(SCHEDULER_SERVLET_PATH)
                .host(SCHEDULER_HOST)
                .port(SCHEDULER_PORT)
                .build()))
        .serviceId(REMOTE_SCHEDULER_SERVICE_ID)
        .methodId(ADD_PERIODIC_TASK)
        .requestMapper(periodicTaskRequestFromModelMapper)
        .responseMapper(stringMapper.getToModel())
        .prepare()
        .execute(periodicTaskRequest);
```

_//todo сделать proxy для удобного запроса_

При старте шедуллера, в него загрузятся все задачи, имеющие статус `NEW`, `PROCESSING`.

#### InfinityProcess

Представляет из себя задачу, выполняющуюся бесконечное количество раз с определенным периодом.
Не имеет статусов, создать задачу можно http запросом или черз запрос на добавление в базу данных с последующим рестартом шедуллера

__Http запрос для создания задачи:__
```
{
	"executableServletPath": "ru.adk.protobuf.servlet.ExampleModuleServlet",
	"executableServiceId": "EXAMPLE_SERVICE",
	"executableMethodId": "EXAMPLE_METHOD",
	"executionPeriodSeconds" : 10,
	"executableRequest": {
		"intParameter" : 12345,
		"stringParameter" : "some string"
	}
}
```

## Интерфейс сервиса 

* `String addDeferredTask(DeferredTaskRequest deferredTaskRequest)`
* `String addPeriodicTask(PeriodicTaskRequest periodicTaskRequest)`
* `String addInfinitiveProcess(InfinityProcessRequest request)`
* `DeferredTask getDeferredTaskById(String taskId)`
* `PeriodicTask getPeriodicTaskById(String taskId)`
* `Set<DeferredTask> getAllDeferredTasks()`
* `Set<PeriodicTask> getAllPeriodicTasks()`
* `Set<InfinityProcess> getAllInfinityProcesses()`
* `void cancelPeriodicTask(String taskId)`

Скачать проект Postman с примерами всех запросов можно [тут](http://10.35.215.200/gitlab/development/application-remote-scheduler/wikis/uploads/cfb616da40e5524b678a7dfe62dcc309/Scheduler.postman_collection.json).

## Конфигурация модуля

```
scheduler:
  http:
    path: /scheduler
  refreshDeferredPeriodMinutes: 5
  refreshPeriodicPeriodMinutes: 1440
balancer:
  host: localhost
  port: 20005
db:
  adapter:
    serviceId: «SCHEDULER_DB_ADAPTER_SERVICE"
```

* `http.path` - указание http пути шедуллера;
* `refreshDeferredPeriodMinutes` - период обновления задач `DeferredTask`  из базы данных;
* `refreshPeriodicPeriodMinutes`- период обновления задач `PeriodicTask`  из базы данных;
* `db.adapter.service.id` - id сервиса с реализацией адаптера к базе данных.
