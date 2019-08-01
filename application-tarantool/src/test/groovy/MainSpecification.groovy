import ru.adk.tarantool.configuration.lua.TarantoolIndexConfiguration
import spock.lang.Specification

import static java.util.Optional.empty
import static ru.adk.config.extensions.activator.AgileConfigurationsActivator.useAgileConfigurations
import static ru.adk.core.constants.StringConstants.EMPTY_STRING
import static ru.adk.core.factory.CollectionsFactory.setOf
import static ru.adk.entity.Entity.entityBuilder
import static ru.adk.entity.PrimitivesFactory.stringPrimitive
import static ru.adk.tarantool.configuration.TarantoolConfiguration.TarantoolEntityMapping.mapping
import static ru.adk.tarantool.configuration.TarantoolModuleConfiguration.entityMapping
import static ru.adk.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.STRING
import static ru.adk.tarantool.dao.TarantoolDao.tarantool
import static ru.adk.tarantool.model.TarantoolUpdateFieldOperation.assigment
import static ru.adk.tarantool.module.TarantoolModule.tarantoolModule
import static ru.adk.tarantool.module.TarantoolModule.tarantoolModuleState
import static ru.adk.tarantool.service.TarantoolIndexService.createIndex
import static ru.adk.tarantool.service.TarantoolSequenceService.createSequence
import static ru.adk.tarantool.service.TarantoolSpaceService.createSpace
import static ru.adk.tarantool.service.TarantoolSpaceService.dropSpace

class MainSpecification extends Specification {
    def spaceName = "DataEntity"
    def instanceId = "T"
    def secondaryIndex = "SecondaryIndex"
    def sequence = "DataEntitySequence"
    def primaryIndex = "PrimaryIndex"
    def fieldName = "Data"
    def idField = "id"
    def dataValue = "Тест"
    def entity = entityBuilder()
            .intField(idField, null)
            .stringField(fieldName, dataValue)
            .build()
    def dao = tarantool(instanceId)
    def newValue = "Мяу"

    def "should run CRUD operations with tarantool with preparation steps"() {
        setup:
        useAgileConfigurations()

        tarantoolModule()
                .getTarantoolConfigurations()[instanceId]
                .entityMapping[spaceName] = mapping().field(fieldName, 2).map()

        createSpace(instanceId, spaceName)
        createSequence(instanceId, sequence)
        createIndex(instanceId, TarantoolIndexConfiguration.builder()
                .spaceName(spaceName)
                .indexName(primaryIndex)
                .sequence(sequence)
                .build())
        createIndex(instanceId, TarantoolIndexConfiguration.builder()
                .spaceName(spaceName)
                .indexName(secondaryIndex)
                .part(TarantoolIndexConfiguration.Part.builder()
                .fieldNumber(entityMapping(instanceId, spaceName).number(fieldName))
                .type(STRING)
                .build())
                .build())

        when:
        entity = dao.put(spaceName, entity)

        then:
        dao.selectByIndex(spaceName, secondaryIndex, setOf(entity.getString(fieldName)))
                .first()
                .getString(fieldName) == entity.getString(fieldName)

        when:
        def newEntity = dao.update(spaceName, setOf(entity.getInt(idField)) as Set<Integer>, assigment(entityMapping(instanceId, spaceName).number(fieldName), fieldName, stringPrimitive(newValue)))

        then:
        dao.getByIndex(spaceName, secondaryIndex, setOf(newValue)).get() == newEntity

        when:
        dao.deleteByIndex(spaceName, secondaryIndex, setOf(newValue))

        then:
        dao.getByIndex(spaceName, secondaryIndex, setOf(entity.getString(fieldName))) == empty()
        dao.len(spaceName) == 0L


        cleanup:
        dropSpace(instanceId, spaceName)
        tarantoolModuleState().loadedCommonScripts.clear()
        tarantoolModuleState().loadedValueScripts.clear()
    }

    def "should run CRUD operations with tarantool without preparation steps"() {
        setup:
        useAgileConfigurations()

        tarantoolModule()
                .getTarantoolConfigurations()[instanceId]
                .entityMapping[spaceName] = mapping().field(fieldName, 2).map()

        when:
        entity = dao.put(spaceName, entity)

        then:
        entity.getString(fieldName) == dao.get(spaceName, entity.getInt(idField))
                .map { it.getString(fieldName) }
                .orElse(EMPTY_STRING)

        when:
        dao.delete(spaceName, entity.getInt(idField))

        then:
        empty() == dao.get(spaceName, entity.getInt(idField))
        0L == dao.len(spaceName)

        when:
        dao.upsert(spaceName, entity, assigment(entityMapping(instanceId, spaceName).number(fieldName), fieldName, stringPrimitive("Мяу")))
        createIndex(instanceId, TarantoolIndexConfiguration.builder()
                .spaceName(spaceName)
                .indexName(secondaryIndex)
                .part(TarantoolIndexConfiguration.Part.builder()
                .fieldNumber(entityMapping(instanceId, spaceName).number(fieldName))
                .type(STRING)
                .build())
                .build())

        then:
        dao.getByIndex(spaceName, secondaryIndex, setOf(entity.getString(fieldName))).isPresent()

        when:
        dao.upsert(spaceName, entity, assigment(entityMapping(instanceId, spaceName).number(fieldName), fieldName, stringPrimitive("Гав")))

        then:
        dao.getByIndex(spaceName, secondaryIndex, setOf("Гав")).isPresent()

        when:
        dao.update(spaceName, setOf(entity.getInt(idField)) as Set<Integer>, assigment(entityMapping(instanceId, spaceName).number(fieldName), fieldName, stringPrimitive("Мяу")))

        then:
        dao.getByIndex(spaceName, secondaryIndex, setOf("Мяу")).isPresent()

        cleanup:
        dropSpace(instanceId, spaceName)
        tarantoolModuleState().loadedCommonScripts.clear()
        tarantoolModuleState().loadedValueScripts.clear()
    }
}
