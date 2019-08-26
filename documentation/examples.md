# ART Examples

## Value & POJO
Value is a universal data model for representing data as JSON/XML/Protobuf/Tuple or yore custom data format.

For optimization ART ***is not*** using reflection for serialization and deserialization POJOs to/from Value.

Instead of it ART providing ValueMapping API and ValueMapping Generator (from ART application-gradle-plugin).

Code:
```java
import lombok.*;
import ru.art.entity.*;
import ru.art.entity.mapper.ValueFromModelMapper.*;
import ru.art.entity.mapper.ValueToModelMapper.*;
import java.util.*;

import static ru.art.entity.Entity.*;

public class MainModule {
    @Getter
    @Builder
    @ToString
    private static class Customer {
        private final String id;
        private String name;
        @Singular
        private List<String> orderIds;
    }

    public static void main(String[] args) {
        EntityFromModelMapper<Customer> fromCustomer = customer -> entityBuilder()
                .stringField("id", customer.getId())
                .stringField("name", customer.getName())
                .stringCollectionField("orderIds", customer.getOrderIds())
                .build();
        EntityToModelMapper<Customer> toCustomer = entity -> Customer.builder()
                .id(entity.getString("id"))
                .name(entity.getString("name"))
                .orderIds(entity.getStringList("orderIds"))
                .build();
        Customer customer = Customer.builder().id("1").name("Customer").orderId("1").orderId("2").build();
        System.out.println("Customer = " + customer);
        Entity entity = fromCustomer.map(customer);
        System.out.println("Customer entity from customer = " + entity);
        System.out.println("Customer entity as JSON = " + writeJson(entity));
        System.out.println("Customer entity as Protobuf = " + writeProtobuf(entity));
        System.out.println("Customer entity to customer = " + toCustomer.map(entity));
    }
}
```

After running in output console you could see something like it:
```
Customer entity as JSON = {"id":"1","name":"Customer","orderIds":["1","2"]}
Customer entity as Protobuf = value {
  type_url: "type.googleapis.com/ru.art.protobuf.entity.ProtobufEntity"
  value: "\nI\n\004name\022A\n=\n/type.googleapis.com/google.protobuf.StringValue\022\n\n\bCustomer\020\005\n\315\001\n\borderIds\022\300\001\n\273\001\n=type.googleapis.com/ru.art.protobuf.entity.ProtobufCollection\022z\b\005\022:\n6\n/type.googleapis.com/google.protobuf.StringValue\022\003\n\0011\020\005\022:\n6\n/type.googleapis.com/google.protobuf.StringValue\022\003\n\0012\020\005\020\003\n@\n\002id\022:\n6\n/type.googleapis.com/google.protobuf.StringValue\022\003\n\0011\020\005"
}
valueType: ENTITY
Customer entity to customer = MainModule.Customer(id=1, name=Customer, orderIds=[1, 2])
```

## HTTP Serving
ART providing you functional to serving HTTP requests

```java
import static ru.art.config.extensions.activator.AgileConfigurationsActivator.*;
import static ru.art.entity.PrimitiveMapping.StringPrimitive.*;
import static ru.art.entity.StringParametersMapping.*;
import static ru.art.http.server.HttpServer.httpServer;
import static ru.art.http.server.function.HttpServiceFunction.*;

public class MainModule {
    public static void main(String[] args) {
        useAgileConfigurations(MainModule.class.getName());
        httpGet("/hello")
                .fromPathParameters("param")
                .requestMapper(stringParameterToStringMapper("param").getToModel())
                .responseMapper(fromModel)
                .handle(requestParam -> "<h1>" + requestParam + "</h1>");
        httpServer().await();
    }
}
```

After running this code and open browser on url with path '/hello' from logs you could see something like this:

<<screen>>

## HTTP Communication
ART providing you functional for sending HTTP request

```java
import static ru.art.config.extensions.activator.AgileConfigurationsActivator.*;
import static ru.art.entity.PrimitiveMapping.StringPrimitive.*;
import static ru.art.http.client.communicator.HttpCommunicator.*;

public class MainModule {
    public static void main(String[] args) {
        useAgileConfigurations(MainModule.class.getName());
        httpCommunicator("http://example.com/")
                .responseMapper(toModel)
                .execute()
                .ifPresent(System.out::println);
    }
}
```

After running this code you could see something like this:
```html
<!doctype html>
<html>
<head>
    <title>Example Domain</title>

    <meta charset="utf-8" />
    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <style type="text/css">
    body {
        background-color: #f0f0f2;
        margin: 0;
        padding: 0;
        font-family: "Open Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
        
    }
    div {
        width: 600px;
        margin: 5em auto;
        padding: 50px;
        background-color: #fff;
        border-radius: 1em;
    }
    a:link, a:visited {
        color: #38488f;
        text-decoration: none;
    }
    @media (max-width: 700px) {
        body {
            background-color: #fff;
        }
        div {
            width: auto;
            margin: 0 auto;
            border-radius: 0;
            padding: 1em;
        }
    }
    </style>    
</head>

<body>
<div>
    <h1>Example Domain</h1>
    <p>This domain is established to be used for illustrative examples in documents. You may use this
    domain in examples without prior coordination or asking for permission.</p>
    <p><a href="http://www.iana.org/domains/example">More information...</a></p>
</div>
</body>
</html>
```

## GRPC Serving

## GRPC Communication

## RSocket Serving

## RSocket Communication

## Rocks DB

## Kafka Embedded Broker

## Kafka Clients: stream & producer

## Local Scheduler

## Tarantool DB

## Configurations
