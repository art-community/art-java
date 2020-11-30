
package io.art.tests.specification.tarantool


import io.art.value.immutable.Entity
import org.tarantool.TarantoolClient
import org.tarantool.TarantoolClusterClientConfig
import io.art.refactored.configuration.TarantoolInstanceConfiguration
import io.art.refactored.configuration.TarantoolModuleConfiguration
import io.art.refactored.dao.TarantoolInstance
import io.art.refactored.dao.TarantoolSpace
import io.art.refactored.model.TarantoolUpdateFieldOperation
import io.art.refactored.module.TarantoolModule
import io.art.refactored.module.connector.TarantoolConnector
import spock.lang.Specification

import java.util.concurrent.Future

import static io.art.launcher.ModuleLauncher.launch
import static io.art.model.implementation.ModuleModel.*
import static io.art.refactored.configuration.space.TarantoolSpaceConfig.tarantoolSpaceConfig
import static io.art.refactored.configuration.space.TarantoolSpaceFormat.tarantoolSpaceFormat
import static io.art.refactored.configuration.space.TarantoolSpaceIndex.tarantoolSpaceIndex
import static io.art.refactored.constants.TarantoolModuleConstants.TarantoolFieldType.NUMBER
import static io.art.refactored.constants.TarantoolModuleConstants.TarantoolFieldType.UNSIGNED
import static io.art.refactored.constants.TarantoolModuleConstants.TarantoolIndexType
import static io.art.value.factory.PrimitivesFactory.intPrimitive
import static io.art.value.factory.PrimitivesFactory.longPrimitive
import static io.art.value.factory.PrimitivesFactory.stringPrimitive
import static io.art.value.tuple.PlainTupleWriter.*

class TarantoolRefactoredBenchmarks extends Specification {
    def benchmarkOpsCount = 10000
    def router1Address = "localhost:3311"
    def router2Address = "localhost:3312"
    def storage1Address = "localhost:3301"
    def storage2Address = "localhost:3302"
    def username = 'username'
    def password = 'password'

    def "start modules"() {
        setup:
        launch module().make()
    }

