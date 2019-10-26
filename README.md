# ART
ART is a kit of modules for developing multifunctional applications.


## Badges
![Build Status](https://travis-ci.com/art-community/ART.svg)
[![Gitter](https://badges.gitter.im/art-community/community.svg)](https://gitter.im/art-community/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
![Bintray](https://img.shields.io/bintray/v/art-community/art/io.github.art)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3a5d459c173a4109b2d039c8f7cd3cce)](https://app.codacy.com/app/antonbashir/ART?utm_source=github.com&utm_medium=referral&utm_content=art-community/ART&utm_campaign=Badge_Grade_Dashboard)
![GitHub repo size](https://img.shields.io/github/repo-size/art-community/art)

## Build and Binaries
Releases are available via Maven Central.

Example:
```kotlin
plugins {
    id("io.github.art.project") version "1.0.81"
}

art {
  idea()
  lombok()  
  
// Modules that includes into result project *.jar 
  // Alternatives: providedModules (not included into jar)  and 
  // testModules (use in tests sources)
  embeddedModules {
      // For specify version. Default version is "1.+" 
      //useVersion("1.+")

      // For full kit of modules 
      kit()    
  }  
 }
```
## Requirements
- Java 8+ (Tested on 9, 11, 13)
- Gradle 5.+
- If working with web - installed nodejs and npm
- If using tarantool on Windows - installed WSL (Tarantool is not supported on Windows System)
- If using sql - installed SQL db

## Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/art-community/art/issues).

## Dependency libraries

* `junit:junit:4.12`
* `org.codehaus.groovy:groovy-all:2.5.+`
* `com.typesafe:config:1.4+`
* `com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.+`
* `com.fasterxml.jackson.core:jackson-core:2.9.+`
* `com.fasterxml.jackson.core:jackson-databind:2.9.+`
* `org.spockframework:spock-core:1.1+`
* `cglib:cglib-nodep:3.2.+`
* `com.oracle:ojdbc6:11.+`
* `com.squareup:javapoet:1.11.+`
* `org.membrane-soa:service-proxy-core:4.6.1`
* `com.google.guava:guava:25.1-android`
* `org.apache.httpcomponents:httpclient:4.5.+`
* `io.grpc:grpc-stub:1.18.0`
* `io.grpc:grpc-netty:1.18.0`
* `io.grpc:grpc-netty-shaded:1.18.0`
* `org.zalando:logbook-core:1.11.+`
* `org.apache.httpcomponents:httpcore:4.4.+`
* `org.apache.httpcomponents:httpasyncclient:4.1.+`
* `org.zalando:logbook-httpclient:1.11.+`
* `org.apache.tomcat.embed:tomcat-embed-core:9.0.+`
* `org.apache.tomcat:tomcat-servlet-api:9.0.+`
* `org.zalando:logbook-servlet:1.11.+`
* `org.jtwig:jtwig-core:5.87.0.RELEASE`
* `org.jeasy:easy-random:4.+`
* `org.jeasy:easy-random-core:4.+`
* `org.apache.kafka:kafka_2.12:2.2.+`
* `org.apache.kafka:kafka-clients:2.2.+`
* `org.apache.kafka:kafka-log4j-appender:0.9.0.0`
* `org.apache.kafka:kafka-streams:2.2.+`
* `org.slf4j:jul-to-slf4j:1.7.+`
* `org.apache.logging.log4j:log4j-api:2.11.+`
* `org.apache.logging.log4j:log4j-core:2.11.+`
* `org.apache.logging.log4j:log4j-slf4j-impl:2.11.+`
* `org.msgpack:msgpack-core:0.8.+`
* `io.micrometer:micrometer-registry-prometheus:1.+`
* `io.github.mweirauch:micrometer-jvm-extras:0.1.2`
* `io.prometheus:simpleclient_dropwizard:0.5+`
* `io.dropwizard.metrics:metrics-core:4.0.+`
* `io.dropwizard.metrics:metrics-jvm:4.0.+`
* `io.grpc:grpc-protobuf:1.18.0`
* `io.projectreactor:reactor-core:3.2.9.+`
* `org.rocksdb:rocksdbjni:5.14.+`
* `io.rsocket:rsocket-core:0.12.+`
* `io.rsocket:rsocket-transport-netty:0.12.+`
* `io.github.resilience4j:resilience4j-circuitbreaker:0.13.+`
* `io.github.resilience4j:resilience4j-ratelimiter:0.13.+`
* `io.github.resilience4j:resilience4j-retry:0.13.+`
* `io.github.resilience4j:resilience4j-metrics:0.13.+`
* `io.github.resilience4j:resilience4j-bulkhead:0.13.+`
* `io.github.resilience4j:resilience4j-timelimiter:0.13.+`
* `io.dropwizard.metrics:metrics-json:4.0.+`
* `org.jooq:jooq:3.11.+`
* `com.zaxxer:HikariCP:3.2.+`
* `org.apache.tomcat:tomcat-jdbc:9.0.+`
* `org.zeroturnaround:zt-exec:1.+`
* `org.apache.logging.log4j:log4j-iostreams:2.11.+`
* `org.tarantool:connector:1.9.+`
* `org.jvnet.staxex:stax-ex:1.7.+`

## Documentation

* [How To](https://github.com/art-community/ART/tree/latest/documentation/how-to-get-started-with-art.md)
* [Examples](https://github.com/art-community/ART/tree/latest/documentation/examples.md)
* [Concepts & motivation](https://github.com/art-community/ART/tree/latest/documentation/concept-&-motivation.md)
* [Use cases](https://github.com/art-community/ART/tree/latest/documentation/use-cases.md)
* [Modules](https://github.com/art-community/ART/tree/latest/documentation/modules-&-capabilities.md)
* [Universal value model](https://github.com/art-community/ART/tree/latest/documentation/universal-value-model.md)
* [Auto configuring](https://github.com/art-community/ART/tree/latest/documentation/agile-auto-configuring.md)
* [Configuration specifications](https://github.com/art-community/ART/tree/latest/documentation/configuration-specifications.md)
* [Public API](https://github.com/art-community/ART/tree/latest/documentation/public-api.md)
* [Constraints & Development](https://github.com/art-community/ART/tree/latest/documentation/constraints-&-development.md)
 

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
