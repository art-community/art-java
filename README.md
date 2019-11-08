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
    id("io.github.art.project") version "1.0.94"
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
- Java 8+ (9, 10, 11, 12)
- Gradle 5+
- If working with web - installed nodejs and npm
- If using tarantool on Windows - installed WSL (Tarantool is not supported on Windows System)
- If using sql - installed SQL db

## Bugs and Feedback
For bugs, questions and discussions please use the [Github Issues](https://github.com/art-community/art/issues).

## Dependency libraries

* `com.101tec:zkclient:0.11`
* `com.auth0:java-jwt:3.8.3`
* `com.fasterxml.jackson.core:jackson-annotations:2.10.0`
* `com.fasterxml.jackson.core:jackson-core:2.10.0`
* `com.fasterxml.jackson.core:jackson-databind:2.10.0`
* `com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.10.0`
* `com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.10.0`
* `com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.10.0`
* `com.fasterxml.jackson.jaxrs:jackson-jaxrs-base:2.9.8`
* `com.fasterxml.jackson.jaxrs:jackson-jaxrs-json-provider:2.9.8`
* `com.fasterxml.jackson.module:jackson-module-jaxb-annotations:2.9.8`
* `com.fasterxml.jackson.module:jackson-module-paranamer:2.10.0`
* `com.fasterxml.jackson.module:jackson-module-scala_2.12:2.10.0`
* `com.github.docker-java:docker-java:3.1.5`
* `com.github.luben:zstd-jni:1.4.0-1`
* `com.github.spotbugs:spotbugs-annotations:3.1.9`
* `com.google.android:annotations:4.1.1.4`
* `com.google.api-client:google-api-client:1.21.0`
* `com.google.api.grpc:proto-google-common-protos:1.12.0`
* `com.google.code.findbugs:jsr305:1.3.9`
* `com.google.code.findbugs:jsr305:3.0.2`
* `com.google.code.gson:gson:2.7`
* `com.google.errorprone:error_prone_annotations:2.3.2`
* `com.google.errorprone:error_prone_annotations:2.3.3`
* `com.google.guava:failureaccess:1.0.1`
* `com.google.guava:guava:28.1-jre`
* `com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava`
* `com.google.http-client:google-http-client-jackson2:1.21.0`
* `com.google.http-client:google-http-client:1.21.0`
* `com.google.j2objc:j2objc-annotations:1.3`
* `com.google.oauth-client:google-oauth-client:1.21.0`
* `com.google.protobuf:protobuf-java:3.9.0`
* `com.googlecode.jatl:jatl:0.2.2`
* `com.googlecode.javaewah:JavaEWAH:1.1.6`
* `com.jcraft:jsch:0.1.55`
* `com.jcraft:jzlib:1.1.1`
* `com.kohlschutter.junixsocket:junixsocket-common:2.2.0`
* `com.kohlschutter.junixsocket:junixsocket-native-common:2.2.0`
* `com.oracle:ojdbc6:11.2.0.4.0-atlassian-hosted`
* `com.predic8:soa-model-core:1.6.0`
* `com.predic8:xmlbeautifier:1.2.2`
* `com.squareup:javapoet:1.11.1`
* `com.sun.activation:javax.activation:1.2.0`
* `com.thoughtworks.paranamer:paranamer:2.8`
* `com.typesafe.scala-logging:scala-logging_2.12:3.9.0`
* `com.typesafe:config:1.4.0`
* `com.yammer.metrics:metrics-core:2.2.0`
* `com.zaxxer:HikariCP:3.4.1`
* `commons-codec:commons-codec:1.11`
* `commons-codec:commons-codec:1.12`
* `commons-io:commons-io:2.4`
* `commons-io:commons-io:2.6`
* `commons-lang:commons-lang:2.5`
* `commons-lang:commons-lang:2.6`
* `commons-logging:commons-logging:1.2`
* `fi.solita.clamav:clamav-client:1.0.1`
* `io.dropwizard.metrics:metrics-core:3.1.2`
* `io.dropwizard.metrics:metrics-jvm:4.0.7`
* `io.dropwizard:dropwizard-db:1.3.16`
* `io.github.classgraph:classgraph:4.8.52`
* `io.github.mweirauch:micrometer-jvm-extras:0.1.4`
* `io.github.resilience4j:resilience4j-bulkhead:1.1.1-SNAPSHOT`
* `io.github.resilience4j:resilience4j-circuitbreaker:1.1.1-SNAPSHOT`
* `io.github.resilience4j:resilience4j-core:1.1.1-SNAPSHOT`
* `io.github.resilience4j:resilience4j-metrics:1.1.1-SNAPSHOT`
* `io.github.resilience4j:resilience4j-ratelimiter:1.1.1-SNAPSHOT`
* `io.github.resilience4j:resilience4j-retry:1.1.1-SNAPSHOT`
* `io.github.resilience4j:resilience4j-timelimiter:1.1.1-SNAPSHOT`
* `io.grpc:grpc-api:1.24.0`
* `io.grpc:grpc-context:1.24.0`
* `io.grpc:grpc-core:1.24.0`
* `io.grpc:grpc-netty-shaded:1.24.0`
* `io.grpc:grpc-netty:1.24.0`
* `io.grpc:grpc-protobuf-lite:1.24.0`
* `io.grpc:grpc-protobuf:1.24.0`
* `io.grpc:grpc-stub:1.24.0`
* `io.micrometer:micrometer-core:1.3.1`
* `io.micrometer:micrometer-registry-prometheus:1.3.1`
* `io.netty:netty-all:4.1.38.Final`
* `io.netty:netty-all:4.1.42.Final`
* `io.opencensus:opencensus-api:0.21.0`
* `io.opencensus:opencensus-contrib-grpc-metrics:0.21.0`
* `io.pebbletemplates:pebble:3.1.0`
* `io.perfmark:perfmark-api:0.17.0`
* `io.projectreactor.netty:reactor-netty:0.8.8.RELEASE`
* `io.projectreactor:reactor-core:3.3.0.RELEASE`
* `io.prometheus:simpleclient:0.7.0`
* `io.prometheus:simpleclient_common:0.7.0`
* `io.prometheus:simpleclient_dropwizard:0.7.0`
* `io.rsocket:rsocket-core:0.12.2-RC4`
* `io.rsocket:rsocket-transport-netty:0.12.2-RC4`
* `io.vavr:vavr-match:0.10.2`
* `io.vavr:vavr:0.10.2`
* `javax.annotation:com.springsource.javax.annotation:1.0.0`
* `javax.xml.bind:jaxb-api:2.3.0`
* `net.sf.jopt-simple:jopt-simple:5.0.4`
* `org.apache.commons:commons-compress:1.18`
* `org.apache.httpcomponents:httpasyncclient:4.1.4`
* `org.apache.httpcomponents:httpclient:4.5.10`
* `org.apache.httpcomponents:httpcore-nio:4.4.10`
* `org.apache.httpcomponents:httpcore:4.4.12`
* `org.apache.kafka:connect-api:2.3.1`
* `org.apache.kafka:connect-json:2.3.1`
* `org.apache.kafka:kafka-clients:2.3.1`
* `org.apache.kafka:kafka-log4j-appender:2.3.1`
* `org.apache.kafka:kafka-streams:2.3.1`
* `org.apache.kafka:kafka_2.12:2.3.1`
* `org.apache.logging.log4j:log4j-api:2.12.1`
* `org.apache.logging.log4j:log4j-core:2.12.1`
* `org.apache.logging.log4j:log4j-iostreams:2.12.1`
* `org.apache.logging.log4j:log4j-jcl:2.12.1`
* `org.apache.logging.log4j:log4j-jul:2.12.1`
* `org.apache.logging.log4j:log4j-slf4j-impl:2.12.1`
* `org.apache.logging.log4j:log4j-web:2.12.1`
* `org.apache.tomcat.embed:tomcat-embed-core:9.0.27`
* `org.apache.tomcat:tomcat-annotations-api:9.0.27`
* `org.apache.tomcat:tomcat-jdbc:9.0.27`
* `org.apache.tomcat:tomcat-juli:9.0.27`
* `org.apache.tomcat:tomcat-servlet-api:9.0.27`
* `org.apache.yetus:audience-annotations:0.5.0`
* `org.apache.zookeeper:zookeeper:3.4.14`
* `org.apiguardian:apiguardian-api:1.0.0`
* `org.bitbucket.b_c:jose4j:0.4.4`
* `org.bouncycastle:bcpg-jdk15on:1.61`
* `org.bouncycastle:bcpkix-jdk15on:1.54`
* `org.bouncycastle:bcpkix-jdk15on:1.60`
* `org.bouncycastle:bcpkix-jdk15on:1.61`
* `org.bouncycastle:bcprov-jdk15on:1.54`
* `org.bouncycastle:bcprov-jdk15on:1.60`
* `org.bouncycastle:bcprov-jdk15on:1.61`
* `org.checkerframework:checker-qual:2.8.1`
* `org.codehaus.groovy:groovy-json:2.4.7`
* `org.codehaus.groovy:groovy-templates:2.4.12`
* `org.codehaus.groovy:groovy-xml:2.4.12`
* `org.codehaus.groovy:groovy:2.4.12`
* `org.codehaus.mojo:animal-sniffer-annotations:1.17`
* `org.codehaus.mojo:animal-sniffer-annotations:1.18`
* `org.eclipse.jgit:org.eclipse.jgit:5.5.1.201910021850-r`
* `org.hdrhistogram:HdrHistogram:2.1.11`
* `org.jeasy:easy-random-core:4.1.0`
* `org.jeasy:easy-random:4.1.0`
* `org.jetbrains.kotlin:kotlin-stdlib-common:1.3.31`
* `org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.31`
* `org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.31`
* `org.jetbrains.kotlin:kotlin-stdlib:1.3.31`
* `org.jetbrains:annotations:13.0`
* `org.jooq:jooq:3.12.3`
* `org.jvnet.staxex:stax-ex:1.8.2`
* `org.latencyutils:LatencyUtils:2.0.3`
* `org.lz4:lz4-java:1.6.0`
* `org.membrane-soa:service-proxy-annot:4.6.1`
* `org.membrane-soa:service-proxy-core:4.6.1`
* `org.msgpack:msgpack-core:0.8.18`
* `org.objenesis:objenesis:3.1`
* `org.projectlombok:lombok:1.18.10`
* `org.reactivestreams:reactive-streams:1.0.2`
* `org.reactivestreams:reactive-streams:1.0.3`
* `org.rocksdb:rocksdbjni:5.18.3`
* `org.rocksdb:rocksdbjni:6.3.6`
* `org.scala-lang:scala-library:2.12.10`
* `org.scala-lang:scala-reflect:2.12.8`
* `org.slf4j:slf4j-api:1.7.26`
* `org.slf4j:slf4j-api:1.7.28`
* `org.tarantool:connector:1.9.2`
* `org.unbescape:unbescape:1.1.6.RELEASE`
* `org.xerial.snappy:snappy-java:1.1.7.3`
* `org.yaml:snakeyaml:1.24`
* `org.zalando:faux-pas:0.8.0`
* `org.zalando:logbook-api:1.13.0`
* `org.zalando:logbook-core:1.13.0`
* `org.zalando:logbook-httpclient:1.13.0`
* `org.zalando:logbook-servlet:1.13.0`
* `org.zeroturnaround:zt-exec:1.11`

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
