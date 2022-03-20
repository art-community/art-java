local cfg = {
    listen = 3305,
    pid_file = "test-shard-2-master.pid",
    log = "file:test-shard-2-master.log",
    sharding = require("sharding"),
}

require("art-tarantool")
vshard = require('vshard')
vshard.storage.cfg(cfg, '1e02ae8a-afc0-4e91-ba34-843a356b8ed7')
require("art.storage").initialize()

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
