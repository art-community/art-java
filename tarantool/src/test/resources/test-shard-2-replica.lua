local current = os.getenv("PWD") or io.popen("cd"):read()
local temp = os.getenv("TMPDIR") or "/tmp"
local cfg = {
    work_dir = temp .. "/tarantool/test-shard-2-replica",
    pid_file = current .. "/test-shard-2-replica.pid",
    bucket_count = 2,
    log = "file:" .. current .. "/test-shard-2-replica.log",
    election_mode = "voter",
    replication_connect_quorum = 0,
    memtx_use_mvcc_engine = true,
    replication_synchro_quorum = 2,
    replication_synchro_timeout = 60,
    sharding = require("test-sharding")
}

local initializer = require("test-shard-initializer")
require("art-tarantool")
vshard = require('vshard')
vshard.storage.cfg(cfg, 'bd13c3f6-a7e3-11ec-b909-0242ac120002')
require("art.storage").initialize()
initializer()
