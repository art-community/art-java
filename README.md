# ART

ART is a kit of modules for developing multifunctional applications.


## Build and Binaries
Releases are available via Maven Central.

Example:

```kotlin
plugins {
    id("io.github.art.project") version "1.0.62"
}

art {
  idea()
  lombok()
  tests()
  
  // Modules that includes into result project *.jar 
  // Alternatives: providedModules (not included into jar)  and 
  // testModules (use in tests sources)
  /*embeddedModules {
      // For specify version
      useVersion("1.+") //or useVersion() without arguments. Then will be used "1.+" version as default

      // For full kit of modules 
      kit()    
    
      // For using protocols
      //protocols {
          //grpcServer()
          //grpcCommunication()
          //httpServer()
          //httpCommunication()
          //rsocket()
          //soapServer()
          //soapCommunication()
      //}
      
      // For using databases
      //db {
          //sql()
          //tarantool()
          //rocksdb()
      //}   
      
      // For using file configurations
      //localConfigurationManagement()
      
      // For using remote configurations    
      //remoteConfigurationManagement()

      // For using file and configurations    
      //configurationManagement()

      // For using local scheduler
      //localScheduling()
      
      // For using remote scheduler
      //remoteScheduling()

      // For using local and remote scheduler
      //scheduling()
      
      // For using kafka consumer
      //kafkaConsumer()            
      
      // For using kafka producer
      //producer()            
      
      // For using kafka consumer and producer
      //kafka()            

      dataFormats {
          // Includes by default if using grpc/rsocket/remote configuration modules
          protobuf()
          // Includes by default if using rsocket/http modules
          json()
          // Includes by default if using rsocket/soap modules
          xml()
      }            
  }*/  
    
  //For using ART mappers and specifications generator
  generator {
    //packageName = "<path to application main package (parent of 'model' and/or 'service' packages)>"
  }

  // For using web (sources need to be placed in src/main/web (by default) 
  //web()
  
  // For using Groovy 
  //groovy()

  // For using Kotlin 
  //kotlin()

  // For using Scala 
  //scala()
  
  // For using BDD Spock Framework  
  //spockFramework()

  // For using JMH  
  //jmh()

  // For using Gatling  
  //gatling()
 }
```
## Requirements

- Java 8 - heavy dependence on Java 8 functional APIs
- If working with web - installed nodejs and npm
- If using tarantool - installed tarantool
- If using kafka - installed kafka
- If using sql - installed SQL db

## Bugs and Feedback

For bugs, questions and discussions please use the [Github Issues](https://github.com/art-community/art/issues).

## LICENSE

ART Java

Copyright 2019 ART

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
