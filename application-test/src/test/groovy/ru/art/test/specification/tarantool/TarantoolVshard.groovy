
package ru.art.test.specification.tarantool


import ru.art.tarantool.storage.vshard.connector.VshardCartridgeConnector
import spock.lang.Specification

import static ru.art.entity.Entity.entityBuilder

class TarantoolVshard extends Specification {
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
        /*
        VshardStandardConnector std = new VshardStandardConnector();
        TarantoolClient stdClient = std.getClient();
        std.testCall(stdClient);
        std.testEval(stdClient);
        */
        VshardCartridgeConnector cart = new VshardCartridgeConnector();
        io.tarantool.driver.api.TarantoolClient cartClient = cart.getClient();
        cart.testCall(cartClient);
        cart.testEval(cartClient);

        String luaFunction = "function hello()\n " +
                "return 'Hi'\n " +
                "end";
        cart.uploadFunction(cartClient, luaFunction);


    }
}