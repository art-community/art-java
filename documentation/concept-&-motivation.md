# What is ART?

## A little marketing or how I got to such a life
 
What means `coding` for you?</br>
`Code` - is an ART.

* Coding is not about doing business tasks.
* Coding is not about writing `@Controller` or `@Service` annotation.
* Coding is not about refactoring of refactoring of refactoring.
* Coding is not about copy-paste from stackoverflow.

Coding is about making ART. Creating and producing beautiful solutions from your mind.

* Now it's not the time for 100500 same classes and services.
* Now it's not the time for coding on annotations or YAML (which in principle is not a programming language)
* Now it's not the time for wasting 24 hours during in an attempt to understand `Why the my context did not created and where are my beans ?`

Often coding is difficult, painstaking, dreary, killing occupation for creativity. So it's better to simplify it.
It is the time to make ART! Less thinking how to code and more about how it works and looks!

To prevent ourselves making complicated solutions every time we use DK (development kit):

* SoftwareDK
* AndroidDK
* IOSDK
* WindowsDK

...and other DK.

ART is a development kit for Java Developers. And also it is:

* The product of conceptual and configuring modeling. Its not just kit of ~~shapeless~~ universal functions.
* The power for every developer to be a creator. World of tools, technologies, functions and libraries. All parts of this world are chained, but independent.
* Toolbox for any project. Startup, Enterprise or just school project: you can use ART. 

With ART you can:
    * create HTTP/GRPC services
    * create two-way reactive communication between web page and server using RSocket  
    * use `Tarantool` with ART when you may be need a persistence for you're application with high reading speed and SQL solution is not for you. You no need to know `Lua` or `C++`.
    * create integration thorough streaming using `Kafka` modules. Not just listener for consumer. ART provides you full support of `Kafka Stack` (`Broker, Consumer or Producer`)
    * integration with latest technology stack without worries about query generation for SQL DB and `Hibernate`. You won't need to create any DbUtils, SqlUtis and other same endless horrific util-classes - you can just use a SQL module from ART. 
    
 With ART you won't spend too much time learning because:
* ART is simple. How much time you wasted during trying to understand how works nowadays framework? And how many files for sources you see when opening their code? And how many lines in that files? ART you can understand even without docs. Just try it! Look at the code.
* ART is compact. You are reading documentation of something framework and meet something like this `Go to <url> and install <soft> on youre machine`. Of course, some server application really must be installed with Linux package managers of even with Ansible, but what if i what to centralize configuring of them ? Or, for example, i whant to launch needed soft on any machine, but not inside the container (I hope you do not put your databases in the container on production). For solute yore task you no need to install the BIG database or deploy Message Broker, or write wrapper on a wrapper on wrapper. ART votes for embedded and distroless solutions.
* you won't spend all day trying to find the correct version of the library. Write 3 *magic* lines in your gradle file and you will have combination of multiple libraries, collected inside one dependency.

## Words nothing, show me the features

### Protocols

ART support serving and communication with:

* `HTTP` (+ `SOAP`). As HTTP Server ART using Embedded Apache Tomcat, and for communication - Apache Http Client and Apache Async HTTP Client
* `GRPC`. Implementation of Google Protobuf RPC. Besides, ART doesn't use .proto files to write a request and response scheme
* `RSocket`. All five modes: 
    * fireAndForget
    * requestResponse
    * requestSteam
    * requestChannel
    * metadataPush
    
By the way, ART allows you to work with reactive and straight way communcation.

### Value & Data formats

ART supports multiple data formats:

* JSON
* XML
* Protobuf
* Message Puck
* Plain Tuple

All of these formats have a single abstraction point - Value.
Value could be:
* Primitive `{Int | Float | Double | String | Boolean }`
* Collection of `{Int | Float | Double | String | Boolean | Value }`
* Entity that is `String -> Value`
* StringParameters that are `String -> String`
* MapValue that is `Value -> Value`

Every data structure that you write in your code can be mapped to Value. And Value can be mapped to every data format.

Mapping to/from POJO doesn't use reflection. Mapper can be generated from POJO:
1. You can write POJO 
2. Run mapper generation Gradle task
3. Mappers are ready!

### DB

ART supports:

* SQL databases. As connection pool you can choose Tomcat or Hikari. As ORM library ART is using JOOQ.
* DAO-compatibility API for RocksDB, Tarantool, Reindexer.
* Reindexer. As a rocksDB ART will be able to work with Reindexer full-text search database.

Also if you use Tarantool ART has something more to offer:
* You don't need to install tarantool binary by yourself. 
* You don't need to write Lua procedures to working with T. 

ART can work with Tarantool in two modes: `LOCAL` and `REMOTE`. 
With `LOCAL mode` is on ART will try to launch embedded Tarantool by itself. 
With `REMOTE mode` is on ART will connect to the existing instance of Tarantool. 
Also ART supports connection to the multiple instances of Tarantool. 
ART executes all the lua requests itself. You just working with ART Tarantool DAO in you're Java application, and don't write lua scripts. As a data structure to interact with Tarantool ART use Value converted to/from Plain Tuple.

### Configuring

ART could be configured by:

* YAML
* JSON
* HOCON
* Java Properties
* ART Value (receiving from remote configurator by GRPC protocol)

Remote configuration could be applied by runtime without downtime. Server's parameters like port's also could be changed. In this case server will be restarted;

### Scheduling

ART has two schedulers: 

