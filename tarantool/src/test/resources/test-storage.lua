local current = os.getenv("PWD") or io.popen("cd"):read()
local temp = os.getenv("TMPDIR") or "/tmp"

require("art-tarantool")
box.cfg {
    background = true,
    listen = 3301,
    pid_file = current .. "/test-storage.pid",
    log = "file:" .. current .. "/test-storage.log",
    work_dir = temp .. "/tarantool/test-storage",
}
require("art.storage").initialize()

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

await = function()
    fiber = require('fiber')
    yaml = require("yaml")
    log = require("log")
    function waiter()
        local info = box.info()
        while info.status ~= "running" do
            log.info(yaml.encode(info))
            fiber.sleep(1)
        end
    end
    local waiter = fiber.new(waiter)
    waiter:set_joinable(true)
    waiter:join()
end
box.schema.func.create("await", { if_not_exists = true })
