/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.tarantool.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;


public interface TarantoolModuleConstants {
    int DEFAULT_TARANTOOL_CONNECTION_TIMEOUT = 3 * 1000;
    int DEFAULT_TARANTOOL_CONNECTIONS_NUMBER = 1;
    int DEFAULT_TARANTOOL_READ_TIMEOUT = 1000;
    int DEFAULT_TARANTOOL_REQUEST_TIMEOUT = 2 * 1000;
    int DEFAULT_TARANTOOL_PORT = 3301;
    String DEFAULT_TARANTOOL_HOST = "localhost";
    String DEFAULT_TARANTOOL_USERNAME = "guest";
    String DEFAULT_TARANTOOL_PASSWORD = "";

    int DEFAULT_TARANTOOL_RETRIES = 3;

    interface ConfigurationKeys{
        String TARANTOOL_SECTION = "tarantool";
        String TARANTOOL_LOGGING_KEY = "logging";
        String TARANTOOL_CLUSTERS_SECTION = "clusters";
        String TARANTOOL_CLUSTER_BALANCING_METHOD = "balancing";
        String TARANTOOL_INSTANCES_SECTION = "instances";
        String TARANTOOL_INSTANCE_HOST_KEY = "host";
        String TARANTOOL_INSTANCE_PORT_KEY = "port";
        String TARANTOOL_INSTANCE_READABLE_KEY = "readable";
        String TARANTOOL_INSTANCE_WRITEABLE_KEY = "writeable";
        String TARANTOOL_INSTANCE_USERNAME_KEY = "username";
        String TARANTOOL_INSTANCE_PASSWORD_KEY = "password";
        String TARANTOOL_INSTANCE_CONNECTIONS_KEY = "connections";
        String TARANTOOL_INSTANCE_CONNECTION_TIMEOUT_KEY = "connection_timeout";
        String TARANTOOL_INSTANCE_READ_TIMEOUT_KEY = "read_timeout";
        String TARANTOOL_INSTANCE_REQUEST_TIMEOUT_KEY = "request_timeout";
        String TARANTOOL_INSTANCE_MAX_CONNECTIONS_RETRY_KEY = "max_connections_retry";
    }

    interface ExceptionMessages {
        String CONFIGURATION_IS_NULL = "Tarantool ''{0}'' configuration is null. Please specify it.";
        String CLUSTER_CONFIGURATION_IS_NULL = "Tarantool cluster ''{0}'' configuration is null. Please specify it.";
        String UNABLE_TO_CONNECT_TO_TARANTOOL = "Unable to connect to tarantool ''{0}'' with address ''{1}''.";
        String UNABLE_TO_CONNECT_TO_TARANTOOL_RETRY = "Unable to connect to tarantool ''{0}'' with address ''{1}''. Retrying...\n";
        String RESULT_IS_INVALID = "Response ''{0}'' returned from Tarantool can`t be converted to Entity";
        String UNABLE_TO_GET_RESPONSE = "Unable to get response from function call";
        String UNKNOWN_BALANCING_METHOD = "Unknown load balancing method: ''{0}''";
    }

    interface LoggingMessages {
        String TARANTOOL_CLIENT_CREATED = "Created tarantool client ''{0}'' with address ''{1}''";
        String CALLING_FUNCTION = "Calling tarantool function ''{0}''";
        String CALLED_FUNCTION = "Called tarantool function ''{0}'' with result: {1}";
        String FAILED_FUNCTION = "Failed to call tarantool function ''{0}''";
    }

    interface Functions {
        String PUT = "art.api.put";
        String GET = "art.api.get";
        String REPLACE = "art.api.replace";
        String SELECT = "art.api.select";
        String DELETE = "art.api.delete";
        String INSERT = "art.api.insert";
        String UPDATE = "art.api.update";
        String UPSERT = "art.api.upsert";
        String AUTO_INCREMENT = "art.api.autoIncrement";

        String CREATE_SPACE = "art.api.space.create";
        String FORMAT_SPACE = "art.api.space.format";
        String CREATE_INDEX = "art.api.space.createIndex";
        String DROP_INDEX = "art.api.space.dropIndex";
        String DROP_SPACE = "art.api.space.drop";
        String RENAME_SPACE = "art.api.space.rename";
        String TRUNCATE = "art.api.space.truncate";
        String COUNT = "art.api.space.count";
        String LEN = "art.api.space.len";
        String SCHEMA_COUNT = "art.api.space.schemaCount";
        String SCHEMA_LEN = "art.api.space.schemaLen";
        String LIST_SPACES = "art.api.space.list";
        String LIST_INDICES = "art.api.space.listIndices";
    }

    enum TarantoolFieldType {
        UNSIGNED,
        STRING,
        INTEGER,
        NUMBER,
        BOOLEAN,
        ARRAY,
        SCALAR
    }

    enum TarantoolIndexType {
        HASH,
        TREE,
        BITSET,
        RTREE
    }

    @Getter
    @AllArgsConstructor
    enum TarantoolOperator {
        ADDITION("+"),
        SUBTRACTION("-"),
        AND("&"),
        OR("|"),
        XOR("^"),
        STRING_SPLICE(":"),
        INSERTION("!"),
        DELETION("#"),
        ASSIGMENT("=");

        private final String operator;
    }
}
