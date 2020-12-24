
package io.art.tests.specification.tarantool

import io.art.tarantool.dao.TarantoolAsynchronousSpace
import io.art.value.immutable.Entity
import io.art.value.immutable.Value
import io.art.tarantool.dao.TarantoolInstance
import io.art.tarantool.model.TarantoolUpdateFieldOperation
import io.art.tarantool.dao.TarantoolSpace
import io.art.tarantool.storage.TarantoolStorageSpace
import spock.lang.Specification

import static io.art.launcher.ModuleLauncher.launch
import static io.art.model.modeler.ModuleModeler.module
import static io.art.tarantool.configuration.space.TarantoolSpaceFormat.tarantoolSpaceFormat
import static io.art.tarantool.configuration.space.TarantoolSpaceIndex.tarantoolSpaceIndex
import static io.art.tarantool.configuration.space.TarantoolSpaceConfig.tarantoolSpaceConfig
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolIndexType
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.*
import static io.art.value.factory.PrimitivesFactory.*
import static io.art.tarantool.module.TarantoolModule.*

class Tarantool extends Specification {
    def mappingTimeout = 300

    def setupSpec(){
        launch module().apply()
    }

    def "Storage1 CRUD"() {
        setup:
        def spaceName = "s1_CRUD"
        def clientId = "storage_1_a"


        TarantoolInstance db = tarantoolInstance(clientId)
        TarantoolSpace space = db.space(spaceName)



        Value data = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
                .build()
        Value request = intPrimitive(3)

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .field("id", NUMBER, false))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .part("id")
                .ifNotExists(true)
                .unique(true))


        when:
        def spaces = db.listSpaces()
        def indices = space.listIndices()
        then:
        spaces.contains(spaceName) && indices.contains("primary")

        when:
        space.insert(data)
        then:
        space.get(request).get() == data


        when:
        space.autoIncrement(data)
        space.autoIncrement(data)
        space.autoIncrement(data)
        db.renameSpace(spaceName, spaceName = "s1_CRUD2")
        space = db.space(spaceName)
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("data", stringPrimitive("testData"))
                .build()
        space.autoIncrement(data)
        then:
        (space.len() == 5) && (space.schemaLen() == 2)


        when:
        request = intPrimitive(2)
        then:
        space.get(request).isEmpty() && space.select(request).isEmpty()


        when:
        request = intPrimitive(7)
        Value response = space.select(request).get().get(0) as Entity
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
        (space.get(request).get() as Entity).get("data") == stringPrimitive("another")

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
        (space.len() == 5) && (space.schemaLen() == 2)


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
        (space.get(request).get() as Entity).get("data") == stringPrimitive("another")

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

    def "TarantoolStorage1 interface ops"(){
        setup:
        def spaceName = "s1_storage_ops"
        def clientId = "storage_1_a"

        def db = tarantoolInstance(clientId)
        def space = new TarantoolStorageSpace(db.space(spaceName))



        Entity data = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
                .build()
        Value request = intPrimitive(3)

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .field("id", UNSIGNED, false))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
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
        db.renameSpace(spaceName, spaceName = "s1_storage_ops2")
        space = new TarantoolStorageSpace(db.space(spaceName))
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("data", stringPrimitive("testData"))
                .build()
        space.autoIncrement(data)
        then:
        (space.count() == 5)


        when:
        request = intPrimitive(2)
        then:
        space.get(request).isEmpty() && space.find(request).isEmpty()


        when:
        request = intPrimitive(7)
        Entity response = space.find(request).get().get(0) as Entity
        then:
        response == data


        when:
        space.truncate()
        then:
        (space.count() == 0)


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


        cleanup:
        db.dropSpace(spaceName)
    }

    def "Storage2 async CRUD"(){
        setup:
        def spaceName = "s2_CRUD"
        def clientId = "storage_2_a"


        TarantoolInstance db = tarantoolInstance(clientId)
        TarantoolAsynchronousSpace space = db.asynchronousSpace(spaceName)



        Entity data = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
                .build()
        Value request = intPrimitive(3)

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .field("id", NUMBER, false))
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .part("id")
                .ifNotExists(true)
                .unique(true))


        when:
        def spaces = db.listSpaces()
        def indices = space.listIndices().get()
        then:
        spaces.contains(spaceName) && indices.contains("primary")

        when:
        space.insert(data)
        then:
        ((Entity) space.get(request).get().get()) == data


        when:
        space.autoIncrement(data)
        space.autoIncrement(data)
        space.autoIncrement(data)
        db.renameSpace(spaceName, spaceName = "s1_CRUD2")
        space = db.asynchronousSpace(spaceName)
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("data", stringPrimitive("testData"))
                .build()
        space.autoIncrement(data)
        then:
        ((space.len().get() == 5) && (space.schemaLen().get() == 2))


        when:
        request = intPrimitive(2)
        then:
        space.get(request).get().isEmpty() && space.select(request).get().isEmpty()


        when:
        request = intPrimitive(7)
        Entity response = space.select(request).get().get().get(0) as Entity
        then:
        response == data


        when:
        space.truncate()
        then:
        (space.count().get() == 0) && (space.schemaCount().get() == 0)


        when:
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("data", stringPrimitive("another data"))
                .build()
        space.put(data)
        then:
        space.get(request).get().get() == data


        when:
        space.delete(intPrimitive(7))
        then:
        space.get(request).get().isEmpty()


        when:
        space.put(data)
        space.update(intPrimitive(7),
                TarantoolUpdateFieldOperation.assigment(2, 'data', stringPrimitive("another")))
        then:
        (space.get(request).get().get() as Entity).get("data") == stringPrimitive("another")

        when:
        space.put(data)
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("data", stringPrimitive("something"))
                .build()
        space.replace(data)
        then:
        space.get(intPrimitive(7)).get().get() == data

        when:
        space.upsert(data, TarantoolUpdateFieldOperation.addition(1, 1))
        space.upsert(data, TarantoolUpdateFieldOperation.addition(1, 1))
        then:
        space.get(intPrimitive(7)).get().isPresent() && space.get(intPrimitive(8)).get().isPresent()


        cleanup:
        db.dropSpace(spaceName)
    }

    def "Storage2 cluster operations lock"(){
        setup:
        def spaceName = "s2_COL"
        def clientId = "storage_2_a"
        TarantoolInstance db = tarantoolInstance(clientId)
        boolean result = false


        getClient(clientId).eval("art.box.space.activeClusterOperation = true")

        when:
        try{
            db.createSpace(spaceName, tarantoolSpaceConfig())
        } catch(TarantoolDaoException){
            result = true
        }
        then:
        result


        when:
        result = false
        try {
            db.formatSpace(spaceName, tarantoolSpaceFormat())
        } catch(TarantoolDaoException){
            result = true
        }
        then:
        result


        when:
        result = false
        try {
            db.createIndex(spaceName, "primary", tarantoolSpaceIndex())
        } catch(TarantoolDaoException){
            result = true
        }
        then:
        result


        when:
        result = false
        try {
            db.renameSpace(spaceName, spaceName = "s2_COL2")
        } catch(TarantoolDaoException){
            result = true
        }
        then:
        result


        when:
        result = false
        try {
            db.dropSpace(spaceName)
        } catch(TarantoolDaoException){
            result = true
        }
        then:
        result


        cleanup:
        getClient(clientId).eval("art.box.space.activeClusterOperation = false")
    }


}
