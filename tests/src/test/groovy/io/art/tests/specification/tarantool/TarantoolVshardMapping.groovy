
package io.art.tests.specification.tarantool

import io.art.tarantool.space.TarantoolSpace
import io.art.tarantool.instance.TarantoolInstance

import io.art.tarantool.model.operation.TarantoolUpdateFieldOperation
import io.art.value.immutable.Entity
import io.art.value.immutable.Value
import spock.lang.Specification

import static io.art.launcher.ModuleLauncher.launch
import static io.art.model.configurator.ModuleModelConfigurator.*;
import static io.art.tarantool.configuration.space.TarantoolSpaceConfig.tarantoolSpaceConfig
import static io.art.tarantool.configuration.space.TarantoolSpaceFormat.tarantoolSpaceFormat
import static io.art.tarantool.configuration.space.TarantoolSpaceIndex.tarantoolSpaceIndex
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.UNSIGNED
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolIndexType
import static io.art.tarantool.module.TarantoolModule.*
import static io.art.value.factory.PrimitivesFactory.intPrimitive
import static io.art.value.factory.PrimitivesFactory.stringPrimitive

class TarantoolVshardMapping extends Specification {
    def testOpsCount = 100
    def synchronizationTimeout = 60

    def setupSpec(){
        launch module().configure()
    }

    def "Router2 mapping builder"(){
        setup:
        def clusterId = "routers"
        def spaceName = "r2_map_build"

        TarantoolInstance db = tarantoolInstance(clusterId)
        TarantoolSpace space = db.space(spaceName)



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
        sleep(synchronizationTimeout*5)

        when:
        for (int i = 0; i<testOpsCount; i++){
            space.autoIncrement(data)
        }

        db.createIndex(spaceName, 'data', tarantoolSpaceIndex()
                .part(3)
                .unique(false))
        sleep(synchronizationTimeout*10)

        def response = space.select(stringPrimitive('data'))
                .index('data')
                .get()
        int succeeded = response.size()
        then:
        (succeeded == testOpsCount)

        cleanup:
        db.dropSpace(spaceName)
    }

    def "Router2 mapping loss"(){
        setup:
        def clusterId = "routers"
        def spaceName = "r2_map_loss"

        TarantoolInstance db = tarantoolInstance(clusterId)
        TarantoolSpace space = db.space(spaceName)



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
        for (int i = 0; i<testOpsCount; i++){
            space.autoIncrement(data)
            sleep(synchronizationTimeout)
            if (space.get(intPrimitive(i+1)).isPresent()) succeeded++
        }
        println(intPrimitive(succeeded))
        then:
        (succeeded == testOpsCount)

        cleanup:
        db.dropSpace(spaceName)
    }

}
