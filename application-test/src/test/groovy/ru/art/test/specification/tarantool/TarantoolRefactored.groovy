
package ru.art.test.specification.tarantool

import org.tarantool.TarantoolClient
import ru.art.refactored.storage.dao.TarantoolDao
import ru.art.entity.Entity
import ru.art.refactored.module.connector.TarantoolConnector
import ru.art.refactored.model.TarantoolUpdateFieldOperation
import spock.lang.Specification
import static ru.art.refactored.configuration.TarantoolSpaceFormat.tarantoolSpaceFormat
import static ru.art.refactored.configuration.TarantoolSpaceIndex.tarantoolSpaceIndex
import static ru.art.refactored.configuration.TarantoolSpaceConfig.tarantoolSpaceConfig
import static ru.art.refactored.constants.TarantoolModuleConstants.TarantoolIndexType
import static ru.art.entity.PrimitivesFactory.*


class TarantoolRefactored extends Specification {
    def space = "storage_test"

    def "CRUD on storage"() {
        setup:
        TarantoolConnector connector = new TarantoolConnector()
        TarantoolClient client = connector.getClient('localhost:3301', connector.getDefaultConfig())
        TarantoolDao dao = new TarantoolDao(client)

        Entity data = new Entity.EntityBuilder()
                .intField("id", 3)
                .stringField("data", "testData")
                .stringField("anotherData", "another data")
                .build()
        Entity request = new Entity.EntityBuilder().intField("id", 3).build()

        dao.space.create(space, tarantoolSpaceConfig()
                .ifNotExists(true))
        dao.space.format(space, tarantoolSpaceFormat()
                .addField("id", "number", false))
        dao.space.createIndex(space, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .id(0)
                .part("id")
                .ifNotExists(true)
                .unique(true))



        when:
        dao.insert(space, data)
        then:
        dao.get(space, request).get() == data


        when:
        dao.autoIncrement(space, data)
        dao.autoIncrement(space, data)
        dao.autoIncrement(space, data)
        dao.space.rename(space, space = "storage_test2")
        data = new Entity.EntityBuilder()
                .intField("id", 7)
                .stringField("data", "testData")
                .build()
        dao.autoIncrement(space, data)
        then:
        ((dao.space.len(space) == 5) && (dao.space.schemaLen(space) == 2))


        when:
        request = new Entity.EntityBuilder().intField("id", 2).build()
        then:
        dao.get(space, request).isEmpty() && dao.select(space, request).isEmpty()


        when:
        request = new Entity.EntityBuilder().intField("id", 7).build()
        Entity response = dao.select(space, request).get().get(0)
        then:
        response == data


        when:
        dao.space.truncate(space)
        then:
        (dao.space.count(space) == 0) && (dao.space.schemaCount(space) == 0)


        when:
        data = new Entity.EntityBuilder()
                .intField("id", 7)
                .stringField("data", "another data")
                .build()
        dao.put(space, data)
        then:
        dao.get(space, request).get() == data


        when:
        dao.delete(space, intPrimitive(7))
        then:
        dao.get(space, request).isEmpty()


        when:
        dao.put(space,data)
        dao.update(space, intPrimitive(7),
                TarantoolUpdateFieldOperation.assigment(2, 'data', stringPrimitive("another")))
        then:
        ((Entity)dao.get(space, request).get()).getString("data") == "another"

        when:
        dao.put(space, data)
        data = new Entity.EntityBuilder()
                .intField("id", 7)
                .stringField("data", "something")
                .build()
        dao.replace(space, data)
        then:
        dao.get(space, intPrimitive(7)).get() == data

        when:
        dao.upsert(space, data, TarantoolUpdateFieldOperation.addition(1, 1))
        dao.upsert(space, data, TarantoolUpdateFieldOperation.addition(1, 1))
        then:
        dao.get(space, intPrimitive(7)).isPresent() && dao.get(space, intPrimitive(8)).isPresent()


        cleanup:
        dao.space.drop(space)


    }