    def "Storage1 CRUD(warmup)"() {
        setup:
        def spaceName = "s1_CRUD"
        def clientId = "storage_1"
        TarantoolClusterClientConfig clientConfig = new TarantoolClusterClientConfig()
        clientConfig.username = username
        clientConfig.password = password
        clientConfig.connectionTimeout = 5 * 1000

        TarantoolInstanceConfiguration instanceConfig = new TarantoolInstanceConfiguration.TarantoolInstanceConfigurationBuilder()
                .address(storage1Address)
                .config(clientConfig)
                .build()

        TarantoolModuleConfiguration moduleConfig = new TarantoolModuleConfiguration()
        moduleConfig.instances.put(clientId, instanceConfig)

        TarantoolModule tnt = new TarantoolModule(moduleConfig)
        TarantoolInstance db = tnt.getInstance(clientId)
        TarantoolSpace space = tnt.getSpace(clientId, spaceName)


        Entity data = Entity.entityBuilder()
                .put("id", 3)
                .put("data", "testData")
                .put("anotherData", "another data")
                .build()
        Entity request = Entity.entityBuilder().put("id", longPrimitive(3)).build()

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .addField("id", NUMBER, false))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .id(0)
                .part("id")
                .ifNotExists(true)
                .unique(true))



        when:
        space.insert(data)
        then:
        space.get(request).get() == data


        when:
        space.autoIncrement(data)
        space.autoIncrement(data)
        space.autoIncrement(data)
        db.renameSpace(spaceName, spaceName = "s1_CRUD2")
        space = tnt.getSpace(clientId, spaceName)
        data = Entity.entityBuilder()
                .put("id", 7)
                .put("data", "testData")
                .build()
        space.autoIncrement(data)
        then:
        ((space.len() == 5) && (space.schemaLen() == 2))


        when:
        request = Entity.entityBuilder().put("id", 2).build()
        then:
        space.get(request).isEmpty() && space.select(request).isEmpty()


        when:
        request = Entity.entityBuilder().put("id", 7).build()
        Entity response = space.select(request).get().get(0) as Entity
        then:
        response == data


        when:
        space.truncate()
        then:
        (space.count() == 0) && (space.schemaCount() == 0)


        when:
        data = Entity.entityBuilder()
                .put("id", 7)
                .put("data", "another data")
                .build()
        space.put(data)
        then:
        space.get(request).get() == data


        when:
        space.delete(intPrimitive(7))
        then:
        space.get(request).isEmpty()


        when:
        space.put(data)
        space.update(intPrimitive(7),
                TarantoolUpdateFieldOperation.assigment(2, 'data', stringPrimitive("another")))
        then:
        ((Entity)space.get(request).get()).getString("data") == "another"

        when:
        space.put(data)
        data = Entity.entityBuilder()
                .put("id", 7)
                .put("data", "something")
                .build()
        space.replace(data)
        then:
        space.get(intPrimitive(7)).get() == data

        when:
        space.upsert(data, TarantoolUpdateFieldOperation.addition(1, 1))
        space.upsert(data, TarantoolUpdateFieldOperation.addition(1, 1))
        then:
        space.get(intPrimitive(7)).isPresent() && space.get(intPrimitive(8)).isPresent()


        cleanup:
        db.dropSpace(spaceName)


    }

    def "Router1 art.get benchmark"(){
        setup:
        def clientId = "router_1"
        def spaceName = "r1_art_get_bench"

        TarantoolClusterClientConfig clientConfig = new TarantoolClusterClientConfig()
        clientConfig.username = username
        clientConfig.password = password
        clientConfig.connectionTimeout = 5 * 1000

        TarantoolInstanceConfiguration instanceConfig = new TarantoolInstanceConfiguration.TarantoolInstanceConfigurationBuilder()
                .address(router1Address)
                .config(clientConfig)
                .build()

        TarantoolModuleConfiguration moduleConfig = new TarantoolModuleConfiguration()
        moduleConfig.instances.put(clientId, instanceConfig)

        TarantoolModule tnt = new TarantoolModule(moduleConfig)
        TarantoolInstance db = tnt.getInstance(clientId)
        TarantoolSpace space = tnt.getSpace(clientId, spaceName)



        Entity data = Entity.entityBuilder()
                .put("id", 3)
                .put("bucket_id", 99)
                .put("data", "testData")
                .put("anotherData", "another data")
                .build()
        Entity request = Entity.entityBuilder()
                .put("id", 3)
                .put("bucket_id", 99)
                .build()

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .addField("id", UNSIGNED, false)
                .addField("bucket_id", UNSIGNED))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .part("id")
                .ifNotExists(true)
                .unique(true))
        db.createIndex(spaceName, 'bucket_id', tarantoolSpaceIndex()
                .part(2)
                .unique(false))

        when:
        space.insert(data)

        for (int i = 0; i<benchmarkOpsCount; i++){
            space.get(request)
        }
        then:
        true

        cleanup:
        db.dropSpace(spaceName)
    }

    def "Router2 art.auto_increment benchmark"(){
        setup:
        def clientId = "router_2"
        def spaceName = "r2_art_inc_bench"

        TarantoolClusterClientConfig clientConfig = new TarantoolClusterClientConfig()
        clientConfig.username = username
        clientConfig.password = password
        clientConfig.connectionTimeout = 5 * 1000

        TarantoolInstanceConfiguration instanceConfig = new TarantoolInstanceConfiguration.TarantoolInstanceConfigurationBuilder()
                .address(router2Address)
                .config(clientConfig)
                .build()

        TarantoolModuleConfiguration moduleConfig = new TarantoolModuleConfiguration()
        moduleConfig.instances.put(clientId, instanceConfig)

        TarantoolModule tnt = new TarantoolModule(moduleConfig)
        TarantoolInstance db = tnt.getInstance(clientId)
        TarantoolSpace space = tnt.getSpace(clientId, spaceName)



        Entity data = Entity.entityBuilder()
                .put("id", 3)
                .put("bucket_id", 99)
                .put("data", "testData")
                .put("anotherData", "another data")
                .build()

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .addField("id", UNSIGNED, false)
                .addField("bucket_id", UNSIGNED))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .part("id")
                .ifNotExists(true)
                .unique(true))
        db.createIndex(spaceName, 'bucket_id', tarantoolSpaceIndex()
                .part(2)
                .unique(false))

        when:

        for (int i = 0; i<benchmarkOpsCount; i++){
            space.autoIncrement(data)
        }
        then:
        true

        cleanup:
        db.dropSpace(spaceName)
    }

    def "Storage1 art.get benchmark"(){
        setup:
        def spaceName = "s1_art_get_bench"
        def clientId = "storage_1"
        TarantoolClusterClientConfig clientConfig = new TarantoolClusterClientConfig()
        clientConfig.username = username
        clientConfig.password = password
        clientConfig.connectionTimeout = 5 * 1000

        TarantoolInstanceConfiguration instanceConfig = new TarantoolInstanceConfiguration.TarantoolInstanceConfigurationBuilder()
                .address(storage1Address)
                .config(clientConfig)
                .build()

        TarantoolModuleConfiguration moduleConfig = new TarantoolModuleConfiguration()
        moduleConfig.instances.put(clientId, instanceConfig)

        TarantoolModule tnt = new TarantoolModule(moduleConfig)
        TarantoolInstance db = tnt.getInstance(clientId)
        TarantoolSpace space = tnt.getSpace(clientId, spaceName)



        Entity data = Entity.entityBuilder()
                .put("id", 3)
                .put("data", "testData")
                .put("anotherData", "another data")
                .build()
        Entity request = Entity.entityBuilder().put("id", 3).build()

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .addField("id", NUMBER, false))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .id(0)
                .part("id")
                .ifNotExists(true)
                .unique(true))

        when:
        space.insert(data)

        for (int i = 0; i<benchmarkOpsCount; i++){
            space.get(request)
        }
        then:
        true

        cleanup:
        db.dropSpace(spaceName)
    }

    def "Storage2 art.auto_increment benchmark"(){
        setup:
        def spaceName = "s2_art_inc_bench"
        def clientId = "storage_1"
        TarantoolClusterClientConfig clientConfig = new TarantoolClusterClientConfig()
        clientConfig.username = username
        clientConfig.password = password
        clientConfig.connectionTimeout = 5 * 1000

        TarantoolInstanceConfiguration instanceConfig = new TarantoolInstanceConfiguration.TarantoolInstanceConfigurationBuilder()
                .address(storage2Address)
                .config(clientConfig)
                .build()

        TarantoolModuleConfiguration moduleConfig = new TarantoolModuleConfiguration()
        moduleConfig.instances.put(clientId, instanceConfig)

        TarantoolModule tnt = new TarantoolModule(moduleConfig)
        TarantoolInstance db = tnt.getInstance(clientId)
        TarantoolSpace space = tnt.getSpace(clientId, spaceName)



        Entity data = Entity.entityBuilder()
                .put("id", 3)
                .put("data", "testData")
                .put("anotherData", "another data")
                .build()

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .addField("id", NUMBER, false))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .id(0)
                .part("id")
                .ifNotExists(true)
                .unique(true))

        when:
        space.insert(data)

        for (int i = 0; i<benchmarkOpsCount; i++){
            space.autoIncrement(data)
        }
        then:
        true

        cleanup:
        db.dropSpace(spaceName)
    }

    def "Storage1 box.get benchmark"(){
        setup:
        def spaceName = "s1_box_get_bench"
        def clientId = "storage_1"
        TarantoolClusterClientConfig clientConfig = new TarantoolClusterClientConfig()
        clientConfig.username = username
        clientConfig.password = password
        clientConfig.connectionTimeout = 5 * 1000

        TarantoolInstanceConfiguration instanceConfig = new TarantoolInstanceConfiguration.TarantoolInstanceConfigurationBuilder()
                .address(storage1Address)
                .config(clientConfig)
                .build()

        TarantoolModuleConfiguration moduleConfig = new TarantoolModuleConfiguration()
        moduleConfig.instances.put(clientId, instanceConfig)

        TarantoolModule tnt = new TarantoolModule(moduleConfig)
        TarantoolInstance db = tnt.getInstance(clientId)
        TarantoolSpace space = tnt.getSpace(clientId, spaceName)
        TarantoolClient client = TarantoolConnector.connect(clientId, instanceConfig)

        Entity data = Entity.entityBuilder()
                .put("id", 3)
                .put("data", "testData")
                .put("anotherData", "another data")
                .build()
        Entity request = Entity.entityBuilder().put("id", 3).build()
        List<?> tuple = writeTuple(request).getTuple()

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .addField("id", NUMBER, false))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .id(0)
                .part("id")
                .ifNotExists(true)
                .unique(true))

        when:
        space.insert(data)
        for (int i = 0; i<benchmarkOpsCount; i++){
            client.syncOps().call("box.space."+ spaceName +":get", tuple)
        }

        then:
        true


        cleanup:
        db.dropSpace(spaceName)
    }

    def "Storage2 box.auto_increment benchmark"(){
        setup:
        def spaceName = "s2_box_inc_bench"
        def clientId = "storage_2"
        TarantoolClusterClientConfig clientConfig = new TarantoolClusterClientConfig()
        clientConfig.username = username
        clientConfig.password = password
        clientConfig.connectionTimeout = 5 * 1000

        TarantoolInstanceConfiguration instanceConfig = new TarantoolInstanceConfiguration.TarantoolInstanceConfigurationBuilder()
                .address(storage2Address)
                .config(clientConfig)
                .build()

        TarantoolModuleConfiguration moduleConfig = new TarantoolModuleConfiguration()
        moduleConfig.instances.put(clientId, instanceConfig)

        TarantoolModule tnt = new TarantoolModule(moduleConfig)
        TarantoolInstance db = tnt.getInstance(clientId)
        TarantoolClient client = TarantoolConnector.connect(clientId, instanceConfig)

        Entity data = Entity.entityBuilder()
                .put("id", 3)
                .put("data", "testData")
                .put("anotherData", "another data")
                .build()
        List<?> tuple = writeTuple(data).getTuple()

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .addField("id", NUMBER, false))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .id(0)
                .part("id")
                .ifNotExists(true)
                .unique(true))

        when:
        for (int i = 0; i<benchmarkOpsCount; i++){
            client.syncOps().call("box.space."+ spaceName +":auto_increment", tuple)
        }

        then:
        true


        cleanup:
        db.dropSpace(spaceName)
    }

    def "art-platform.io docker box.auto_increment async benchmark"(){
        setup:
        def spaceName = "s2_box_inc_async_bench"
        def clientId = "storage_2"
        TarantoolClusterClientConfig clientConfig = new TarantoolClusterClientConfig()
        //clientConfig.username = username
        //clientConfig.password = password
        clientConfig.connectionTimeout = 5 * 1000

        TarantoolInstanceConfiguration instanceConfig = new TarantoolInstanceConfiguration.TarantoolInstanceConfigurationBuilder()
                .address("art-platform.io:3302")
                .config(clientConfig)
                .build()

        TarantoolModuleConfiguration moduleConfig = new TarantoolModuleConfiguration()
        moduleConfig.instances.put(clientId, instanceConfig)

        TarantoolModule tnt = new TarantoolModule(moduleConfig)
        TarantoolInstance db = tnt.getInstance(clientId)
        TarantoolClient client = TarantoolConnector.connect(clientId, instanceConfig)


        Entity data = Entity.entityBuilder()
                .put("id", 3)
                .put("data", "testData")
                .put("anotherData", "another data")
                .build()
        List<?> tuple = writeTuple(data).getTuple()

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .addField("id", NUMBER, false))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .id(0)
                .part("id")
                .ifNotExists(true)
                .unique(true))



        List<Future<?>> results = new LinkedList<Future<?>>()
        when:
        for (int j = 0; j< benchmarkOpsCount/1000; j++) {
            for (int i = 0; i < 1000; i++) {
                results.push(client.asyncOps().call("box.space." + spaceName + ":auto_increment", tuple))
            }
            for (int i = 0; i < 1000; i++) {
                results.get(i).get()
            }
            results.clear()
        }
        then:
        true


        cleanup:
        db.dropSpace(spaceName)
    }

    def "art-platform.io docker art.auto_increment async benchmark"(){
        setup:
        def spaceName = "s2_art_inc_async_bench"
        def clientId = "storage_2"
        TarantoolClusterClientConfig clientConfig = new TarantoolClusterClientConfig()
        //clientConfig.username = username
        //clientConfig.password = password
        clientConfig.connectionTimeout = 5 * 1000

        TarantoolInstanceConfiguration instanceConfig = new TarantoolInstanceConfiguration.TarantoolInstanceConfigurationBuilder()
                .address("art-platform.io:3302")
                .config(clientConfig)
                .build()

        TarantoolModuleConfiguration moduleConfig = new TarantoolModuleConfiguration()
        moduleConfig.instances.put(clientId, instanceConfig)

        TarantoolModule tnt = new TarantoolModule(moduleConfig)
        TarantoolInstance db = tnt.getInstance(clientId)
        TarantoolClient client = TarantoolConnector.connect(clientId, instanceConfig)


        Entity data = Entity.entityBuilder()
                .put("id", 3)
                .put("data", "testData")
                .put("anotherData", "another data")
                .build()
        List<?> tuple = writeTuple(data).getTuple()

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .addField("id", NUMBER, false))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .id(0)
                .part("id")
                .ifNotExists(true)
                .unique(true))
        List<Future<?>> results = new LinkedList<Future<?>>()


        when:
        for (int j = 0; j< benchmarkOpsCount/1000; j++) {
            for (int i = 0; i < 1000; i++) {
                results.push(client.asyncOps().call("art.auto_increment", spaceName, tuple))
            }
            for (int i = 0; i < 1000; i++) {
                results.get(i).get()
            }
            results.clear()
        }

        then:
        true


        cleanup:
        db.dropSpace(spaceName)
    }
}
