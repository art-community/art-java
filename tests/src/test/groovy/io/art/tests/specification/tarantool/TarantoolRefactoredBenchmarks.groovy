
package io.art.tests.specification.tarantool

import io.art.value.immutable.Entity
import io.art.value.immutable.Value
import io.art.refactored.dao.TarantoolInstance
import io.art.refactored.dao.TarantoolSpace
import io.art.refactored.model.TarantoolUpdateFieldOperation
import spock.lang.Specification


import static io.art.launcher.ModuleLauncher.launch
import static io.art.model.module.ModuleModel.*
import static io.art.refactored.configuration.space.TarantoolSpaceConfig.tarantoolSpaceConfig
import static io.art.refactored.configuration.space.TarantoolSpaceFormat.tarantoolSpaceFormat
import static io.art.refactored.configuration.space.TarantoolSpaceIndex.tarantoolSpaceIndex
import static io.art.refactored.constants.TarantoolModuleConstants.TarantoolFieldType.NUMBER
import static io.art.refactored.constants.TarantoolModuleConstants.TarantoolFieldType.UNSIGNED
import static io.art.refactored.constants.TarantoolModuleConstants.TarantoolIndexType
import static io.art.refactored.module.TarantoolModule.getClient
import static io.art.refactored.module.TarantoolModule.getInstance
import static io.art.refactored.module.TarantoolModule.getSpace
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
        launch module()
    }

    def "Storage1 CRUD(warmup)"() {
        setup:
        def spaceName = "s1_CRUD"
        def clientId = "storage_1_a"

        TarantoolInstance db = getInstance(clientId)
        TarantoolSpace space = getSpace(clientId, spaceName)


        Entity data = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
                .build()
        Value request = longPrimitive(3)

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
                .put("id", intPrimitive(7))
                .put("data", stringPrimitive("testData"))
                .build()
        space.autoIncrement(data)
        then:
        ((space.len() == 5) && (space.schemaLen() == 2))


        when:
        request = intPrimitive(2)
        then:
        space.get(request).isEmpty() && space.select(request).isEmpty()


        when:
        request = intPrimitive(7)
        Entity response = space.select(request).get().get(0) as Entity
        then:
        response == data


        when:
        space.truncate()
        then:
        (space.count() == 0) && (space.schemaCount() == 0)


        when:
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("data", stringPrimitive("another data"))
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
                .put("id", intPrimitive(7))
                .put("data", stringPrimitive("something"))
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

        TarantoolInstance db = getInstance(clientId)
        TarantoolSpace space = getSpace(clientId, spaceName)



        Entity data = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
                .build()
        Entity request = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("bucket_id", intPrimitive(99))
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

        TarantoolInstance db = getInstance(clientId)
        TarantoolSpace space = getSpace(clientId, spaceName)



        Entity data = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
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
        def clientId = "storage_1_a"

        TarantoolInstance db = getInstance(clientId)
        TarantoolSpace space = getSpace(clientId, spaceName)



        Entity data = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
                .build()
        Value request = intPrimitive(3)

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
        def clientId = "storage_2_a"

        TarantoolInstance db = getInstance(clientId)
        TarantoolSpace space = getSpace(clientId, spaceName)



        Entity data = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
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

        def db = getInstance(clientId)
        def space = getSpace(clientId, spaceName)
        def client = getClient(clientId)

        Entity data = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
                .build()
        Value request = intPrimitive(3)
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
            client.call("box.space."+ spaceName +":get", tuple).get()
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

        def db = getInstance(clientId)
        def space = getSpace(clientId, spaceName)
        def client = getClient(clientId)

        Entity data = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
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
            client.call("box.space."+ spaceName +":auto_increment", tuple).get()
        }

        then:
        true


        cleanup:
        db.dropSpace(spaceName)
    }

}
