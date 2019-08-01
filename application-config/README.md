# ADK Модуль "application-config"

## Назначение модуля
Данный модуль является агрегатором всех модулей по работе с конфигами. К ним относятся:
* application-config-groovy
* application-config-typeface
* application-config-yaml
* application-config-remote

## Основная функция модуля:
Универсальная загрузка и кеширование конфига. 

Чтобы загрузить конфиг достаточно вызвать один из методов класса `ConfigProvider`. 
Провайдер обратится к кешу конфига. В случае налия конфига в кеше провайдер вернет ссылку на объект `Config`. Иначе - выполнит загрузку конфига через `ConfigLoader`.
Объект `Config` содержит непосредственно сам конфиг в виде `Object`, а также тип конфига. 

Конфиги бывают:
 1. PROPERTIES - Java проперти.
 Пример: 
    
     
        SOME_INT_VALUE = 2
        SOME_DOUBLE_VALUE = 1.2
        SOME_STRING_VALUE = foo
        SOME_INT_ARRAY = 1;2;3
 2. JSON - всеми "любимый" JSON.
Пример: 

         
         "crm-balance-module": {  
         "protobuf.servlet.path": "ru.rti.crm.protobuf.servlet.CrmBalanceServlet",
           "http.server.port": 11001,
           "protobuf.server.port": 12001,
           "http.servlet.path": "/crm-balance-module",
           "db.url": "jdbc:oracle:thin:@//10.28.43.13:1521/MSKDEV3",
           "db.login": "sa",
           "db.password": "sa"
         }
 
3. HOCON - улучшенный JSON (спасибо, Scala :) )
 Пример:
            
            player: { 
                name: "Steve",
                level: 30
            }
 

 4. GROOVY - для самых отчаянных 
 Пример:
        
        environments {
            dev {
                mail.hostname = 'local'
            }
            test {
                mail.hostname = 'test'
            }
            prod {
                mail.hostname = 'prod'
            }
        }
 
 5. REMOTE_ENTITY_CONFIG - конфиг из удаленного конфигуратора (прилетает и хранится в виде `Entity`).
 Пример: см. Класс `ru.adk.entity.Entity`
 6. YAML - мой фаворит))
 Пример:
 
    
    rocks:
      db:
        path: rocks.db
    configurator:
      http:
        port: 11000
      protobuf:
        port: 11001
     

## Особености конфигурации модуля
В конфигурации хранится тип конфига "по умолчанию". 

То есть, в случае если в модуль будут поступать "запросы" на конфиг без указания типа, то тип он возьмет из конфигурации.
Дополнительно, конфигурация содержит частоту обновления кеша "cacheUpdateIntervalSeconds". По умолчанию - каждые 30 секнд.

## Про модули-загрузчики
Модули: "application-config-groovy", "application-config-yaml", "application-config-typeface" являются обертками над соответствующими библиотеками.

Из примечательного стоит упомянуть, что модули загружают конфиг по следующему принципу:

1. Попробовали взять конфиг из файла, путь к которому задается через системную пропертю "module.config.file"
2. Получилось? Круто! Не получилось - берем конфиг с именем "module-config.yml" из classpath модуля
3. Не получилось? Роняем модуль с эксепшоном ^^

## Что можно почитать про конфиги?
* [Про Groovy](https://habr.com/post/122127/) + [Про Groovy Config](http://mrhaki.blogspot.com/2009/10/groovy-goodness-using-configslurper.html)
* [Про JSON](https://gist.github.com/ermakovpetr/4c9f56d48e49d822705a)
* [Про HOCON](https://habr.com/company/mailru/blog/306848/)
* [Про YAML](https://www.opennet.ru/base/dev/yaml.txt.html)
