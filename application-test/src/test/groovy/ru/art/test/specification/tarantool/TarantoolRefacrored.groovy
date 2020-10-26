
package ru.art.test.specification.tarantool

import org.tarantool.TarantoolClient
import refactored.storage.dao.TarantoolDao
import ru.art.entity.Entity
import ru.art.entity.Value
import refactored.module.connector.TarantoolConnector
import spock.lang.Specification

import static ru.art.entity.Entity.entityBuilder

class TarantoolRefacrored extends Specification {
    def spaceName = "DataEntity"
    def masterId = "t-master-1"
    def replicaId = "t-master-2"
    def secondaryIndex = "SecondaryIndex"
    def secondaryIndexId = 1
    def sequence = "DataEntitySequence"
    def primaryIndex = "PrimaryIndex"
    def fieldName = "Data"
    def idField = "id"
    def dataValue = "Тест"
    def entity = entityBuilder().stringField(fieldName, dataValue).build()
    def dao
    def newValue = "Мяу"

    def "should run CRUD operations with tarantool with preparation steps"() {
        setup:

        TarantoolConnector std = new TarantoolConnector();
        TarantoolClient stdClient = std.getClient('localhost:3301', std.getDefaultConfig());
        TarantoolDao dao = new TarantoolDao();
        dao.client = stdClient;
        Entity args = new Entity.EntityBuilder()
                .intField("id", 5)
                .build();
        Optional<Value> response = dao.get("test", args);


        args = new Entity.EntityBuilder()
                .intField("id", 5)
                .stringField("data", "testData")
                .stringField("anotherData", "another data")
                .build()
        dao.insert("test", args);

    }
}