# What is ART?

## A little marketing or how I got to such a life
 
What means `coding` for you?

Two years ago I asked myself. And i'm answered. `Code` - is an ART.

* Coding is not about doing business tasks.
* Coding is not about writing `@Controller` or `@Service` annotation.
* Coding is not about refactoring of refactoring of refactoring.
* Coding is not about copy-paste from stackoverflow.

Coding is about making ART. Creating and producing beautiful solutions from you're mind.

* Now its not a time for 100500 same classes and services.
* Now its not a time for coding on annotations or YAML (which in principle is not a programming language)
* Now its not a time for wasting 24 hours during in an attempt to understand `Why the my context did not created and where are my beans ?`

Now is time to making ART. But does true art not require hellish zeal? Yep! 

Coding is difficult, painstaking, dreary, killing occupation. But the question is, what can we do to simplify it?

The best solution is - DK (development kit):

* SoftwareDK
* AndroidDK
* IOSDK
* WindowsDK

...and other DK.

So, yes, well, I can say, that ART is yet another development kit for Java Developing. 

But, why its not called `ADK`? 
      
Because of:

* ART is product of conceptual and configuring modeling. Its not just kit of shapeless functions. It has an Conceptual Model and Architecture.
* ART is toolbox for every Java Developer. Startup, EE, or just school project: you can use ART. 
* ART is a world. World of tools, technologies, functions and libraries. All parts of this world are chained, but independent.
* ART is a solution. 
    * Needs an HTTP REST integration with optimized GRPC inner protocol and realtime RSocket reactive updates to Web page ? You have an ART. 
    * Or you may be need a persistence for you're application with high reading speed and SQL solution is not for you? Just use `Tarantool` with ART. You no need to know `Lua` or `C++`. 
    * Integration thorough streaming ? `Kafka` modules are yore solution. Not just listener for consumer. ART provides you full support of `Kafka Stack` (`Broker, Consumer or Producer`)
    * Integration with nowadays stack, but SQL DB's haunt you and `Hibernate` can't generate yore query? DbUtils? SqlUtils? Familiar names, right? So you can just use a SQL module from ART.  
    * All working day trying to find the correct version of the library? It's not very like art. But what if I say that writing 3 lines in youre gradle file and you will have not just one library, you will have combination of multiple libraries, collected inside one dependency. 
* ART is simple. How much time you wasted during trying to understand how works nowadays framework? And how many files for sources you see when opening their code? And how many lines in that files? ART you can understand even without docs. Just try it! Look at the code.
* ART is compact. You are reading documentation of something framework and meet something like this `Go to <url> and install <soft> on youre machine`. Of course, some server application really must be installed with Linux package managers of even with Ansible, but what if i what to centralize configuring of them ? Or, for example, i whant to launch needed soft on any machine, but not inside the container (I hope you do not put your databases in the container on production). For solute yore task you no need to install the BIG database or deploy Message Broker, or write wrapper on a wrapper on wrapper. ART votes for embedded and distroless solutions.

## Words nothing, show me the features

### Protocols

ART support serving and communication with:

* HTTP (+ SOAP). As HTTP Server ART using Embedded Apache Tomcat, and for communication - Apache Http Client and Apache Async HTTP Client
* GRPC. Implementation of Google Protobuf RPC. A distinctive feature of ART is that it does not use .proto files to write a request and response scheme
* RSocket. All five modes: 
    * fireAndForget
    * requestResponse
    * requestSteam
    * requestChannel
    * metadataPush
    
In ART reactive and straight communication is just a two constants inside the enum. You can combine two styles in one common serving & communication model.

### Value & Data formats

ART support multiple data formats:

* JSON
* XML
* Protobuf
* Message Puck
* Plain Tuple

But all of thees formats have single abstraction point - Value.
Value could be a 
* Primitive `{Int | Float | Double | String | Boolean }`
* Collection of `{Int | Float | Double | String | Boolean | Value }`
* Entity that is `String -> Value`
* StringParameters that are `String -> String`
* MapValue that is `Value -> Value`

Every data structure that you write in youre code could be mapped to/from Value. 

And Value could be mapped to/from every data format.  Mapping to/from POJO doesn't use reflection or hand-written. Mappers code is generating from POJO. So you just write you're POJOs and run Gradle task that generate yore mappers.

### DB

ART support of:

* SQL databases. As connection pool you could choose between Tomcat and Hikari. As ORM library ART is using JOOQ.
* RocksDB. ART has DAO-compatibility API for interact with RocksDB.
* Tarantool. ART has DAO-compatibility API for interact with Tarantool. You don't need to install tarantool binary by yourself. Also you don't need to write Lua procedures to working with T. ART can work with Tarantool in two modes: `LOCAL` and `REMOTE`. When `LOCAL`, then ART will try to launch tarantool by itself. And when `REMOTE`, then ART will connect to already running instance of Tarantol. Also ART supporting multiple instances of Tarantool. But the key feature is that ART executes all the lua requests itself. You just working with ART Tarantool DAO in you're Java application, and don't write lua scripts. As a data structure to interact with Tarantool ART use Value converted to/from Plain Tuple.
* Reindexer. As a rocksDB ART will be able to work with Reindexer full-text search database.

 
## How does it looks?  


Well, okay, sounds good.

But, what inside in ART ? How its working and why i need read this docs and ART's code ?

Keywords of ART architecture:

* Context
* Module
* Configuration
* State
* Service
* Method
* Specification
* Communicator
* DAO
* Interceptor
* Function
* Value & Entity
* Mapper

