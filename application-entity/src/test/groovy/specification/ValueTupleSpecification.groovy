package specification

import ru.adk.entity.tuple.PlainTupleReader
import ru.adk.entity.tuple.PlainTupleWriter
import spock.lang.Specification

import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf
import static ru.adk.core.factory.CollectionsFactory.mapOf
import static ru.adk.entity.Entity.entityBuilder
import static ru.adk.entity.PrimitivesFactory.stringPrimitive
import static ru.adk.entity.tuple.TupleReader.readTuple
import static ru.adk.entity.tuple.TupleWriter.writeTuple
import static ru.adk.entity.tuple.schema.ValueSchema.fromTuple

class ValueTupleSpecification extends Specification {
    def "should descript entity into/from tuple correctly"() {
        setup:
        def entity = entityBuilder()
                .stringField("stringField", "stringFieldValue")
                .stringCollectionField("stringCollectionField", fixedArrayOf("stringCollectionFieldElement"))
                .entityCollectionField("entityCollectionField",
                fixedArrayOf(entityBuilder().stringCollectionField("innerString", fixedArrayOf("innerStringValue")).build()))
                .entityField("entityField", entityBuilder()
                .stringField("innerStringField", "innerStringFieldValue").build())
                .stringParametersField("stringParametersField", mapOf("SK", "SV"))
                .mapField("mapField", mapOf(stringPrimitive("mapKey"), entityBuilder().stringField("mapEntKey", "mapEntValue").build()))
                .build()

        when:
        def tuple = writeTuple entity
        println "Сущность: $entity"
        println "Кортеж: $tuple"

        then:
        entity == readTuple(tuple)
    }


    def "should read/write entity from/to plain tuple"() {
        setup:
        def entity = entityBuilder()
                .stringField("stringField", "stringFieldValue")
                .stringCollectionField("stringCollectionField", fixedArrayOf("stringCollectionFieldElement"))
                .entityCollectionField("entityCollectionField",
                fixedArrayOf(entityBuilder().stringCollectionField("innerString", fixedArrayOf("innerStringValue")).build()))
                .entityField("entityField", entityBuilder()
                .stringField("innerStringField", "innerStringFieldValue").build())
                .stringParametersField("stringParametersField", mapOf("SK", "SV"))
                .mapField("mapField", mapOf(stringPrimitive("mapKey"), entityBuilder().stringField("mapEntKey", "mapEntValue").build()))
                .build()

        when:
        def result = PlainTupleWriter.writeTuple entity
        def tuple = result.tuple
        def schemaTuple = result.schema.toTuple()
        println "Сущность: $entity"
        println "Схема: $schemaTuple"
        println "Кортеж: $tuple"

        then:
        entity == PlainTupleReader.readTuple(tuple, fromTuple(schemaTuple))
    }
}