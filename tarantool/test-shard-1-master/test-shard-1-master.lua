local current = os.getenv("PWD") or io.popen("cd"):read()
local cfg = {
    work_dir = "/tmp/tarantool/test-shard-1-master",
    pid_file = current .. "/test-shard-1-master.pid",
    bucket_count = 2,
    log = "file:" .. current .. "/test-shard-1-master.log",
    sharding = {
        ['cbf06940-0790-498b-948d-042b62cf3d29'] = {
            replicas = {
                ['8a274925-a26d-47fc-9e1b-af88ce939412'] = {
                    uri = 'username:password@127.0.0.1:3303',
                    name = 'test-shard-1-master',
                    master = true
                },
                ['ce1f21d6-a7e3-11ec-b909-0242ac120002'] = {
                    uri = 'username:password@127.0.0.1:3304',
                    name = 'test-shard-1-replica',
                    master = false
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
                    name = 'test-shard-2-replica',
                    master = false
                },
            },
        },
    },
    election_mode = "candidate",
    replication_connect_quorum = 0,
    memtx_use_mvcc_engine = true,
    replication_synchro_quorum = 2,
}

require("art-tarantool")
vshard = require('vshard')
vshard.storage.cfg(cfg, '8a274925-a26d-47fc-9e1b-af88ce939412')
require("art.storage").initialize()

box.once("main", function()
    box.schema.user.create('username', { password = 'password', if_not_exists = true })
    box.schema.user.grant('username', 'read,write,execute,create,alter,drop', 'universe', nil, { if_not_exists = true })

    testChannel = function()
        box.session.push("test")
        box.session.push("test")
    end
    box.schema.func.create("testChannel", { if_not_exists = true })

    testMapper = function(data)
        return data[16] .. " - mapped"
    end
    box.schema.func.create("testMapper", { if_not_exists = true })

    testFilter = function(data)
        return data[9] > 3
    end
    box.schema.func.create("testFilter", { if_not_exists = true })
end)