    def "VSHARD cluster operations lock"(){
        setup:
        TarantoolConnector connector = new TarantoolConnector()
        TarantoolClient client = connector.getClient('localhost:3301', connector.getDefaultConfig())
        TarantoolDao dao = new TarantoolDao(client)
        client.syncOps().eval("art_svc.space.cluster_op_in_progress = true")
        String exception

        when:
        try{
            dao.space.create(space, tarantoolSpaceConfig())
        } catch(Exception e){
            exception = e.getMessage()
        }
        then:
        exception.contentEquals("java.util.concurrent.TimeoutException")


        when:
        try{
            dao.space.format(space, tarantoolSpaceFormat())
        } catch(Exception e){
            exception = e.getMessage()
        }
        then:
        exception.contentEquals("java.util.concurrent.TimeoutException")


        when:
        try{
            dao.space.createIndex(space, "primary", tarantoolSpaceIndex())
        } catch(Exception e){
            exception = e.getMessage()
        }
        then:
        exception.contentEquals("java.util.concurrent.TimeoutException")


        when:
        try{
            dao.space.rename(space, space = "storage_test2")
        } catch(Exception e){
            exception = e.getMessage()
        }
        then:
        exception.contentEquals("java.util.concurrent.TimeoutException")


        when:
        try{
            dao.space.drop(space)
        } catch(Exception e){
            exception = e.getMessage()
        }
        then:
        exception.contentEquals("java.util.concurrent.TimeoutException")


        cleanup:
        client.syncOps().eval("art_svc.space.cluster_op_in_progress = false")
    }

    def "VSHARD router CRUD"() {
        setup:
        TarantoolConnector std = new TarantoolConnector()
        TarantoolClient client = std.getClient('localhost:3300', std.getDefaultConfig())
        TarantoolDao dao = new TarantoolDao(client)
        space = "vshard_test"

        Entity data = new Entity.EntityBuilder()
                .intField("id", 3)
                .intField("bucket_id", 99)
                .stringField("data", "testData")
                .stringField("anotherData", "another data")
                .build()
        Entity request = new Entity.EntityBuilder()
                .intField("id", 3)
                .intField("bucket_id", 99)
                .build()

        dao.space.create(space, tarantoolSpaceConfig()
                .ifNotExists(true))
        dao.space.format(space, tarantoolSpaceFormat()
                .addField("id", "unsigned", false)
                .addField("bucket_id", "unsigned"))
        dao.space.createIndex(space, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .id(0)
                .part("id")
                .ifNotExists(true)
                .unique(true))

        when:
        dao.insert(space, data)
        then:
        dao.get(space, request).get() == data


        when:
        dao.autoIncrement(space, data)
        dao.autoIncrement(space, data)
        dao.autoIncrement(space, data)
        dao.space.rename(space, space = "storage_test2")
        data = new Entity.EntityBuilder()
                .intField("id", 7)
                .intField("bucket_id", 99)
                .stringField("data", "testData")
                .build()
        dao.autoIncrement(space, data)
        then:
        ((dao.space.len(space) == 5) && (dao.space.schemaLen(space) == 2))


        when:
        request = new Entity.EntityBuilder()
                .intField("id", 2)
                .intField("bucket_id", 99)
                .build()
        then:
        true
        dao.get(space, request).isEmpty()
//                && dao.select(space, request).isEmpty()


        when:
        request = new Entity.EntityBuilder()
                .intField("id", 7)
                .intField("bucket_id", 99)
                .build()
//        Entity response = dao.select(space, request).get().get(0)
        then:
        true
//        response == data


        when:
        dao.space.truncate(space)
        then:
        (dao.space.count(space) == 0) && (dao.space.schemaCount(space) == 0)


        when:
        data = new Entity.EntityBuilder()
                .intField("id", 7)
                .intField("bucket_id", 99)
                .stringField("data", "another data")
                .build()
        dao.put(space, data)
        then:
        dao.get(space, request).get() == data


        when:
        def key = new Entity.EntityBuilder()
                .intField("id", 7)
                .intField("bucket_id", 99)
                .build()
        dao.delete(space, key)
        then:
        dao.get(space, request).isEmpty()


        when:
        dao.put(space,data)

        dao.update(space, key, TarantoolUpdateFieldOperation.assigment(3, 'data', stringPrimitive("another")))
        then:
        ((Entity)dao.get(space, request).get()).getString("data") == "another"

        when:
        dao.put(space, data)
        data = new Entity.EntityBuilder()
                .intField("id", 7)
                .intField("bucket_id", 99)
                .stringField("data", "something")
                .build()
        dao.replace(space, data)
        then:
        dao.get(space, key).get() == data

        when:
        dao.upsert(space, data, TarantoolUpdateFieldOperation.addition(1, 1))
        dao.upsert(space, data, TarantoolUpdateFieldOperation.addition(1, 1))
        then:
        def request2 = new Entity.EntityBuilder()
                .intField("id", 8)
                .intField("bucket_id", 99)
                .build()
        dao.get(space, request).isPresent() && dao.get(space, request2).isPresent()

        cleanup:
        dao.space.drop(space)
    }
}