local current = os.getenv("PWD") or io.popen("cd"):read()
local temp = os.getenv("TMPDIR") or "/tmp"
local cfg = {
    work_dir = temp .. "/tarantool/test-shard-1-master",
    pid_file = current .. "/test-shard-1-master.pid",
    bucket_count = 2,
    log = "file:" .. current .. "/test-shard-1-master.log",
    election_mode = "candidate",
    replication_connect_quorum = 0,
    memtx_use_mvcc_engine = true,
    replication_synchro_quorum = 2,
    replication_synchro_timeout = 60,
    sharding = require("test-sharding")
}

local initializer = require("test-shard-initializer")
require("art-tarantool")
vshard = require('vshard')
vshard.storage.cfg(cfg, '8a274925-a26d-47fc-9e1b-af88ce939412')
require("art.storage").initialize()
initializer()
