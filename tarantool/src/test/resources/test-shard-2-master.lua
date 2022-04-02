local current = os.getenv("PWD") or io.popen("cd"):read()
local temp = os.getenv("TMPDIR") or "/tmp"
local cfg = {
    work_dir = temp .. "/tarantool/test-shard-2-master",
    pid_file = current .. "/test-shard-2-master.pid",
    bucket_count = 2,
    log = "file:" .. current .. "/test-shard-2-master.log",
    election_mode = "candidate",
    replication_connect_quorum = 0,
    memtx_use_mvcc_engine = true,
    replication_synchro_quorum = 2,
    sharding = require("test-sharding")
}

local initializer = require("test-master-initializer")
require("art-tarantool")
vshard = require('vshard')
vshard.storage.cfg(cfg, '1e02ae8a-afc0-4e91-ba34-843a356b8ed7')
require("art.storage").initialize()
initializer()
