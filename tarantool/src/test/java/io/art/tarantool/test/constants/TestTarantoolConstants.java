package io.art.tarantool.test.constants;

import java.nio.file.*;

public interface TestTarantoolConstants {
    Path EXECUTABLE = Paths.get("/usr/bin/tarantool");
    String STORAGE_SCRIPT = "test-storage.lua";
    String EXECUTABLE_NOT_FOUND = "Tarantool executable has not found";
    int STORAGE_PORT = 3301;
    String USERNAME = "username";
    String PASSWORD = "password";
    String MODULE_SCRIPT = "art-tarantool.lua";
}
