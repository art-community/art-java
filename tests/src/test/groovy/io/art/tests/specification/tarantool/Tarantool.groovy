
package io.art.tests.specification.tarantool

import io.art.tarantool.space.TarantoolAsynchronousSpace
import io.art.value.immutable.Entity
import io.art.value.immutable.Value
import io.art.tarantool.instance.TarantoolInstance
import io.art.tarantool.model.operation.TarantoolUpdateFieldOperation
import io.art.tarantool.space.TarantoolSpace
import io.art.tarantool.storage.TarantoolStorageSpace
import spock.lang.Specification

import static io.art.launcher.ModuleLauncher.launch
import static io.art.model.implementation.ModuleModel.module
import static io.art.tarantool.configuration.space.TarantoolSpaceFormat.tarantoolSpaceFormat
import static io.art.tarantool.configuration.space.TarantoolSpaceIndex.tarantoolSpaceIndex
import static io.art.tarantool.configuration.space.TarantoolSpaceConfig.tarantoolSpaceConfig
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolIndexType
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.*
import static io.art.value.factory.PrimitivesFactory.*
import static io.art.tarantool.module.TarantoolModule.*

class Tarantool extends Specification {
    def synchronizationTimeout = 300

    def setupSpec(){
        launch module().make()
    }

    def "Storage1 CRUD"() {
        setup:
        def spaceName = "s1_CRUD"
        def clusterId = "storage1"


        TarantoolInstance db = tarantoolInstance(clusterId)
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
        spaces.get().contains(spaceName) && indices.get().contains("primary")

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
        (space.len().get() == 5) && (space.schemaLen().get() == 2)


        when:
        request = intPrimitive(2)
        then:
        space.get(request).isEmpty() && space.select(request).fetch().isEmpty()


        when:
        request = intPrimitive(7)
        Value response = space.select(request).get().get(0) as Entity
        then:
        response == data


        when:
        space.truncate()
        sleep(synchronizationTimeout)
        then:
        (space.count().get() == 0) && (space.schemaCount().get() == 0)


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

    def "Routers CRUD"() {
        setup:
        def clusterId = "routers"
        def spaceName = "r_crud"


        TarantoolInstance db = tarantoolInstance(clusterId)
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
        spaces.get().contains(spaceName) && indices.get().contains("primary")


        when:
        space.insert(data)
        then:
        sleep(synchronizationTimeout)
        space.get(request).get() == data


        when:
        space.autoIncrement(data)
        space.autoIncrement(data)
        space.autoIncrement(data)
        db.renameSpace(spaceName, spaceName = "r_crud2")
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("testData"))
                .build()
        space = db.space(spaceName)
        space.autoIncrement(data)
        then:
        sleep(synchronizationTimeout)
        (space.len().get() == 5) && (space.schemaLen().get() == 2)


        when:
        request = intPrimitive(2)
        then:
        true
        space.get(request).isEmpty() && space.select(request).fetch().isEmpty()


        when:
        request = intPrimitive(7)
        sleep(synchronizationTimeout)
        Value response = space.select(request).get().get(0)
        then:
        true
        response == data


        when:
        space.truncate()
        then:
        (space.count().get() == 0) && (space.schemaCount().get() == 0)


        when:
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("another data"))
                .build()
        space.put(data)
        then:
        sleep(synchronizationTimeout)
        space.get(request).get() == data


        when:
        Value key = intPrimitive(7)
        space.delete(key)
        then:
        space.get(request).isEmpty()


        when:
        space.put(data)
        sleep(synchronizationTimeout)
        space.update(key, TarantoolUpdateFieldOperation.assigment(3, 'data', stringPrimitive("another")))
        then:
        (space.get(request).get() as Entity).get("data") == stringPrimitive("another")

        when:
        space.put(data)
        sleep(synchronizationTimeout)
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
        sleep(synchronizationTimeout)
        then:
        space.get(request).isPresent() && space.get(intPrimitive(8)).isPresent()

        cleanup:
        db.dropSpace(spaceName)
    }

    def "TarantoolStorage1 interface ops"(){
        setup:
        def spaceName = "s1_storage_ops"
        def clusterId = "storage1"

        def db = tarantoolInstance(clusterId)
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
        (space.count().get() == 5)


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
        sleep(synchronizationTimeout)
        then:
        (space.count().get() == 0)


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
        def clusterId = "storage2"


        TarantoolInstance db = tarantoolInstance(clusterId)
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
        spaces.get().contains(spaceName) && indices.contains("primary")

        when:
        space.insert(data)
        sleep(synchronizationTimeout)
        then:
        (Entity) space.get(request).get() == data


        when:
        space.autoIncrement(data)
        space.autoIncrement(data)
        space.autoIncrement(data)
        db.renameSpace(spaceName, spaceName = "s2_CRUD2")
        space = db.asynchronousSpace(spaceName)
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("data", stringPrimitive("testData"))
                .build()
        space.autoIncrement(data)
        sleep(synchronizationTimeout)
        then:
        ((space.len().get() == 5) && (space.schemaLen().get() == 2))


        when:
        request = intPrimitive(2)
        then:
        space.get(request).isEmpty() && space.select(request).fetch().isEmpty()


        when:
        request = intPrimitive(7)
        Entity response = space.select(request).get().get(0) as Entity
        then:
        response == data


        when:
        space.truncate()
        sleep(synchronizationTimeout)
        then:
        (space.count().get() == 0) && (space.schemaCount().get() == 0)


        when:
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("data", stringPrimitive("another data"))
                .build()
        space.put(data)
        sleep(synchronizationTimeout)
        then:
        space.get(request).get() == data


        when:
        space.delete(intPrimitive(7))
        sleep(synchronizationTimeout)
        then:
        space.get(request).isEmpty()


        when:
        space.put(data)
        space.update(intPrimitive(7),
                TarantoolUpdateFieldOperation.assigment(2, 'data', stringPrimitive("another")))
        sleep(synchronizationTimeout)
        then:
        (space.get(request).get() as Entity).get("data") == stringPrimitive("another")

        when:
        space.put(data)
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("data", stringPrimitive("something"))
                .build()
        space.replace(data)
        sleep(synchronizationTimeout)
        then:
        space.get(intPrimitive(7)).get() == data

        when:
        space.upsert(data, TarantoolUpdateFieldOperation.addition(1, 1))
        space.upsert(data, TarantoolUpdateFieldOperation.addition(1, 1))
        sleep(synchronizationTimeout)
        then:
        space.get(intPrimitive(7)).isPresent() && space.get(intPrimitive(8)).isPresent()


        cleanup:
        db.dropSpace(spaceName)
    }

    def "Storage2 cluster operations lock"(){
        setup:
        def spaceName = "s2_COL"
        def clusterId = "storage2"
        TarantoolInstance db = tarantoolInstance(clusterId)
        boolean result = false


        getCluster(clusterId).getClient('storage_2_a').eval("art.box.space.activeClusterOperation = true")

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
        getCluster(clusterId).getClient('storage_2_a').eval("art.box.space.activeClusterOperation = false")
    }


}
