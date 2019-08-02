/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.config.extensions.tarantool;

public interface TarantoolConfigKeys {
    String TARANTOOL_SECTION_ID = "tarantool";
    String TARANTOOL_LOCAL_SECTION_ID = "tarantool.local";
    String TARANTOOL_CONFIGURATIONS_SECTION_ID = "tarantool.configurations";
    String CONNECTION_SECTION_ID = "connection";
    String INITIAL_SECTION_ID = "initial";
    String INSTANCE_MODE = "instanceMode";
    String PROBE_CONNECTION_TIMEOUT = "probeConnectionTimeout";
    String CONNECTION_TIMEOUT = "connectionTimeout";
    String INITIALIZATION_MODE = "initializationMode";
    String EXECUTABLE_APPLICATION_NAME = "executableApplicationName";
    String STARTUP_TIMEOUT_SECONDS = "startupTimeoutSeconds";
    String WORKING_DIRECTORY = "workingDirectory";
    String USERNAME = "username";
    String PASSWORD = "password";
    String BACKGROUND = "background";
    String CUSTOM_PROC_TITLE = "customProcTitle";
    String MEMTX_DIR = "memtxDir";
    String VINYL_DIR = "vinylDir";
    String WORK_DIR = "workDir";
    String PID_FILE = "pidFile";
    String READ_ONLY = "readOnly";
    String VINYL_TIMEOUT = "vinylTimeout";
    String WORKER_POOL_THREADS = "workerPoolThreads";
    String MEMTEX_MAX_TUPLE_SIZE = "memtexMaxTupleSize";
    String MEMTX_MEMORY = "memtxMemory";
    String SLAB_ALLOC_FACTOR = "slabAllocFactor";


}
