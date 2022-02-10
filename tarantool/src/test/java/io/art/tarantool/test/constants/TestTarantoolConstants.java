package io.art.tarantool.test.constants;

import static io.art.core.converter.WslPathConverter.*;
import static io.art.core.extensions.RandomExtensions.*;

public interface TestTarantoolConstants {
    String WORKING_DIRECTORY = "test-" + randomPositiveInt();
    String COMMAND = "cd " + convertToWslPath(WORKING_DIRECTORY) + " && /usr/bin/tarantool";
    String BASH = "bash";
    String BASH_ARGUMENT = "-c";
    String WRONG_ENVIRONMENT = "Tarantool supports only Linux and OSX";
    String STORAGE_SCRIPT = "test-storage.lua";
    String EXECUTABLE_NOT_FOUND = "Tarantool executable has not found";
    int STORAGE_PORT = 3301;
    String USERNAME = "username";
    String PASSWORD = "password";
    String MODULE_SCRIPT = "art-tarantool.lua";
}
