local current = os.getenv("PWD") or io.popen("cd"):read()
local temp = os.getenv("TMPDIR") or "/tmp"
local cfg = {
    work_dir = temp .. "/tarantool/test-shard-1-replica",
    pid_file = current .. "/test-shard-1-replica.pid",
    bucket_count = 2,
    log = "file:" .. current .. "/test-shard-1-replica.log",
    election_mode = "voter",
    replication_connect_quorum = 0,
    replication_synchro_quorum = 2,
    sharding = require("test-sharding")
}

require("art-tarantool")
vshard = require('vshard')
vshard.storage.cfg(cfg, 'ce1f21d6-a7e3-11ec-b909-0242ac120002')
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
