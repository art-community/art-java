return {
    ['cbf06940-0790-498b-948d-042b62cf3d29'] = {
        replicas = {
            ['8a274925-a26d-47fc-9e1b-af88ce939412'] = {
                uri = 'username:password@127.0.0.1:3303',
                name = 'test-shard-1-master',
                master = true
            },
            ['ce1f21d6-a7e3-11ec-b909-0242ac120002'] = {
                uri = 'username:password@127.0.0.1:3304',
                name = 'test-shard-1-replica'
            },
        },
    },
    ['ac522f65-aa94-4134-9f64-51ee384f1a54'] = {
        replicas = {
            ['1e02ae8a-afc0-4e91-ba34-843a356b8ed7'] = {
                uri = 'username:password@127.0.0.1:3305',
                master = true,
                name = 'test-shard-2-master'
            },
            ['bd13c3f6-a7e3-11ec-b909-0242ac120002'] = {
                uri = 'username:password@127.0.0.1:3306',
                name = 'test-shard-2-replica'
            },
        },
    },
}
