
package io.art.tests.specification.tarantool

import io.art.tarantool.space.TarantoolSpace
import io.art.tarantool.instance.TarantoolInstance
import io.art.tarantool.model.operation.TarantoolUpdateFieldOperation
import io.art.value.immutable.Entity
import io.art.value.immutable.Value
import spock.lang.Specification

import static io.art.launcher.ModuleLauncher.launch
import static io.art.model.implementation.ModuleModel.module
import static io.art.tarantool.configuration.space.TarantoolSpaceConfig.tarantoolSpaceConfig
import static io.art.tarantool.configuration.space.TarantoolSpaceFormat.tarantoolSpaceFormat
import static io.art.tarantool.configuration.space.TarantoolSpaceIndex.tarantoolSpaceIndex
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.NUMBER
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.STRING
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.UNSIGNED
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolIndexType
import static io.art.tarantool.module.TarantoolModule.tarantoolInstance
import static io.art.value.factory.PrimitivesFactory.intPrimitive
import static io.art.value.factory.PrimitivesFactory.stringPrimitive

class TarantoolTransaction extends Specification {
    def synchronizationTimeout = 60

    def setupSpec(){
        launch module().make()
    }

    def "Storage1 transactions"() {
        setup:
        def clusterId = "storage1"
        def spaceName = 's1_tx'

        TarantoolInstance db = tarantoolInstance(clusterId)
        TarantoolSpace space = db.space(spaceName)

        Value data = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
                .build()

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .field("id", NUMBER, false)
                .field("data", STRING, false))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .part("id")
                .ifNotExists(true)
                .unique(true))
        db.createIndex(spaceName, 'data', tarantoolSpaceIndex()
                .part('data')
                .ifNotExists(true)
                .unique(false))


        when:
        db.beginTransaction()
        def result1 = space.insert(data)
        def result2 = space.autoIncrement(data)
        db.commitTransaction()

        then:
        ((Entity) result1.get()).get('id') == intPrimitive(3) &&
                ((Entity) result2.get()).get('id') == intPrimitive(4)


        when:
        db.beginTransaction()
        result1 = space.autoIncrement(data)
        result2 = space.autoIncrement(result1.useResult())
        db.commitTransaction()

        then:
        ((Entity) result1.get()).get('id') == intPrimitive(5) &&
                ((Entity) result2.get()).get('id') == intPrimitive(6)

        when:
        db.beginTransaction()
        result1 = space.autoIncrement(data)
        result2 = space.select(result1.useResultField('data'))
                .index('data')
                .execute()
        db.commitTransaction()
        result1.synchronize()
        then:
        result2.get().size() == 5


        cleanup:
        db.dropSpace(spaceName)

    }

    def "Routers transaction"() {
        setup:
        def clusterId = "routers"
        def spaceName = "r_tx"


        def db = tarantoolInstance(clusterId)
        def space = db.space(spaceName)



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
        spaces.get().contains(spaceName) && indices.get().contains("primary")


        when:
        db.beginTransaction()
        def response1 = space.insert(data)
        def response2 = space.get(request)
        db.commitTransaction()
        response1.get()
        then:
        response1.get() == response2.get()


        when:
        db.beginTransaction()
        response1 = space.autoIncrement(data)
        space.autoIncrement(response1.useResult())
        space.autoIncrement(response1.useResult())
        db.commitTransaction()
        db.renameSpace(spaceName, spaceName = "r_tx2")
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("testData"))
                .build()
        space = db.space(spaceName)
        space.autoIncrement(data)
        then:
        def len = space.len()
        def schemaLen = space.schemaLen()
        sleep(synchronizationTimeout)
        len.get() == 5 && schemaLen.get() == 2


        when:
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("another data"))
                .build()

        db.beginTransaction()
        space.put(data)
        response1 = space.get(intPrimitive(7))
        db.commitTransaction()
        response1.synchronize()

        then:
        response1.get() == data


        when:
        Value key = intPrimitive(7)

        db.beginTransaction()
        response1 = space.delete(key)
        response2 = space.get(key)
        db.commitTransaction()
        response1.synchronize()
        then:
        response2.isEmpty()


        when:
        db.beginTransaction()
        space.put(data)
        space.update(key, TarantoolUpdateFieldOperation.assigment(3, 'data', stringPrimitive("another")))
        response1 = space.get(key)
        db.commitTransaction()
        response1.synchronize()

        then:
        ((Entity)response1.get()).get("data") == stringPrimitive("another")

        when:
        db.beginTransaction()
        space.put(data)
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("something"))
                .build()
        space.replace(data)
        response1 = space.get(key)
        db.commitTransaction()
        response1.synchronize()
        then:
        response1.get() == data

        when:
        space.upsert(data, TarantoolUpdateFieldOperation.addition(1, 1))
        space.upsert(data, TarantoolUpdateFieldOperation.addition(1, 1))
        sleep(synchronizationTimeout)
        then:
        space.get(request).isPresent() && space.get(intPrimitive(8)).isPresent()

        cleanup:
        db.dropSpace(spaceName)
    }

}
