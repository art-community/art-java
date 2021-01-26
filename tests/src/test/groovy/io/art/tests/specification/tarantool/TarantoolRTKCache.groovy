
package io.art.tests.specification.tarantool

import io.art.tarantool.instance.TarantoolInstance
import io.art.tarantool.model.operation.TarantoolUpdateFieldOperation
import io.art.tarantool.model.record.TarantoolRecord
import io.art.tarantool.space.TarantoolSpace
import io.art.value.immutable.Entity
import io.art.value.immutable.Value
import spock.lang.Specification

import static io.art.launcher.ModuleLauncher.launch
import static io.art.model.configurator.ModuleModelConfigurator.module
import static io.art.tarantool.configuration.space.TarantoolSpaceConfig.tarantoolSpaceConfig
import static io.art.tarantool.configuration.space.TarantoolSpaceFormat.tarantoolSpaceFormat
import static io.art.tarantool.configuration.space.TarantoolSpaceIndex.tarantoolSpaceIndex
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.BOOLEAN
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.STRING
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.INTEGER
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.INTEGER
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.UNSIGNED
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolIndexIterator
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolIndexType
import static io.art.tarantool.module.TarantoolModule.tarantoolInstance
import static io.art.value.factory.PrimitivesFactory.boolPrimitive
import static io.art.value.factory.PrimitivesFactory.intPrimitive
import static io.art.value.factory.PrimitivesFactory.longPrimitive
import static io.art.value.factory.PrimitivesFactory.stringPrimitive;

class TarantoolRTKCache extends Specification {

    def spaceName = "tt"
    def clusterId = "art-platform-s2"
    def rand = new Random();

    def setupSpec(){
        launch module().configure()
    }

    static def createSpace(TarantoolInstance db, String spaceName){
        db.createSpace(spaceName, tarantoolSpaceConfig()
                .ifNotExists(true))
    }

    static def formatSpace(TarantoolInstance db, String spaceName){
        db.formatSpace(spaceName, tarantoolSpaceFormat()
                .field("int1_id", INTEGER, false)
                .field("int2", INTEGER, false)
                .field("int3", INTEGER, false)
                .field("int4", INTEGER, false)
                .field("int5", INTEGER, false)

                .field("int6", INTEGER, false)
                .field("int7", INTEGER, false)
                .field("int8", INTEGER, false)
                .field("int9", INTEGER, false)
                .field("int10", INTEGER, false)

                .field("int11", INTEGER, false)
                .field("int12", INTEGER, false)
                .field("int13", INTEGER, false)
                .field("int14", INTEGER, false)
                .field("int15", INTEGER, false)

                .field("int16", INTEGER, false)
                .field("int17", INTEGER, false)
                .field("int18", INTEGER, false)
                .field("int19", INTEGER, false)
                .field("int20", INTEGER, false)

                .field("int21", INTEGER, false)
                .field("int22", INTEGER, false)
                .field("int23", INTEGER, false)
                .field("int24", INTEGER, false)
                .field("int25", INTEGER, false)

                .field("int26", INTEGER, false)
                .field("int27", INTEGER, false)
                .field("int28", INTEGER, false)
                .field("int29", INTEGER, false)
                .field("int30", INTEGER, false)

                .field("int31", INTEGER, false)
                .field("int32", INTEGER, false)
                .field("int33", INTEGER, false)
                .field("int34", INTEGER, false)
                .field("int35", INTEGER, false)

                .field("int36", INTEGER, false)
                .field("int37", INTEGER, false)
                .field("int38", INTEGER, false)
                .field("int39", INTEGER, false)
                .field("int40", INTEGER, false)

                .field("long1", INTEGER, false)
                .field("long2", INTEGER, false)
                .field("long3", INTEGER, false)
                .field("long4", INTEGER, false)
                .field("long5", INTEGER, false)
                .field("long6", INTEGER, false)
                .field("long7", INTEGER, false)
                .field("long8", INTEGER, false)
                .field("long9", INTEGER, false)
                .field("long10", INTEGER, false)
                .field("long11", INTEGER, false)
                .field("long12", INTEGER, false)

                .field("bool1", BOOLEAN, false)
                .field("bool2", BOOLEAN, false)
                .field("bool3", BOOLEAN, false)
                .field("bool4", BOOLEAN, false)
                .field("bool5", BOOLEAN, false)
                .field("bool6", BOOLEAN, false)
                .field("bool7", BOOLEAN, false)
                .field("bool8", BOOLEAN, false)

                .field("str1", STRING, false)
                .field("str2", STRING, false)
                .field("str3", STRING, false)
                .field("str4", STRING, false)
                .field("str5", STRING, false)
                .field("str6", STRING, false)
                .field("str7", STRING, false)
                .field("str8", STRING, false)
                .field("str9", STRING, false)
                .field("str10", STRING, false)
                .field("str11", STRING, false)
                .field("str12", STRING, false)
                .field("str13", STRING, false)
        )
    }

    static def createIndices(TarantoolInstance db, String spaceName){
        db.createIndex(spaceName, "primary", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .part("int1_id")
                .ifNotExists(true)
                .unique(true)
                .sequence())

        db.createIndex(spaceName, "int", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .part("int5")
                .unique(false))

        db.createIndex(spaceName, "long", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .part("long5")
                .unique(false))

        db.createIndex(spaceName, "bool", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .part("bool5")
                .unique(false))

        db.createIndex(spaceName, "string", tarantoolSpaceIndex()
                .type(TarantoolIndexType.TREE)
                .part("str5")
                .unique(false))
    }

    def getDataEntity(){
        def dataBuilder = Entity.entityBuilder().put("id", intPrimitive(null))

        for (int i=2; i<41; i++){
            dataBuilder.put("int" + i.toString(), intPrimitive(rand.nextInt()))
        }

        for (int i=1; i<13; i++){
            dataBuilder.put("long" + i.toString(), longPrimitive(rand.nextLong()))
        }

        for (int i=1; i<9; i++){
            dataBuilder.put("bool" + i.toString(), boolPrimitive(rand.nextBoolean()))
        }

        for (int i=1; i<14; i++){
            dataBuilder.put("string" + i.toString(), stringPrimitive("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"))
        }

        return dataBuilder.build();
    }

    def "add 1M records"(){
        setup:

        TarantoolInstance db = tarantoolInstance(clusterId)
        TarantoolSpace space = db.space(spaceName)
        //createSpace(db, spaceName)
        //formatSpace(db, spaceName)
        //createIndices(db, spaceName)

        def TarantoolRecord result

        print("started sending records/n/n")
        when:
        for (int i = 0; i< 1000; i++){
            //db.beginTransaction()
            for(int j = 0; j < 1000; j++){
                result = space.insert(getDataEntity())
            }
            //db.commitTransaction()
            //result.synchronize()
            sleep(10)
        }

        then: true
    }
}
