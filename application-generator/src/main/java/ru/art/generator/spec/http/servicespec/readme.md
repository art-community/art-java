# ADK генератор кода для Http сервисной спецификации (HttpServiceSpecification)

## Назначение
Генератор позволяет на основе интерфейсов-сервисов создавать классы-спецификации для организации взаимодействия между модулями.

## Принцип работы
Запуск генератора для определенного класса осуществляется через Gradle задачу - `generator.specifications.<имя сервиса>.generate<имя сервиса>HttpSpecification`.

Пример: generateExampleServiceHttpSpecification

Далее выполняется следующая последовательность шагов:
1. Компиляция класса;
2. Создание пакета **spec** (при необходимости) и генерация в него спецификаций.

Для генерации спецификаций всех сервисов, существующих в пакете **service** запускается Gradle задача `generator.specifications.<имя сервиса>.generate<имя сервиса>Specifications`.

Пример: generateExampleServiceSpecifications

## Настройка
Для корректной работы генератора в `build.gradle` проекта, для которого требуется сгенерировать спецификацию, нужно прописать:
```
ext {
    packageName = 'имя_родительского_пакета'
}
```
и добавить в зависимости проект 'application-generator' (по умолчанию version: adk_version).

## Ограничения
1. Для генерации Http сервисной спецификации интерфейс Service должен содержать аннотацию @HttpService.

2. Аннотация @RequestMapper требует наличия аннотаций @FromBody/@FromMultipart/@FromPathParams/@FromQueryParams.

3. Для аннотаций @HttpTrace, @HttpHead и @HttpConnect невозможно создать request.

4. Нельзя одновременно указать несколько аннотаций для http методов: @HttpGet/@HttpPost/@HttpDelete/@HttpConnect/@HttpHead/@HttpOptions/@HttpPatch/@HttpPut/@HttpTrace.

5. Нельзя одновременно указать несколько аннотаций: @FromBody/@FromMultipart/@FromPathParams/@FromQueryParams.

6. Основные поддерживаемые примитивные классы для мапперов:
   ```
       java.lang.String;
       java.lang.Integer / int;
       java.lang.Double / double;
       java.lang.Long / long;
       java.lang.Byte / byte;
       java.lang.Boolean / boolean;
       java.lang.Float / float;
   ```
 7. Переменные мапперов, которые создаются генератором, должны иметь названия "to" + <Имя класса модели> и "from" + <Имя класса модели>

## Возможности
1. Поддерживаемые аннотации:
```
    @NonGenerated
    @HttpService
    @Listen
    @NotNull
    @RequestMapper
    @ResponseMapper
    @Validatable
    @FromBody
    @FromMultipart
    @FromPathParams
    @FromQueryParams
    @HttpConsumes
    @HttpProduces
    @HttpGet
    @HttpPost
    @HttpDelete
    @HttpConnect
    @HttpHead
    @HttpOptions
    @HttpPatch
    @HttpPut
    @HttpTrace
    @RequestInterceptor
    @ResponseInterceptor
```

2. Аннотация **@NonGenerated**
Используется для сервисов, которые генератор не должен обрабатывать.

3. Аннотация **@GenerationException**

Данной аннотацией помечается константа httpService, если часть методов не была сгенерирована, с указанием метода и необработанной аннотации.

Аннотация имеет вид:
```
@GenerationException(
	notGeneratedFields = "[firstMethod: HttpConsumes, secondMethod: ResponseMapper]"
)
```
где notGeneratedFields - список методов с указанием необработанных в них аннотаций.

## Что почитать
[JavaPoet](https://www.baeldung.com/java-poet)