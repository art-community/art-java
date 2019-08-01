# application-module-executor

Модуль предназначен для вызова методов сервиса стороннего модуля по его адресу с возможностью задания реквеста.

## Пример запроса
```json
{
    "moduleHost": "localhost",
    "modulePort": 10001,
    "executableServletPath": "/example",
    "executableServiceId": "EXAMPLE_SERVICE",
    "executableMethodId": "REQUEST_RESPONSE_HANDLING_EXAMPLE",
    "executableRequest" : {
    	"innerModel": {
    		"exampleBooleanField" : true,
    		"exampleIntegerField": 10,
    		"exampleStringField": "exampleString"	
    	},
    	"primitiveTypeInt": 20
    }
}
```

## Пример ответа
 ```json
 {
     "response": {
         "responseMessage": "This is response message"
     }
 }
 ```

Пример в виде проекта для Postman можно скачать [тут](http://10.35.215.200/gitlab/development/application-module-executor/wikis/uploads/3d2c1f4d9b6f4409e598e883fc244e74/Executor.postman_collection.json).