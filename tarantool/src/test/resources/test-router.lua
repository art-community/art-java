local current = os.getenv("PWD") or io.popen("cd"):read()
local temp = os.getenv("TMPDIR") or "/tmp"
local cfg = {
    background = true,
    listen = 3302,
    work_dir = temp .. "/tarantool/test-router",
    pid_file = current .. "/test-router.pid",
    bucket_count = 2,
    log = "file:" .. current .. "/test-router.log",
    replication_connect_quorum = 0,
    replication_synchro_quorum = 2,
    sharding = require("test-sharding")
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

bootstrap = function()
    vshard.router.bootstrap()
    fiber = require('fiber')
    yaml = require("yaml")
    log = require("log")
    local function waitFunction()
        while true do
            local info = vshard.router.info()

            log.info(yaml.encode(info))

            local available = info["status"] == 0

            for _, set in pairs(info["replicasets"]) do
                available = available and set["replica"]["status"] == "available"
            end

            if available then
                return
            else
                fiber.sleep(1)
            end
        end
    end
    local waiter = fiber.new(waitFunction)
    waiter:set_joinable(true)
    waiter:join()
end

box.schema.func.create("bootstrap", { if_not_exists = true })

