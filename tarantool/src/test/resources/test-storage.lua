box.cfg {
    listen = 3301,
    pid_file = "test-storage.pid",
    log_level = 7,
    log = "file:test-storage.log",
}
box.schema.user.create('username', { password = 'password' })
box.schema.user.grant('username', 'read,write,execute,create,alter,drop', 'universe', nil)
require("art-tarantool")
require("art.storage").initialize()
