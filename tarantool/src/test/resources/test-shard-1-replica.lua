local current = os.getenv("PWD") or io.popen("cd"):read()
local temp = os.getenv("TMPDIR") or "/tmp"
local cfg = {
    work_dir = temp .. "/tarantool/test-shard-1-replica",
    pid_file = current .. "/test-shard-1-replica.pid",
    bucket_count = 2,
    log = "file:" .. current .. "/test-shard-1-replica.log",
    election_mode = "voter",
    replication_connect_quorum = 0,
    memtx_use_mvcc_engine = true,
    replication_synchro_quorum = 2,
    sharding = require("test-sharding")
}

local initializer = require("test-replica-initializer")
require("art-tarantool")
vshard = require('vshard')
vshard.storage.cfg(cfg, 'ce1f21d6-a7e3-11ec-b909-0242ac120002')
require("art.storage").initialize()
initializer()