* LOCAL. Jar embedded thread pool wish ordered tasks
* REMOTE. Server scheduler, that could handle tasks of three types:  periodic, infinity and deferred; 
 
### Kafka

ART has modules for 

* Kafka Broker. Has embedded Kafka inside and managed by ART configuration management.
* Kafka Producer. ART communication through Kafka producing.
* Kafka Consumer & Streams. ART communication through Kafka consuming and streaming.
 
### Service Model 
 
ART has itself service architecture. All applications actions are service methods. Service has specification. Specification describe, how we can use service (for example, HttpServiceSpecification or GrpcServiceSpecification).

Specification also specify some configurable parameters of service:

* Resiliency attributes for circuit breaker, rate limiter, retryer
* Deactivation attributes: needs for runtime deactivation service or method

Specification also has ExceptionWrapper for handling service exception exceptions. All service call will not produce an exception: results could be Optional or ServiceResponse, but not an exception or throwable. 

### Interceptors

ART Produces multiple layers of interceptors:

* Transport/Protocol interceptors:
    * RSocket interceptor
    * GRPC Server & Client interceptors
    * HTTP Server, service, service method interceptors
    * HTTP Client interceptor
* All service interceptor. Concrete service interceptor. Service methods interceptors.
  
### Logging & Metrics

ART use log4j2 as logger. ART support overriding logging ofr log4j, slf4j and jul.

For metrics ART use Dropwizard & Micrometer metrics libraries and producing metrics by `/metrics` endpoint in prometheus format.

### Web

ART likes web !

Every module with HTTP Serving will have /information/ui endpoints, that provides web page with module services & protocols information.

HTML pages rendered by pebble engine. But ART can render not only HTML. Every resource, that you need could be processed by ART templating.

## How does it looks?  

Well, okay, sounds good.

But, what inside in ART ? How its working and why i need read this docs and ART's code ?

### Keywords of ART **architecture**:

#### Where does it all begin
* Context
* Module
* Configuration
* State

#### Hands of ART
* Service
* Method
* Specification
* Function

#### Voice of ART
* Communicator

#### ART's Data 
* DAO

#### Catch them all
* Interceptor

#### Do it more abstract
* Value & Entity
* Mapper

### I want to hear three words... Context, Module, Configuration

**Module** - basic conception of any functional in ART architecture.

Model is separate project, separate part of code. 

Every module has 
```
class *ModuleName*Module implements Module<*ModuleName*Configuration, ModuleState>
```

**Module Configuration** - store of any module configurable attribute or object. 

Configuration - place, where we can store any module public objects, thant could be changed in module loaders.

For example, HTTP/GRPC server modules have port in their configurations. Because when we are using HTTP or GRPC server we want to specify their port (by default wil be used any free port on first network adapter).

**Module State** - make you're module stateful.

State - place, where we can store any mutable object, related to our module. 

For example, RocksDB module state has reference to RockDB object, that contain pointer to native RocksDB handler.
This object creating on module loading, using during RocksDB DAOs interacting and destroying on module unloading.

**Context**  - store for all modules. Context is just threadsafe API for map of modules.
It can:
 * Getting state module by name or load module by method reference
 * Getting module by name or load it by method reference
 * Loading module
 * Overriding module
 * Reloading module
 * Refreshing module
 * Check module on existing in context

There are all production-ready modules, that ART provides to you

![Image](https://i.ibb.co/L9mNCbj/image.png)

### Think and do

**Service** - main executable unit in ART.

Service could be imagine as `{Request|Nothing} -> {Response|Nothing}`.

This sounds like 'Convert some requests to some response'.

So service is mapper or converter or handler that working with requests and produces responses.

Service may has some **methods** or could be just a **function** `(X) -> Y`.  

*Method* just is single named action with input and output.

Input could be empty or any request;
Output could be empty or any response; 

**Function** is a service with one method. Functions are protocol-dependent: HTTP_SERVICE_FUNCTION, GRPC_SERVICE_FUNCTION, KAFKA_SERVICE_FUNCTION.

In fact function is just a sugar for declaring service. For example HTTP GET function:
```java
   httpGet("/hello")
                .fromPathParameters("param")
                .requestMapper(stringParameterToStringMapper("param").getToModel())
                .responseMapper(fromModel)
                .handle(requestParam -> "<h1>" + requestParam + "</h1>");
```

This function  receives any (`param`) from url and convert (`->`) into (`HTML header`): `(param) -> (HTML header of param)`.

Function can be:
* handling `(X) -> Y`
* consuming `(X) > {} `
* producing `() -> Y`

Service may has **Specification**.
 
**Specification** describes how we can use our Service.

For example, we want to send HTTP POST request for our application and do some actions.
Then we can use HttpServiceSpecification, inside it we describe, what we want:

```java
httpService() // Beacuse may be we want to use our service not as HTTP
            .post(DO_ACTION) // What **Service Method** will be called on HTTP request 
            .consumes(applicationJsonUtf8()) // What content mime type we are expecting from client
            .fromBody() // Where where must search request
            .validationPolicy(VALIDATABLE) // Validatiable ? Of course!
            .requestMapper(toOurRequest) // How we must parse our request 
            .produces(applicationJsonUtf8()) // What content mime type expecting our client
            .responseMapper(fromExtInteractionCreateResponse) // How we must write our response
            .exceptionMapper(exceptionMapper) / How we muse write our errors
            .listen("/doAction") // What url we want handle
// Above we can describe any other HTTP handlers 
```

... And, it's all!
