
package ru.art.test.specification.tarantool

import io.tarantool.driver.DefaultTarantoolTupleFactory
import io.tarantool.driver.StandaloneTarantoolClient
import io.tarantool.driver.api.TarantoolTupleFactory
import io.tarantool.driver.auth.SimpleTarantoolCredentials
import io.tarantool.driver.mappers.DefaultMessagePackMapperFactory
import spock.lang.Specification

class TarantoolExperimental extends Specification {

    def router1Address = "localhost:3311"
    def storage1Address = "localhost:3301"
    def storage2Address = "localhost:3302"
    def username = 'username'
    def password = 'password'
    
    
    def "cartridge_connector"(){
        setup:

        SimpleTarantoolCredentials creds = new SimpleTarantoolCredentials(username, password)

        StandaloneTarantoolClient client = new StandaloneTarantoolClient(creds, "localhost", 3301)

        DefaultMessagePackMapperFactory mapperFactory = DefaultMessagePackMapperFactory.getInstance();
        TarantoolTupleFactory tupleFactory =
                new DefaultTarantoolTupleFactory(mapperFactory.defaultComplexTypesMapper());


        List<Integer> list = new LinkedList<Integer>()
        list.push(123)
        list.push(234)
        def tuple = tupleFactory.create(router1Address, 1234, list, 'data')


        //List<?> result = client.call('test', tuple).get()
    }
    
    def "index_test"(){
        setup:
        SimpleTarantoolCredentials creds = new SimpleTarantoolCredentials(username, password)

        StandaloneTarantoolClient client = new StandaloneTarantoolClient(creds, "localhost", 3301)

        def spaces = client.call('art.api.space.list').get()
        def indices = client.call('art.api.space.list_indices', 'tst1').get()
        def indices2 = client.call('art.api.space.list_indices', 'tst2').get()
    }

}