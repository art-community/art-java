package io.art.tarantool.test.constants;

import static io.art.core.converter.WslPathConverter.*;

public interface TestTarantoolConstants {
    String STORAGE_DIRECTORY = "test-storage";
    String STORAGE_COMMAND = "cd " + convertToWslPath(STORAGE_DIRECTORY) + " && tarantool";
    String STORAGE_PID = "test-storage.pid";
    String STORAGE_SCRIPT = "test-storage.lua";
    int STORAGE_PORT = 3301;

    String ROUTER_DIRECTORY = "test-router";
    String ROUTER_COMMAND = "cd " + convertToWslPath(ROUTER_DIRECTORY) + " && tarantool";
    String ROUTER_PID = "test-router.pid";
    String ROUTER_SCRIPT = "test-router.lua";
    int ROUTER_PORT = 3302;

    String SHARD_1_DIRECTORY = "test-shard-1";
    String SHARD_1_COMMAND = "cd " + convertToWslPath(SHARD_1_DIRECTORY) + " && tarantool";
    String SHARD_1_PID = "test-shard-1.pid";
    String SHARD_1_SCRIPT = "test-shard-1.lua";
    int SHARD_1_PORT = 3303;

    String SHARD_2_DIRECTORY = "test-shard-2";
    String SHARD_2_COMMAND = "cd " + convertToWslPath(SHARD_2_DIRECTORY) + " && tarantool";
    String SHARD_2_PID = "test-shard-2.pid";
    String SHARD_2_SCRIPT = "test-shard-2.lua";
    int SHARD_2_PORT = 3304;

    String KILL_COMMAND = "kill -9 ";
    String BASH = "bash";
    String BASH_ARGUMENT = "-c";
    String USERNAME = "username";
    String PASSWORD = "password";
    String MODULE_SCRIPT = "art-tarantool.lua";
}
