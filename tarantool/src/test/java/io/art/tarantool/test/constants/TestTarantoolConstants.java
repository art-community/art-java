package io.art.tarantool.test.constants;

import static io.art.core.converter.WslPathConverter.*;
import static io.art.core.determiner.SystemDeterminer.isWindows;

public interface TestTarantoolConstants {
    String STORAGE_DIRECTORY = "test-storage";
    String STORAGE_COMMAND = "cd " + convertToWslPath(STORAGE_DIRECTORY) + " && /usr/bin/tarantool";
    String STORAGE_PID = "test-storage.pid";
    String KILL_COMMAND = "kill -9 ";
    String BASH = isWindows() ? "bash" : "/usr/bin/bash";
    String BASH_ARGUMENT = "-c";
    String STORAGE_SCRIPT = "test-storage.lua";
    int STORAGE_PORT = 3301;
    String USERNAME = "username";
    String PASSWORD = "password";
    String MODULE_SCRIPT = "art-tarantool.lua";
}
