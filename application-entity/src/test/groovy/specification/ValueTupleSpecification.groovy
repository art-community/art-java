/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package specification

import ru.art.entity.tuple.PlainTupleReader
import ru.art.entity.tuple.PlainTupleWriter
import spock.lang.Specification

import static ru.art.core.factory.CollectionsFactory.fixedArrayOf
import static ru.art.core.factory.CollectionsFactory.mapOf
import static ru.art.entity.Entity.entityBuilder
import static ru.art.entity.PrimitivesFactory.stringPrimitive
import static ru.art.entity.tuple.TupleReader.readTuple
import static ru.art.entity.tuple.TupleWriter.writeTuple
import static ru.art.entity.tuple.schema.ValueSchema.fromTuple

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