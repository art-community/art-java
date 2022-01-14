/*
 * ART
 *
 * Copyright 2019-2022 ART
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

import lombok.*;
import static java.lang.Integer.*;
import static java.time.Duration.*;
import java.time.*;


public interface TarantoolModuleConstants {
    int DEFAULT_TARANTOOL_CONNECTIONS_NUMBER = 1;
    Duration DEFAULT_TARANTOOL_CONNECTION_TIMEOUT = ofSeconds(5);
    Duration DEFAULT_TARANTOOL_READ_TIMEOUT = ofSeconds(15);
    int DEFAULT_TARANTOOL_PORT = 3301;
    int RECEIVERS_INITIAL_SIZE = 8129;
    int RECEIVERS_POOL_MAXIMUM = MAX_VALUE / 128;
    int REQUEST_ID_STEP = 8;
    String DEFAULT_TARANTOOL_HOST = "localhost";
    String DEFAULT_TARANTOOL_USERNAME = "guest";
    String DEFAULT_TARANTOOL_PASSWORD = "";
    String DEFAULT_TARANTOOL_CLUSTER_NAME = "default";


    int DEFAULT_TARANTOOL_RETRIES = 3;

    interface ProtocolConstants {
        int VERSION_LENGTH = 64;
        int SALT_LENGTH = 44;
        int GREETING_LENGTH = 128;
        int MINIMAL_HEADER_SIZE = 5;
        int AUTHENTICATION_LENHGT = 20;

        int IPROTO_CODE = 0x00;
        int IPROTO_SYNC = 0x01;
        int IPROTO_CALL = 0x0a;
        int IPROTO_USER_NAME = 0x23;
        int IPROTO_AUTH_DATA = 0x21;
        int IPROTO_TUPLE = 0x21;
        int IPROTO_FUNCTION_NAME = 0x22;
        int IPROTO_AUTH = 0x07;
        int IPROTO_OK = 0x00;
        int IPROTO_NOT_OK = 0x8000;
        int IPROTO_BODY_DATA = 0x30;
        int IPROTO_BODY_ERROR = 0x31;
    }

    interface AuthenticationMechanism {
        String CHAP_SHA1 = "chap-sha1";
    }

    interface ConfigurationKeys {
        String TARANTOOL_SECTION = "tarantool";
        String TARANTOOL_LOGGING_KEY = "logging";
        String TARANTOOL_CLUSTERS_SECTION = "clusters";
        String TARANTOOL_CLUSTER_BALANCING_METHOD = "balancing";
        String TARANTOOL_INSTANCES_SECTION = "instances";
        String TARANTOOL_INSTANCE_HOST_KEY = "host";
        String TARANTOOL_INSTANCE_PORT_KEY = "port";
        String TARANTOOL_INSTANCE_IMMUTABLE_KEY = "immutable";
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
        String CONFIGURATION_IS_NULL = "Tarantool cluster ''{0}'', instance ''{1}'' configuration is null. Please specify it.";
        String CLUSTER_CONFIGURATION_IS_NULL = "Tarantool cluster ''{0}'' configuration is null. Please specify it.";
        String UNABLE_TO_CONNECT_TO_TARANTOOL = "Unable to connect to tarantool ''{0}'' with address ''{1}''.";
        String UNABLE_TO_CONNECT_TO_TARANTOOL_RETRY = "Unable to connect to tarantool ''{0}'' with address ''{1}''. Retrying...\n";
        String RESULT_IS_INVALID = "Response ''{0}'' returned from Tarantool can`t be converted to Entity.";
        String UNABLE_TO_GET_RESPONSE = "Unable to get response from function call.";
        String UNKNOWN_BALANCING_METHOD = "Unknown processInitialization balancing method: ''{0}''";
        String NULL_REQUEST_DATA_EXCEPTION = "Request data tuple is null.";
        String TRANSACTION_FAILED = "Transaction failed with error message: ''{0}''";
        String GET_RESULT_OF_NOT_COMMITTED_TRANSACTION = "Attempt to get result of not committed transaction. Commit first.";
        String ILLEGAL_TRANSACTION_DEPENDENCY_USAGE = "Attempt to use transaction dependency outside of transaction. Use response data using get() instead.";
        String ATTEMPT_OF_NESTED_TRANSACTION = "Illegal attempt to begin nested transaction. Cluster is already in another transaction.";
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

        String CREATE_SPACE = "art.api.space.createLogger";
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

        String TRANSACTION = "art.api.transaction";
    }

    interface SelectOptions {
        String LIMIT = "limit";
        String OFFSET = "offset";
        String FILTER = "filter";
        String SORT = "sort";
        String DISTINCT = "distinct";
    }

    interface SelectFilters {
        String EQUALS = "art.core.stream.filters.equals";
        String NOT_EQUALS = "art.core.stream.filters.notEquals";
        String MORE = "art.core.stream.filters.more";
        String LESS = "art.core.stream.filters.less";
        String IN_RANGE = "art.core.stream.filters.inRange";
        String MOT_IN_RANGE = "art.core.stream.filters.notInRange";

        String LIKE = "art.core.stream.filters.like";
        String STARTS_WITH = "art.core.stream.filters.startsWith";
        String ENDS_WITH = "art.core.stream.filters.endsWith";
        String CONTAINS = "art.core.stream.filters.contains";
    }

    interface SelectSortComparator {
        String DESCENDING = "art.core.stream.comparators.greater";
        String ASCENDING = "art.core.stream.comparators.less";
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

    enum TarantoolIndexIterator {
        EQ,
        REQ,
        GT,
        GE,
        ALL,
        LT,
        LE,
        OVERLAPS,
        NEIGHBOR
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
