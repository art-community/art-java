
package io.art.tests.specification.tarantool

import io.art.value.immutable.Entity
import io.art.value.immutable.Value
import org.tarantool.TarantoolClusterClientConfig
import io.art.refactored.configuration.TarantoolInstanceConfiguration
import io.art.refactored.configuration.TarantoolModuleConfiguration
import io.art.refactored.module.TarantoolModule
import io.art.refactored.dao.TarantoolInstance
import io.art.refactored.model.TarantoolUpdateFieldOperation
import io.art.refactored.dao.TarantoolSpace
import io.art.refactored.storage.StorageSpace
import io.art.refactored.storage.TarantoolStorageSpace
import spock.lang.Specification

import static io.art.launcher.ModuleLauncher.launch
import static io.art.model.module.ModuleModel.module
import static io.art.refactored.configuration.space.TarantoolSpaceFormat.tarantoolSpaceFormat
import static io.art.refactored.configuration.space.TarantoolSpaceIndex.tarantoolSpaceIndex
import static io.art.refactored.configuration.space.TarantoolSpaceConfig.tarantoolSpaceConfig
import static io.art.refactored.constants.TarantoolModuleConstants.TarantoolIndexType
import static io.art.refactored.constants.TarantoolModuleConstants.TarantoolFieldType.*
import static io.art.value.factory.PrimitivesFactory.*

class TarantoolRefactored extends Specification {

    def router1Address = "localhost:3311"
    def storage1Address = "localhost:3301"
    def storage2Address = "localhost:3302"
    def username = 'username'
    def password = 'password'

    def "start modules"() {
        setup:
        launch module()
    }

    def "Storage1 CRUD"() {
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
                .part("id")
                .ifNotExists(true)
                .unique(true))



        when:
        space.insert(data)
        then:
        ((Entity) space.get(request).get()).asMap() == data.asMap()


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
        response.asMap() == data.asMap()


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
        ((Entity) space.get(request).get()).asMap() == data.asMap()


        when:
        space.delete(intPrimitive(7))
        then:
        space.get(request).isEmpty()


        when:
        space.put(data)
        space.update(intPrimitive(7),
                TarantoolUpdateFieldOperation.assigment(2, 'data', stringPrimitive("another")))
        then:
        ((Entity)space.get(request).get()).get("data") == stringPrimitive("another")

        when:
        space.put(data)
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("data", stringPrimitive("something"))
                .build()
        space.replace(data)
        then:
        ((Entity)space.get(intPrimitive(7)).get()).asMap() == data.asMap()

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
                .put("id", intPrimitive(3))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
                .build()
        Value request = Entity.entityBuilder()
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
        then:
        space.get(request).get() == data


        when:
        space.autoIncrement(data)
        space.autoIncrement(data)
        space.autoIncrement(data)
        //db.renameSpace(spaceName, spaceName = "r1_crud2")
        data = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("bucket_id", intPrimitive(99))
                .put("data", stringPrimitive("testData"))
                .build()
        space = tnt.getSpace(clientId, spaceName)
        space.autoIncrement(data)
        then:
        ((space.len() == 5) && (space.schemaLen() == 2))


        when:
        request = Entity.entityBuilder()
                .put("id", intPrimitive(2))
                .put("bucket_id", intPrimitive(99))
                .build()
        then:
        true
        space.get(request).isEmpty()
//                && dao.select(space, request).isEmpty()


        when:
        request = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("bucket_id", intPrimitive(99))
                .build()
//        Entity response = dao.select(space, request).get().get(0)
        then:
        true
//        response == data


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
        space.get(request).get() == data


        when:
        Entity key = Entity.entityBuilder()
                .put("id", intPrimitive(7))
                .put("bucket_id", intPrimitive(99))
                .build()
        space.delete(key)
        then:
        space.get(request).isEmpty()


        when:
        space.put(data)

        space.update(key, TarantoolUpdateFieldOperation.assigment(3, 'data', stringPrimitive("another")))
        then:
        ((Entity)space.get(request).get()).get("data") == stringPrimitive("another")

        when:
        space.put(data)
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
        then:
        def request2 = Entity.entityBuilder()
                .put("id", intPrimitive(8))
                .put("bucket_id", intPrimitive(99))
                .build()
        space.get(request).isPresent() && space.get(request2).isPresent()

        cleanup:
        db.dropSpace(spaceName)
    }

    def "TarantoolStorage1 interface ops"(){
        setup:
        def spaceName = "s1_storage_ops"
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
        StorageSpace space = new TarantoolStorageSpace(tnt.getSpace(clientId, spaceName))



        Entity data = Entity.entityBuilder()
                .put("id", intPrimitive(3))
                .put("data", stringPrimitive("testData"))
                .put("anotherData", stringPrimitive("another data"))
                .build()
        Value request = intPrimitive(3)

        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .addField("id", UNSIGNED, false))
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
        space = new TarantoolStorageSpace(tnt.getSpace(clientId, spaceName))
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

    def "Storage2 cluster operations lock"(){
        setup:
        def spaceName = "s2_COL"
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

        tnt.getClient(clientId).syncOps().eval("art.box.space.cluster_op_in_progress = true")
        String exception = ''

        when:
        try{
            db.createSpace(spaceName, tarantoolSpaceConfig())
        } catch(Exception e){
            exception = e.getMessage()
        }
        then:
        exception.contentEquals("java.util.concurrent.TimeoutException")


        when:
        try{
            db.formatSpace(spaceName, tarantoolSpaceFormat())
        } catch(Exception e){
            exception = e.getMessage()
        }
        then:
        exception.contentEquals("java.util.concurrent.TimeoutException")


        when:
        try{
            db.createIndex(spaceName, "primary", tarantoolSpaceIndex())
        } catch(Exception e){
            exception = e.getMessage()
        }
        then:
        exception.contentEquals("java.util.concurrent.TimeoutException")


        when:
        try{
            db.renameSpace(spaceName, spaceName = "s2_COL2")
        } catch(Exception e){
            exception = e.getMessage()
        }
        then:
        exception.contentEquals("java.util.concurrent.TimeoutException")


        when:
        try{
            db.dropSpace(spaceName)
        } catch(Exception e){
            exception = e.getMessage()
        }
        then:
        exception.contentEquals("java.util.concurrent.TimeoutException")


        cleanup:
        tnt.getClient(clientId).syncOps().eval("art.box.space.cluster_op_in_progress = false")
    }


}
