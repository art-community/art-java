
package io.art.tests.specification.tarantool

import io.art.tarantool.dao.TarantoolAsynchronousSpace
import io.art.tarantool.dao.TarantoolInstance
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
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolIndexType
import static io.art.tarantool.module.TarantoolModule.tarantoolInstance
import static io.art.value.factory.PrimitivesFactory.intPrimitive
import static io.art.value.factory.PrimitivesFactory.stringPrimitive

class TarantoolTransaction extends Specification {
    def synchronizationTimeout = 300

    def setupSpec(){
        launch module().make()
    }

    def "Storage1 transactions"() {
        setup:
        def clusterId = "storage1"
        def spaceName = 's1_tx'

        TarantoolInstance db = tarantoolInstance(clusterId)
        TarantoolAsynchronousSpace space = db.asynchronousSpace(spaceName)

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
        space.beginTransaction()
        def result1 = space.insert(data)
        def result2 = space.autoIncrement(data)
        space.commitTransaction()

        then:
        ((Entity) result1.get()).get('id') == intPrimitive(3) &&
                ((Entity) result2.get()).get('id') == intPrimitive(4)


        when:
        space.beginTransaction()
        result1 = space.autoIncrement(data)
        result2 = space.autoIncrement(result1.useResult())
        space.commitTransaction()

        then:
        ((Entity) result1.get()).get('id') == intPrimitive(5) &&
                ((Entity) result2.get()).get('id') == intPrimitive(6)

        when:
        space.beginTransaction()
        result1 = space.autoIncrement(data)
        result2 = space.select('data', result1.useResultField('data'))
        space.commitTransaction()
        then: true





        cleanup:
        db.dropSpace(spaceName)

    }
}
