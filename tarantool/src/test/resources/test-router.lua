local cfg = {
    listen = 3302,
    pid_file = "test-router.pid",
    log = "file:test-router.log",
    sharding = {
        ['cbf06940-0790-498b-948d-042b62cf3d29'] = {
            replicas = {
                ['8a274925-a26d-47fc-9e1b-af88ce939412'] = {
                    uri = 'username:password@127.0.0.1:3303',
                    master = true
                },
            },
        },
        ['ac522f65-aa94-4134-9f64-51ee384f1a54'] = {
            replicas = {
                ['1e02ae8a-afc0-4e91-ba34-843a356b8ed7'] = {
                    uri = 'username:password@127.0.0.1:3304',
                    master = true
                },
            },
        },
    },
}

require("art-tarantool")
vshard = require('vshard')
vshard.router.cfg(cfg)
require("art.router").initialize()

box.schema.user.create('username', { password = 'password', if_not_exists = true })
box.schema.user.grant('username', 'read,write,execute,create,alter,drop', 'universe', nil, { if_not_exists = true })

testSubscription = function()
    local subscription = require("art.storage.subscription")
    subscription.publish("test", "testEmpty")
    subscription.publish("test", "testRequest", { 1, "test" })
    subscription.publish("test", "testChannel", { 1, "test" })
    subscription.publish("test", "testChannel", { 1, "test" })
end
box.schema.func.create("testSubscription", { if_not_exists = true })

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