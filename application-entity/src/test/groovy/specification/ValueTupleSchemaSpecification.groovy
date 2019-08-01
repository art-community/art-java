package specification

import ru.art.entity.tuple.schema.EntitySchema
import spock.lang.Specification

import static ru.art.core.factory.CollectionsFactory.fixedArrayOf
import static ru.art.core.factory.CollectionsFactory.mapOf
import static ru.art.entity.Entity.entityBuilder
import static ru.art.entity.PrimitivesFactory.stringPrimitive
import static ru.art.entity.tuple.schema.ValueSchema.fromValue

class ValueTupleSchemaSpecification extends Specification {
    def "should descript entity scheme into/from tuple correctly"() {
        setup:
        def entitySchema = fromValue(entityBuilder()
                .stringField("stringField", "stringFieldValue")
                .stringCollectionField("stringCollectionField", fixedArrayOf("stringCollectionFieldElement"))
                .entityCollectionField("entityCollectionField",
                fixedArrayOf(entityBuilder().stringCollectionField("innerString", fixedArrayOf("innerStringValue")).build()))
                .entityField("entityField", entityBuilder()
                .stringField("innerStringField", "innerStringFieldValue").build())
                .stringParametersField("stringParametersField", mapOf("SK", "SV"))
                .mapField("mapField", mapOf(stringPrimitive("mapKey"), entityBuilder().stringField("mapEntKey", "mapEntValue").build()))
                .build())
        when:
        def tuple = entitySchema.toTuple()

        then:
        entitySchema == EntitySchema.fromTuple(tuple)
    }

}