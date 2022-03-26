package io.art.tarantool.test.constants;

import org.msgpack.value.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.converter.WslPathConverter.*;
import static java.lang.System.*;
import static org.msgpack.value.ValueFactory.*;

public interface TestTarantoolConstants {
    String STORAGE_DIRECTORY = "test-storage";
    String STORAGE_PID = "test-storage.pid";
    String STORAGE_SCRIPT = "test-storage.lua";
    int STORAGE_PORT = 3301;

    String ROUTER_DIRECTORY = "test-router";
    String ROUTER_PID = "test-router.pid";
    String ROUTER_SCRIPT = "test-router.lua";
    ImmutableStringValue ROUTER_BOOTSTRAP_FUNCTION = newString("bootstrap");
    int ROUTER_PORT = 3302;

    String SHARD_1_MASTER_DIRECTORY = "test-shard-1-master";
    String SHARD_1_MASTER_PID = "test-shard-1-master.pid";
    String SHARD_1_MASTER_SCRIPT = "test-shard-1-master.lua";
    int SHARD_1_MASTER_PORT = 3303;

    String SHARD_1_REPLICA_DIRECTORY = "test-shard-1-replica";
    String SHARD_1_REPLICA_PID = "test-shard-1-replica.pid";
    String SHARD_1_REPLICA_SCRIPT = "test-shard-1-replica.lua";
    int SHARD_1_REPLICA_PORT = 3304;

    String SHARD_2_MASTER_DIRECTORY = "test-shard-2-master";
    String SHARD_2_MASTER_PID = "test-shard-2-master.pid";
    String SHARD_2_MASTER_SCRIPT = "test-shard-2-master.lua";
    int SHARD_2_MASTER_PORT = 3305;

    String SHARD_2_REPLICA_DIRECTORY = "test-shard-2-replica";
    String SHARD_2_REPLICA_PID = "test-shard-2-replica.pid";
    String SHARD_2_REPLICA_SCRIPT = "test-shard-2-replica.lua";
    int SHARD_2_REPLICA_PORT = 3306;

    String KILL_COMMAND = "kill -9 ";
    String BASH = "bash";
    String BASH_ARGUMENT = "-c";
    String USERNAME = "username";
    String PASSWORD = "password";
    String MODULE_SCRIPT = "art-tarantool.lua";
    String SHARDING_SCRIPT = "test-sharding.lua";
    String MKDIR_COMMAND = "mkdir -p ";
    String TEMP_DIRECTORY = orElse(getenv("TMPDIR"), "/tmp") + "/tarantool";
    String DELETE_COMMAND = "rm -rf ";

    static String instanceCommand(String path) {
        return "cd " + convertToWslPath(path) + " && tarantool";
    }
}
