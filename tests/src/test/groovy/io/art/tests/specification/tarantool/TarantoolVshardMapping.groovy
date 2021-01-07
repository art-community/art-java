
package io.art.tests.specification.tarantool

import io.art.tarantool.dao.TarantoolAsynchronousSpace
import io.art.tarantool.dao.TarantoolInstance
import io.art.tarantool.dao.TarantoolSpace
import io.art.tarantool.model.TarantoolUpdateFieldOperation
import io.art.value.immutable.Entity
import io.art.value.immutable.Value
import spock.lang.Specification

import static io.art.launcher.ModuleLauncher.launch
import static io.art.model.configurator.ModuleModelConfigurator.module
import static io.art.tarantool.configuration.space.TarantoolSpaceConfig.tarantoolSpaceConfig
import static io.art.tarantool.configuration.space.TarantoolSpaceFormat.tarantoolSpaceFormat
import static io.art.tarantool.configuration.space.TarantoolSpaceIndex.tarantoolSpaceIndex
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.UNSIGNED
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolIndexType
import static io.art.tarantool.module.TarantoolModule.*
import static io.art.value.factory.PrimitivesFactory.intPrimitive
import static io.art.value.factory.PrimitivesFactory.stringPrimitive

class TarantoolVshardMapping extends Specification {
    def benchmarkOpsCount = 10000
    def mappingTimeout = 300

    def setupSpec(){
        launch module().configure()
    }

    def "Router1 CRUD"() {
        setup:
        def clientId = "router_1"
        def spaceName = "r1_crud"


        TarantoolInstance db = tarantoolInstance(clientId)
        TarantoolSpace space = db.space(spaceName)



        Entity data = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
                .build()
        Value request = intPrimitive(3)

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .field("id", UNSIGNED, false)
                .field("bucket_id", UNSIGNED))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .part("id")
                .ifNotExists(true)
                .unique(true))
        db.createIndex(spaceName, 'bucket_id', tarantoolSpaceIndex()
                .part(2)
                .unique(false))


        when:
        def spaces = db.listSpaces()
        def indices = space.listIndices()
        then:
        spaces.contains(spaceName) && indices.contains("primary")


        when:
        space.insert(data)
        then:
        sleep(mappingTimeout)
        space.get(request).get() == data


        when:
        space.autoIncrement(data)
        space.autoIncrement(data)
        space.autoIncrement(data)
        db.renameSpace(spaceName, spaceName = "r1_crud2")
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("testData"))
                .build()
        space = db.space(spaceName)
        space.autoIncrement(data)
        then:
        sleep(mappingTimeout)
        ((space.len() == 5) && (space.schemaLen() == 2))


        when:
        request = intPrimitive(2)
        then:
        true
        space.get(request).isEmpty() && space.select(request).isEmpty()


        when:
        request = intPrimitive(7)
        sleep(mappingTimeout)
        Value response = space.select(request).get().get(0)
        then:
        true
        response == data


        when:
        space.truncate()
        then:
        (space.count() == 0) && (space.schemaCount() == 0)


        when:
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("another data"))
                .build()
        space.put(data)
        then:
        sleep(mappingTimeout)
        space.get(request).get() == data


        when:
        Value key = intPrimitive(7)
        space.delete(key)
        then:
        space.get(request).isEmpty()


        when:
        space.put(data)
        sleep(mappingTimeout)
        space.update(key, TarantoolUpdateFieldOperation.assigment(3, 'data', stringPrimitive("another")))
        then:
        ((Entity)space.get(request).get()).get("data") == stringPrimitive("another")

        when:
        space.put(data)
        sleep(mappingTimeout)
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("something"))
                .build()
        space.replace(data)
        then:
        space.get(key).get() == data

        when:
        space.upsert(data, TarantoolUpdateFieldOperation.addition(1, 1))
        space.upsert(data, TarantoolUpdateFieldOperation.addition(1, 1))
        sleep(mappingTimeout)
        then:
        space.get(request).isPresent() && space.get(intPrimitive(8)).isPresent()

        cleanup:
        db.dropSpace(spaceName)
    }

    def "Router2 mapping builder"(){
        setup:
        def clientId = "router_2"
        def spaceName = "r2_map_build"

        TarantoolInstance db = tarantoolInstance(clientId)
        TarantoolAsynchronousSpace space = db.asynchronousSpace(spaceName)



        Entity data = Entity.entityBuilder()
                .put("id", intPrimitive(1))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("data"))
                .put("anotherData", stringPrimitive("another data"))
                .build()

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .field("id", UNSIGNED, false)
                .field("bucket_id", UNSIGNED))
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

        db.createIndex(spaceName, 'data', tarantoolSpaceIndex()
                .part(3)
                .unique(false))
        sleep(mappingTimeout*10)

        def response = space.select('data', stringPrimitive('data')).get().get()
        int succeeded = response.size()
        then:
        (succeeded == benchmarkOpsCount)

        cleanup:
        db.dropSpace(spaceName)
    }

    def "Router2 mapping loss rate"(){
        setup:
        def clientId = "router_2"
        def spaceName = "r2_map_loss"

        TarantoolInstance db = tarantoolInstance(clientId)
        TarantoolAsynchronousSpace space = db.asynchronousSpace(spaceName)



        Entity data = Entity.entityBuilder()
                .put("id", intPrimitive(1))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
                .build()

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .field("id", UNSIGNED, false)
                .field("bucket_id", UNSIGNED))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .part("id")
                .ifNotExists(true)
                .unique(true))
        db.createIndex(spaceName, 'bucket_id', tarantoolSpaceIndex()
                .part(2)
                .unique(false))

        when:

        int succeeded = 0
        for (int i = 0; i<benchmarkOpsCount; i++){
            space.autoIncrement(data)
            sleep(mappingTimeout)
            if (space.get(intPrimitive(i+1)).get().isPresent()) succeeded++
        }
        println(intPrimitive(succeeded))
        then:
        (succeeded == benchmarkOpsCount)

        cleanup:
        db.dropSpace(spaceName)
    }

}
